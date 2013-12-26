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
        //        regionStart = 0;
        //            regionEnd = processInput.length();
        //        regionEnd = input.length();

        forLoop: //
        for (int i = 0; i < inputArray.length; i++) {
            processInput = inputArray[i];
            regexMatcher.reset();

            regionStart = 0;
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
                        //                        match.setMatch(specialCharMatcher.group(), specialCharMatcher.start(), specialCharMatcher.end());
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

                        //                literalPattern = Pattern.compile(literal);
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
                            //                            match.setMatch(null, offset, offset);
                            match.setMatch(null, offset, offset + literalMatcher.regionEnd());
                            //                            matches.add(match);

                            //                            continue;
                            //                            continue mainLoop;
                            //                            continue forLoop;
                            break mainLoop;
                        }

                        //                        match = new MatcherResult();
                        //                        match.setMatch(literalMatcher.group(), literalMatcher.start(), literalMatcher.end());
                        match.setMatch(literalMatcher.group(), offset + literalMatcher.start(),
                                offset + literalMatcher.end());
                        //                match.setPattern(literalMatcher.pattern().pattern(), regexMatcher.start(GROUP_LITERALS),
                        //                        regexMatcher.end(GROUP_LITERALS));
                        //                        match.setPattern(literalMatcher.pattern().pattern(), regexMatcher.start(),
                        //                                regexMatcher.start(GROUP_LITERALS) + subLiteral.length());
                    }
                } else {
                    Logger.info("No literal found.");
                    literal = "";
                }

                /*
                 * Quantifiers matching.
                 */
                if (quantifierChar != null && quantifierChar.length() >= 2 && quantifierChar.length() <= 3) {
                    RegexExplanation explanation = new RegexExplanation(
                            RegexExplanation.ExplainingType.SPECIAL_CHARACTER, quantifierChar);
                    explanations.add(explanation);

                    if (quantifierQuantifier != null || !quantifierQuantifier.isEmpty()) {
                        explanation = new RegexExplanation(RegexExplanation.ExplainingType.QUANTIFIER,
                                quantifierQuantifier);
                        explanations.add(explanation);
                    }

                    Pattern quantifiersPattern = Pattern.compile(specialChar + literal + quantifierChar);
                    Matcher quantifiersMatcher = quantifiersPattern.matcher(processInput);
                    //            quantifiersMatcher.region(literalMatcher.start(), input.length());
                    quantifiersMatcher.region(regionStart, regionEnd);

                    while (quantifiersMatcher.find()) {
                        // if no match has been found and at least one is expected, continue
                        if (quantifiersMatcher.group() == null || quantifiersMatcher.group().isEmpty()) {
                            continue;
                        }

                        match = new MatcherResult();
                        //                        match.setMatch(quantifiersMatcher.group(), quantifiersMatcher.start(), quantifiersMatcher.end());
                        match.setMatch(quantifiersMatcher.group(), offset + quantifiersMatcher.start(), offset
                                + quantifiersMatcher.end());
                        match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(),
                                regexMatcher.end(GROUP_QUANTIFIERS_CHARACTER));

                        match.setExplanation(explanation);
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

    public LinkedList<MatcherResult> getMatches() {
        return matches;
    }

    public LinkedList<RegexExplanation> getExplanations() {
        return explanations;
    }
}
