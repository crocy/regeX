package com.crocx.regex.tutorial;

import java.util.LinkedList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Croc on 30.11.2013.
 */
public class RegexEngine {

    public static final int GROUP_SPECIAL_CHARACTERS = 1;
    public static final int GROUP_LITERALS = 2;
    public static final int GROUP_QUANTIFIERS = 3;

    //    (?:\\\w)?(\w+)?(?![+?*]) -- 1 group: literals
    //    (\\\w)?(\w+(?![+?*]))?(\w[+?*])? -- 3 groups: \chars; literals; quantifiers

    public LinkedList<MatchResult> processRegexAndInput(String regex, String input) {
        Pattern regexPattern = Pattern.compile("(\\\\w)?(\\w+(?![+?*]))?(\\w[+?*])?");
        Matcher regexMatcher = regexPattern.matcher(regex);
        int regionStart = 0;

        String specialCharacter;
        String literal;
        String quantifier;

        LinkedList<MatchResult> matches = new LinkedList<MatchResult>();

        while (regexMatcher.find()) {
            specialCharacter = regexMatcher.group(GROUP_SPECIAL_CHARACTERS);
            literal = regexMatcher.group(GROUP_LITERALS);
            quantifier = regexMatcher.group(GROUP_QUANTIFIERS);

            Pattern inputPattern;
            try {
                inputPattern = Pattern.compile(literal);
            } catch (Exception e) {
                continue;
            }

            Matcher inputMatcher = inputPattern.matcher(input);

            while (inputMatcher.find()) {
                //                regionStart = inputMatcher.start();
                matches.add(inputMatcher.toMatchResult());
            }
        }

        return matches;
    }
}
