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

        LinkedList<MatcherResult> matches = new LinkedList<MatcherResult>();

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

            Pattern literalPattern = Pattern.compile(literal);
            Matcher literalMatcher = literalPattern.matcher(input);

            // literal matcher should always find exactly one match per query
            if (!literalMatcher.find()) {
                continue;
            }

            Pattern quantifiersPattern = Pattern.compile(literal + quantifierChar);
            Matcher quantifiersMatcher = quantifiersPattern.matcher(input);
            quantifiersMatcher.region(literalMatcher.start(), input.length());

            //            // if no match has been found and at least one is expected, continue
            //            if (!quantifiersMatcher.find() && quantifierQuantifier.equals("+")) {
            //                continue;
            //            }
            while (quantifiersMatcher.find()) {
                if (quantifiersMatcher.group() == null || quantifiersMatcher.group().isEmpty()) {
                    continue;
                }

                MatcherResult result = new MatcherResult();
                result.setMatch(quantifiersMatcher.group(), quantifiersMatcher.start(), quantifiersMatcher.end());
                result.setPattern(quantifiersMatcher.pattern().pattern(), regexMatcher.start(), regexMatcher.end());

                matches.add(result);
            }
        }

        return matches;
    }
}
