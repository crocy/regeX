package com.crocx.regex.engine;

import com.crocx.regex.util.Logger;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Croc on 30.11.2013.
 */
public class RegexEngine {

    public static final int GROUP_SPECIAL_CHARACTERS = 1;
    public static final int GROUP_SPECIAL_CHARACTERS_QUANTIFIER = 2;
    public static final int GROUP_LITERALS = 3;
    public static final int GROUP_QUANTIFIERS_CHARACTER = 4;
    public static final int GROUP_QUANTIFIERS_QUANTIFIER = 5;

    private static final int[] QUANTIFIER_INTERVAL_ZERO_OR_ONE = new int[] { 0, 1 };
    private static final int[] QUANTIFIER_INTERVAL_ZERO_OR_INF = new int[] { 0, Integer.MAX_VALUE };
    private static final int[] QUANTIFIER_INTERVAL_ONE_OR_INF = new int[] { 1, Integer.MAX_VALUE };

    //    (?:\\\w)?(\w+)?(?![+?*]) -- 1 group: literals
    //    (\\\w)?(\w+(?![+?*]))?(\w[+?*])? -- 3 groups: \chars; literals; quantifiers
    //    (\\\w([+?*])?)?(\w+(?![+?*]))?(\w?([+?*]))? -- 5 groups: (\char(quantifier))(literal)(char(quantifier))
    //    ((\\)?(\w)([+?*])?)?(\w*?) -- 5 groups: ((\start_of_special_char)(char)(quantifier))(literal{layz_matching})

    private LinkedList<MatcherResult> matches;
    private LinkedList<RegexExplanation> explanations;

    private boolean processPerWord = true;

    private Pattern regexPattern = Pattern.compile("(\\\\\\w([+?*])?)?(\\w+(?![+?*]))?(\\w?([+?*]))?");
    //    private Pattern regexPattern = Pattern.compile("((\\\\)?(\\w)([+?*])?)?(\\w*?)");
    private Matcher regexMatcher;
    private String specialChar, specialQuantifier;
    private String literals;
    private String quantifierChar, quantifierQuantifier;

    public LinkedList<MatcherResult> processRegexAndInput(String regex, String input) {
        String[] inputArray;

        if (processPerWord) {
            inputArray = input.split(" ");
        } else {
            inputArray = new String[] { input };
        }

        MatcherResult match;
        matches = new LinkedList<MatcherResult>();

        //        explanations = new LinkedList<RegexExplanation>();
        // has to be called before processing regex with input to set up some things beforehand
        generateExplanations(regex);

        String processInput;
        int matchOffset = 0;
        int regionStart, regionEnd;
        int lastSuccessfulGroupMatchIndex = -1;

        /*
         * TODO: very bad practice - try to link matches with explanations in a better way. It only works for now
         * because the mainLoop in this method is the same as in generateExplanations(regex).
         */
        int explanationIndex;

        forLoop: //
        for (int i = 0; i < inputArray.length; i++) {
            processInput = inputArray[i];
            regexMatcher.reset();

            //            regionStart = 0;
            regionEnd = processInput.length();
            explanationIndex = 0;

            mainLoop: //
            while (regexMatcher.find()) {
                if (regexMatcher.group() == null || regexMatcher.group().isEmpty()) {
                    // found empty string (probably because nothing in regexPattern is mandatory), continue
                    continue;
                }
                regionStart = regexMatcher.start();

                specialChar = regexMatcher.group(GROUP_SPECIAL_CHARACTERS);
                specialQuantifier = regexMatcher.group(GROUP_SPECIAL_CHARACTERS_QUANTIFIER);
                literals = regexMatcher.group(GROUP_LITERALS);
                quantifierChar = regexMatcher.group(GROUP_QUANTIFIERS_CHARACTER);
                // is already included in quantifierChar!
                quantifierQuantifier = regexMatcher.group(GROUP_QUANTIFIERS_QUANTIFIER);

                /*
                 * Special characters matching.
                 */
                if (specialChar != null && specialChar.length() >= 2 && specialChar.length() <= 3) {
                    Pattern specialCharPattern = Pattern.compile(specialChar);
                    Matcher specialCharMatcher = specialCharPattern.matcher(processInput);
                    specialCharMatcher.region(regionStart, regionEnd);

                    if (specialCharMatcher.find()) {
                        lastSuccessfulGroupMatchIndex = GROUP_SPECIAL_CHARACTERS;

                        match = new MatcherResult();
                        match.setMatch(specialCharMatcher.group(), matchOffset + specialCharMatcher.start(),
                                matchOffset + specialCharMatcher.end());
                        match.setPattern(specialCharMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.end(GROUP_SPECIAL_CHARACTERS));
                        //                        match.setExplanation(explanation);
                        match.setExplanation(explanations.get(explanationIndex));
                        matches.add(match);
                    }

                    explanationIndex++;
                } else {
                    specialChar = "";
                }

                /*
                 * Literals matching.
                 */
                if (literals != null && !literals.isEmpty()) {
                    Pattern literalPattern;
                    Matcher literalMatcher;
                    StringBuilder subLiteral = new StringBuilder();

                    for (int j = 0; j < literals.length(); j++) {
                        subLiteral.append(literals.charAt(j));

                        literalPattern = Pattern.compile(specialChar + subLiteral.toString());
                        literalMatcher = literalPattern.matcher(processInput);
                        literalMatcher.region(regionStart, regionEnd);

                        match = new MatcherResult();
                        //                        match.setExplanation(explanation);
                        match.setExplanation(explanations.get(explanationIndex));
                        matches.add(match);

                        // literal matcher should always find exactly one match per query
                        if (!literalMatcher.find()) {
                            int groupStart = regexMatcher.start(GROUP_LITERALS);
                            match.setType(MatcherResult.ResultType.MISMATCH);
                            match.setPattern(literalMatcher.pattern().pattern(), groupStart + j, groupStart + j + 1);
                            //                            match.setMatch(null, offset, offset + literalMatcher.regionEnd());
                            //                            match.setMatch(null, offset + j, offset + j + 1);

                            subLiteral.deleteCharAt(j); // take one step backwards and try to find last matching position
                            String tempPattern = specialChar + subLiteral.toString();
                            literalPattern = Pattern.compile(tempPattern);
                            literalMatcher = literalPattern.matcher(processInput);
                            literalMatcher.region(regionStart, regionEnd);

                            if (tempPattern.isEmpty() || !literalMatcher.find()) {
                                match.setMatch(null, regionStart, regionEnd);
                            } else {
                                match.setMatch(null, matchOffset + literalMatcher.end(),
                                        matchOffset + literalMatcher.end() + 1);
                            }

                            break mainLoop;
                        }
                        lastSuccessfulGroupMatchIndex = GROUP_LITERALS;

                        match.setPattern(literalMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.start(GROUP_LITERALS) + subLiteral.length());
                        match.setMatch(literalMatcher.group(), matchOffset + literalMatcher.start(), matchOffset
                                + literalMatcher.end());
                    }
                    explanationIndex++;

                } else {
                    literals = "";
                }

                /*
                 * Quantifiers matching.
                 */
                if (quantifierChar != null && quantifierChar.length() >= 2 && quantifierChar.length() <= 3) {
                    // else contition is more interesting :P
                } else {
                    quantifierChar = "";
                    explanationIndex--;
                    Logger.info("No quantifier found.");
                }

                Pattern quantifiersPattern = Pattern.compile(specialChar + literals + quantifierChar);
                Matcher quantifiersMatcher = quantifiersPattern.matcher(processInput);
                quantifiersMatcher.region(regionStart, regionEnd);

                int quantifiersFound = 0;
                while (quantifiersMatcher.find()) {
                    if (!quantifierChar.isEmpty()) {
                        lastSuccessfulGroupMatchIndex = GROUP_QUANTIFIERS_CHARACTER;
                    }
                    quantifiersFound++;

                    match = new MatcherResult();
                    match.setMatch(quantifiersMatcher.group(), matchOffset + quantifiersMatcher.start(), matchOffset
                            + quantifiersMatcher.end());
                    //                    match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(),
                    //                            regexMatcher.end(GROUP_QUANTIFIERS_CHARACTER));
                    match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(),
                            regexMatcher.end(lastSuccessfulGroupMatchIndex));

                    //                        match.setExplanation(explanation);
                    match.setExplanation(explanations.get(explanationIndex));
                    matches.add(match);
                }

                String quantifier = null;
                if (quantifierQuantifier != null && !quantifierQuantifier.isEmpty()) {
                    quantifier = quantifierQuantifier;
                } else if (specialQuantifier != null && !specialQuantifier.isEmpty() && literals.isEmpty()) {
                    quantifier = specialQuantifier;
                }

                if (quantifier != null) {
                    int[] quantifierInterval = parseQuantifier(quantifier.charAt(0));
                    // check if there are at least the minimum and at most maximum number of matches found
                    if (quantifiersFound < quantifierInterval[0] || quantifiersFound > quantifierInterval[1]) {
                        match = new MatcherResult();
                        match.setType(MatcherResult.ResultType.MISMATCH);
                        //                        match.setMatch(quantifiersMatcher.group(), offset + quantifiersMatcher.start(), offset
                        //                                + quantifiersMatcher.end());
                        match.setMatch(null, matchOffset, matchOffset + quantifiersMatcher.regionEnd());
                        //                    match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(),
                        //                            regexMatcher.end(GROUP_QUANTIFIERS_CHARACTER));
                        match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.end(lastSuccessfulGroupMatchIndex));

                        //                        match.setExplanation(explanation);
                        match.setExplanation(explanations.get(explanationIndex));
                        //                        match.setExplanation(quantifierExplanation);
                        matches.add(match);
                    }
                }
            }

            matchOffset += processInput.length() + 1; // +1 for the space ' '
        }

        return matches;
    }

    private void generateExplanations(String regex) {
        explanations = new LinkedList<RegexExplanation>();

        regexMatcher = regexPattern.matcher(regex);

        mainLoop: //
        while (regexMatcher.find()) {
            if (regexMatcher.group() == null || regexMatcher.group().isEmpty()) {
                // found empty string (probably because nothing in regexPattern is mandatory), continue
                continue;
            }

            specialChar = regexMatcher.group(GROUP_SPECIAL_CHARACTERS);
            specialQuantifier = regexMatcher.group(GROUP_SPECIAL_CHARACTERS_QUANTIFIER);
            literals = regexMatcher.group(GROUP_LITERALS);
            quantifierChar = regexMatcher.group(GROUP_QUANTIFIERS_CHARACTER);
            // is already included in quantifierChar!
            quantifierQuantifier = regexMatcher.group(GROUP_QUANTIFIERS_QUANTIFIER);

            /*
             * Special characters matching.
             */
            if (specialChar != null && specialChar.length() >= 2 && specialChar.length() <= 3) {
                RegexExplanation explanation = new RegexExplanation(RegexExplanation.ExplainingType.SPECIAL_CHARACTER,
                        specialChar);
                explanations.add(explanation);

                if (specialQuantifier != null && !specialQuantifier.isEmpty()) {
                    //                    explanation = new RegexExplanation(RegexExplanation.ExplainingType.QUANTIFIER, specialQuantifier);
                    //                    explanations.add(explanation);
                    RegexExplanation quantifierExplanation = new RegexExplanation(
                            RegexExplanation.ExplainingType.QUANTIFIER, specialQuantifier);
                    explanation.setChildExplanation(quantifierExplanation);
                }
            } else {
                Logger.info("No special character found.");
            }

            /*
             * Literals matching.
             */
            if (literals != null && !literals.isEmpty()) {
                RegexExplanation explanation = new RegexExplanation(RegexExplanation.ExplainingType.LITERAL, literals);
                explanations.add(explanation);
            } else {
                Logger.info("No literal found.");
            }

            /*
             * Quantifiers matching.
             */
            if (quantifierChar != null && quantifierChar.length() >= 2 && quantifierChar.length() <= 3) {
                RegexExplanation explanation = new RegexExplanation(RegexExplanation.ExplainingType.LITERAL,
                        quantifierChar);
                explanations.add(explanation);

                RegexExplanation quantifierExplanation = new RegexExplanation(
                        RegexExplanation.ExplainingType.QUANTIFIER, quantifierQuantifier);
                explanation.setChildExplanation(quantifierExplanation);
            } else {
                Logger.info("No quantifier found.");
            }
        }
    }

    /**
     * Return the interval representation of the quantifier. For example, quantifier '+' would return int[] {1, inf}
     * representing that at least 1 and at most infinite number of matches must be found.
     * 
     * @param quantifier
     * @return int[2] - int[0]: minimum number of matches, int[1]: maximum number of matches
     */
    private int[] parseQuantifier(char quantifier) {
        switch (quantifier) {
            case '?':
                return QUANTIFIER_INTERVAL_ZERO_OR_ONE;
            case '*':
                return QUANTIFIER_INTERVAL_ZERO_OR_INF;
            case '+':
                return QUANTIFIER_INTERVAL_ONE_OR_INF;
        }

        throw new RuntimeException("Unknown quantifier: " + quantifier);
    }

    public LinkedList<MatcherResult> getMatches() {
        return matches;
    }

    public LinkedList<RegexExplanation> getExplanations() {
        return explanations;
    }

    public void setProcessPerWord(boolean processPerWord) {
        this.processPerWord = processPerWord;
    }
}
