package com.crocx.regex.tutorial;

import java.util.LinkedList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Croc on 30.11.2013.
 */
public class RegexEngine {

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
            specialCharacter = regexMatcher.group(1);
            literal = regexMatcher.group(2);
            quantifier = regexMatcher.group(3);

            Pattern inputPattern = Pattern.compile(literal);
            Matcher inputMatcher = inputPattern.matcher(input);

            while (inputMatcher.find()) {
                //                regionStart = inputMatcher.start();
                matches.add(inputMatcher.toMatchResult());
            }
        }

        return matches;
    }
}
