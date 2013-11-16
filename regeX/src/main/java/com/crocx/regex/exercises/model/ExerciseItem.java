package com.crocx.regex.exercises.model;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseItem {

    private static final String DEFAULT_INSTRUCTIONS_FILE = "instructions.html";
    private static final String DEFAULT_DATA_FILE = "data.txt";
    private static final String DEFAULT_SOLUTION_OUTPUT_FILE = "solution-output.txt";
    private static final String DEFAULT_SOLUTION_REGEX_FILE = "solution-regex.txt";

    private int id;
    private String assetsUrl;
    private String name;
    private String instructionsUrl;
    private String data;
    private String solutionRegex;
    private String solutionOutput;
    private boolean preferSolutionOutput = true;

    public ExerciseItem(int id) {
        this.id = id;
    }

    public void loadContentFromAssetsUrl(String assetsUrl, AssetManager assetManager) {
        this.assetsUrl = assetsUrl;
        String assetsURL = assetsUrl + "/";
        instructionsUrl = assetsURL + DEFAULT_INSTRUCTIONS_FILE;

        data = loadAssetAsString(assetsURL + DEFAULT_DATA_FILE, assetManager);
        solutionOutput = loadAssetAsString(assetsURL + DEFAULT_SOLUTION_OUTPUT_FILE, assetManager);
        solutionRegex = loadAssetAsString(assetsURL + DEFAULT_SOLUTION_REGEX_FILE, assetManager);
    }

    private String loadAssetAsString(String assetUrl, AssetManager assetManager) {
        StringBuffer stringBuffer = new StringBuffer();
        String tempString;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(assetUrl)));

            tempString = reader.readLine();
            while (tempString != null) {
                stringBuffer.append(tempString);
                tempString = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return stringBuffer.toString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructionsUrl() {
        return instructionsUrl;
    }

    public void setInstructionsUrl(String instructionsUrl) {
        this.instructionsUrl = instructionsUrl;
    }

    public String getSolutionRegex() {
        return solutionRegex;
    }

    public void setSolutionRegex(String solutionRegex) {
        this.solutionRegex = solutionRegex;
    }

    public String getSolutionOutput() {
        return solutionOutput;
    }

    public void setSolutionOutput(String solutionOutput) {
        this.solutionOutput = solutionOutput;
    }

    public boolean isPreferSolutionOutput() {
        return preferSolutionOutput;
    }

    public void setPreferSolutionOutput(boolean preferSolutionOutput) {
        this.preferSolutionOutput = preferSolutionOutput;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAssetsUrl() {
        return assetsUrl;
    }
}
