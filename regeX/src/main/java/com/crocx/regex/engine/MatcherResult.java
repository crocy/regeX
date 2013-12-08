package com.crocx.regex.engine;

/**
 * Created by Croc on 2.12.2013.
 */
public class MatcherResult {

    private ResultType type = null;

    private String match;
    private int matchStart;
    private int matchEnd;

    private String pattern;
    private int patternStart;
    private int patternEnd;

    private RegexExplanation explanation;

    public enum ResultType {
        MATCH, MISMATCH
    }

    public void setMatch(String match, int matchStart, int matchEnd) {
        this.match = match;
        this.matchStart = matchStart;
        this.matchEnd = matchEnd;
    }

    public void setPattern(String pattern, int patternStart, int patternEnd) {
        this.pattern = pattern;
        this.patternStart = patternStart;
        this.patternEnd = patternEnd;
    }

    public String getMatch() {
        return match;
    }

    public int getMatchStart() {
        return matchStart;
    }

    public int getMatchEnd() {
        return matchEnd;
    }

    public String getPattern() {
        return pattern;
    }

    public int getPatternStart() {
        return patternStart;
    }

    public int getPatternEnd() {
        return patternEnd;
    }

    public ResultType getType() {
        return type;
    }

    public void setType(ResultType type) {
        this.type = type;
    }

    public RegexExplanation getExplanation() {
        return explanation;
    }

    public void setExplanation(RegexExplanation explanation) {
        this.explanation = explanation;
    }
}
