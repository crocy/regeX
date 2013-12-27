package com.crocx.regex.engine;

/**
 * Created by Croc on 8.12.2013.
 */
public class RegexExplanation {

    private ExplainingType explainingType;
    private String explainingRegex;
    private String explanationMessage;
    private EmphasiseType emphasiseType = EmphasiseType.NONE;
    private RegexExplanation childExplanation;
    /**
     * Level of explanation (if it's embedded in another explanation, this explanation's level goes up by 1).
     */
    private int level = 0;

    public enum ExplainingType {
        SPECIAL_CHARACTER("Special character"), LITERAL("Literal"), QUANTIFIER("Quantifier"), GROUP("Group");

        private String description;

        private ExplainingType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum EmphasiseType {
        NONE, EMPHASISE_MATCH, EMPHASISE_MISMATCH
    }

    public RegexExplanation() {}

    public RegexExplanation(ExplainingType explainingType, String explainingRegex) {
        this.explainingType = explainingType;
        this.explainingRegex = explainingRegex;

        generateExplanationMessage();
    }

    public void generateExplanationMessage() {
        explanationMessage = explainingType.getDescription() + ": '";

        switch (explainingType) {
            case SPECIAL_CHARACTER:
                // TODO: the "charAt(1)" approach is not good but it'll have to do for now
                explanationMessage += explainingRegex + "' matches "
                        + getSpecialCharExplanation(explainingRegex.charAt(1));
                break;

            case LITERAL:
                explanationMessage += explainingRegex + "' matches character(s) '" + explainingRegex
                        + "' literally (case sensitive)";
                break;

            case QUANTIFIER:
                // TODO: the "charAt(0)" approach is not good but it'll have to do for now
                explanationMessage += explainingRegex + "' means match must occur "
                        + getQuantifierExplanation(explainingRegex.charAt(0));
        }
    }

    /**
     * <ul>
     * <li>\s Any whitespace character
     * <li>\S Any non-whitespace character
     * <li>\d Any digit
     * <li>\D Any non-digit
     * <li>\w Any word character
     * <li>\W Any non-word character
     * <li>\b A word boundary
     * <li>\B Non-word boundary
     * </ul>
     * 
     * @param specialChar like "\w" or even "\w+"
     * @return String explanation
     */
    public String getSpecialCharExplanation(char specialChar) {
        switch (specialChar) {
            case 'd':
                return "any digit";
            case 'D':
                return "any non-digit";
            case 's':
                return "any whitespace character";
            case 'S':
                return "any non-whitespace character";
            case 'w':
                return "any word character: [a-zA-Z0-9_]";
            case 'W':
                return "any non-word character: [^a-zA-Z0-9_]";
        }

        return null;
    }

    public String getQuantifierExplanation(char quantifier) {
        switch (quantifier) {
            case '?':
                return "between zero and one time";
            case '*':
                return "between zero and unlimited times";
            case '+':
                return "between one and unlimited times";
        }

        return null;
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

    public EmphasiseType getEmphasiseType() {
        return emphasiseType;
    }

    public void setEmphasiseType(EmphasiseType emphasiseType) {
        this.emphasiseType = emphasiseType;
    }

    public RegexExplanation getChildExplanation() {
        return childExplanation;
    }

    public void setChildExplanation(RegexExplanation childExplanation) {
        //        this.childExplanation = childExplanation;
        setChildExplanation(childExplanation, true);
    }

    public void setChildExplanation(RegexExplanation childExplanation, boolean incChildLevel) {
        this.childExplanation = childExplanation;

        if (incChildLevel) {
            childExplanation.level = level + 1;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
