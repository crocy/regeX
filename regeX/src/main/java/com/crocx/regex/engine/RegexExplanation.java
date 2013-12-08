package com.crocx.regex.engine;

/**
 * Created by Croc on 8.12.2013.
 */
public class RegexExplanation {

    private ExplainingType explainingType;
    private String explainingRegex;
    private String explanationMessage;
    private boolean emphasise = false;

    public enum ExplainingType {
        SPECIAL_CHARACTER, LITERAL, QUANTIFIER
    }

    public RegexExplanation() {}

    public RegexExplanation(ExplainingType explainingType, String explainingRegex) {
        this.explainingType = explainingType;
        this.explainingRegex = explainingRegex;

        explanationMessage = explainingType + ": " + explainingRegex;
    }

    public ExplainingType getExplainingType() {
        return explainingType;
    }

    public void setExplainingType(ExplainingType explainingType) {
        this.explainingType = explainingType;
    }

    public String getExplainingRegex() {
        return explainingRegex;
    }

    public void setExplainingRegex(String explainingRegex) {
        this.explainingRegex = explainingRegex;
    }

    public String getExplanationMessage() {
        return explanationMessage;
    }

    public void setExplanationMessage(String explanationMessage) {
        this.explanationMessage = explanationMessage;
    }

    public boolean isEmphasise() {
        return emphasise;
    }

    public void setEmphasise(boolean emphasise) {
        this.emphasise = emphasise;
    }
}
