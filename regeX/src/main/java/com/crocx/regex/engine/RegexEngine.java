package com.crocx.regex.engine;

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

    public LinkedList<MatcherResult> processRegexAndInput(String regex, String input) {
        Pattern regexPattern = Pattern.compile("(\\\\\\w([+?*])?)?(\\w+(?![+?*]))?(\\w?([+?*]))?");
        Matcher regexMatcher = regexPattern.matcher(regex);
        //        int regionStart = 0;

        //        String specialChar;
        String literal;
        String quantifierChar, quantifierQuantifier;

        MatcherResult match;
        LinkedList<MatcherResult> matches = new LinkedList<MatcherResult>();

        mainLoop: //
        while (regexMatcher.find()) {
            if (regexMatcher.group() == null || regexMatcher.group().isEmpty()) {
                // found empty string (probably because nothing in regexPattern is mandatory), continue
                continue;
            }
            //            specialChar = regexMatcher.group(GROUP_SPECIAL_CHARACTERS);
            literal = regexMatcher.group(GROUP_LITERALS);

            quantifierChar = regexMatcher.group(GROUP_QUANTIFIERS_CHARACTER);
            // is already included in quantifierChar!
            quantifierQuantifier = regexMatcher.group(GROUP_QUANTIFIERS_QUANTIFIER);

            /*
             * Special characters matching.
             */
            // TODO

            /*
             * Literals matching.
             */
            if (literal == null || literal.isEmpty()) {
                continue;
            }

            Pattern literalPattern;
            Matcher literalMatcher = null;
            StringBuffer subLiteral = new StringBuffer();
            for (int i = 0; i < literal.length(); i++) {
                subLiteral.append(literal.charAt(i));

                //                literalPattern = Pattern.compile(literal);
                literalPattern = Pattern.compile(subLiteral.toString());
                literalMatcher = literalPattern.matcher(input);

                // literal matcher should always find exactly one match per query
                if (!literalMatcher.find()) {
                    match = new MatcherResult();
                    match.setType(MatcherResult.ResultType.MISMATCH);
                    matches.add(match);
                    continue;
                }

                match = new MatcherResult();
                match.setMatch(literalMatcher.group(), literalMatcher.start(), literalMatcher.end());
                //                match.setPattern(literalMatcher.pattern().pattern(), regexMatcher.start(GROUP_LITERALS),
                //                        regexMatcher.end(GROUP_LITERALS));
                match.setPattern(literalMatcher.pattern().pattern(), regexMatcher.start(GROUP_LITERALS),
                        regexMatcher.start(GROUP_LITERALS) + subLiteral.length());
                matches.add(match);
            }

            /*
             * Quantifiers matching.
             */
            Pattern quantifiersPattern = Pattern.compile(literal + quantifierChar);
            Matcher quantifiersMatcher = quantifiersPattern.matcher(input);
            quantifiersMatcher.region(literalMatcher.start(), input.length());

            while (quantifiersMatcher.find()) {
                // if no match has been found and at least one is expected, continue
                if (quantifiersMatcher.group() == null || quantifiersMatcher.group().isEmpty()) {
                    continue;
                }

                match = new MatcherResult();
                match.setMatch(quantifiersMatcher.group(), quantifiersMatcher.start(), quantifiersMatcher.end());
                match.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(GROUP_LITERALS),
                        regexMatcher.end(GROUP_QUANTIFIERS_CHARACTER));

                matches.add(match);
            }
        }

        return matches;
    }

}
