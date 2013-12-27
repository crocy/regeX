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

    private LinkedList<MatcherResult> matches;
    private LinkedList<RegexExplanation> explanations;

    private boolean processPerWord = true;

    public LinkedList<MatcherResult> processRegexAndInput(String regex, String input) {
        String[] inputArray;

        if (processPerWord) {
            inputArray = input.split(" ");
        } else {
            inputArray = new String[] { input };
        }

        Pattern regexPattern = Pattern.compile("(\\\\\\w([+?*])?)?(\\w+(?![+?*]))?(\\w?([+?*]))?");
        Matcher regexMatcher = regexPattern.matcher(regex);

        String specialChar, specialQuantifier;
        String literal;
        String quantifierChar, quantifierQuantifier;

        MatcherResult match;
        matches = new LinkedList<MatcherResult>();
        explanations = new LinkedList<RegexExplanation>();

        String processInput;
        int offset = 0;
        int regionStart, regionEnd;

        forLoop: //
        for (int i = 0; i < inputArray.length; i++) {
            processInput = inputArray[i];
            regexMatcher.reset();

            //            regionStart = 0;
            regionEnd = processInput.length();

            mainLoop: //
            while (regexMatcher.find()) {
                if (regexMatcher.group() == null || regexMatcher.group().isEmpty()) {
                    // found empty string (probably because nothing in regexPattern is mandatory), continue
                    continue;
                }
                regionStart = regexMatcher.start();

                specialChar = regexMatcher.group(GROUP_SPECIAL_CHARACTERS);
                specialQuantifier = regexMatcher.group(GROUP_SPECIAL_CHARACTERS_QUANTIFIER);
                literal = regexMatcher.group(GROUP_LITERALS);
                quantifierChar = regexMatcher.group(GROUP_QUANTIFIERS_CHARACTER);
                // is already included in quantifierChar!
                quantifierQuantifier = regexMatcher.group(GROUP_QUANTIFIERS_QUANTIFIER);

                /*
                 * Special characters matching.
                 */
                if (specialChar != null && specialChar.length() >= 2 && specialChar.length() <= 3) {
                    RegexExplanation explanation = new RegexExplanation(
                            RegexExplanation.ExplainingType.SPECIAL_CHARACTER, specialChar);
                    explanations.add(explanation);

                    if (specialQuantifier != null && !specialQuantifier.isEmpty()) {
                        explanation = new RegexExplanation(RegexExplanation.ExplainingType.QUANTIFIER,
                                specialQuantifier);
                        explanations.add(explanation);
                    }

                    Pattern specialCharPattern = Pattern.compile(specialChar);
                    Matcher specialCharMatcher = specialCharPattern.matcher(processInput);
                    specialCharMatcher.region(regionStart, regionEnd);

                    if (specialCharMatcher.find()) {
                        match = new MatcherResult();
                        match.setMatch(specialCharMatcher.group(), offset + specialCharMatcher.start(), offset
                                + specialCharMatcher.end());
                        match.setPattern(specialCharMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.end(GROUP_SPECIAL_CHARACTERS));
                        match.setExplanation(explanation);
                        matches.add(match);
                    }
                } else {
                    Logger.info("No special character found.");
                    specialChar = "";
                }

                /*
                 * Literals matching.
                 */
                if (literal != null && !literal.isEmpty()) {
                    RegexExplanation explanation = new RegexExplanation(RegexExplanation.ExplainingType.LITERAL,
                            literal);
                    explanations.add(explanation);

                    Pattern literalPattern;
                    Matcher literalMatcher;
                    StringBuffer subLiteral = new StringBuffer();

                    for (int j = 0; j < literal.length(); j++) {
                        subLiteral.append(literal.charAt(j));

                        literalPattern = Pattern.compile(specialChar + subLiteral.toString());
                        literalMatcher = literalPattern.matcher(processInput);
                        literalMatcher.region(regionStart, regionEnd);

                        match = new MatcherResult();
                        match.setPattern(literalMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.start(GROUP_LITERALS) + subLiteral.length());
                        match.setExplanation(explanation);
                        matches.add(match);

                        // literal matcher should always find exactly one match per query
                        if (!literalMatcher.find()) {
                            match.setType(MatcherResult.ResultType.MISMATCH);
                            match.setMatch(null, offset, offset + literalMatcher.regionEnd());

                            break mainLoop;
                        }

                        match.setMatch(literalMatcher.group(), offset + literalMatcher.start(),
                                offset + literalMatcher.end());
                    }
                } else {
                    Logger.info("No literal found.");
                    literal = "";
                }

                /*
                 * Quantifiers matching.
                 */
                if (quantifierChar != null && quantifierChar.length() >= 2 && quantifierChar.length() <= 3) {
                    RegexExplanation explanation = new RegexExplanation(RegexExplanation.ExplainingType.LITERAL,
                            quantifierChar);
                    explanations.add(explanation);

                    RegexExplanation quantifierExplanation = null;
                    //                    if (quantifierQuantifier != null && !quantifierQuantifier.isEmpty()) {
                    quantifierExplanation = new RegexExplanation(RegexExplanation.ExplainingType.QUANTIFIER,
                            quantifierQuantifier);
                    //                        explanations.add(explanation);
                    explanation.setChildExplanation(quantifierExplanation);
                    //                    }

                    Pattern quantifiersPattern = Pattern.compile(specialChar + literal + quantifierChar);
                    Matcher quantifiersMatcher = quantifiersPattern.matcher(processInput);
                    quantifiersMatcher.region(regionStart, regionEnd);

                    int quantifiersFound = 0;
                    while (quantifiersMatcher.find()) {
                        //                        // if no match has been found and at least one is expected, continue
                        //                        if (quantifiersMatcher.group() == null || quantifiersMatcher.group().isEmpty()) {
                        //                            continue;
                        //                        }
                        quantifiersFound++;

                        match = new MatcherResult();
                        match.setMatch(quantifiersMatcher.group(), offset + quantifiersMatcher.start(), offset
                                + quantifiersMatcher.end());
                        match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.end(GROUP_QUANTIFIERS_CHARACTER));

                        match.setExplanation(explanation);
                        matches.add(match);
                    }

                    int[] quantifierInterval = parseQuantifier(quantifierQuantifier.charAt(0));
                    // check if there are at least the minimum and at most maximum number of matches found
                    if (quantifiersFound < quantifierInterval[0] || quantifiersFound > quantifierInterval[1]) {
                        match = new MatcherResult();
                        match.setType(MatcherResult.ResultType.MISMATCH);
                        //                        match.setMatch(quantifiersMatcher.group(), offset + quantifiersMatcher.start(), offset
                        //                                + quantifiersMatcher.end());
                        match.setMatch(null, offset, offset + quantifiersMatcher.regionEnd());
                        match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.end(GROUP_QUANTIFIERS_CHARACTER));

                        match.setExplanation(explanation);
                        //                        match.setExplanation(quantifierExplanation);
                        matches.add(match);
                    }

                } else {
                    Logger.info("No quantifier found.");
                }
            }

            offset += processInput.length() + 1; // +1 for the space ' '
        }

        return matches;
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
}
