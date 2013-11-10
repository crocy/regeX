package com.crocx.regex.exercises.model;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseItem {

    private int exerciseId;
    private String exerciseName;
    private String content;
    private String solution;

    public ExerciseItem(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    //    public void setExerciseId(int exerciseId) {
    //        this.exerciseId = exerciseId;
    //    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
