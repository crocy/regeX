package com.crocx.regex.ui;

/**
 * Created by Croc on 9.11.2013.
 */
public class UiStateManager {

    private UiState currentState;
    private UiState previousState;

    public void fireAction(UiAction action) {
        fireAction(action, null);
    }

    public void fireAction(UiAction action, Object actionObject) {
        currentState.onAction(action, actionObject);
    }

    public void changeState(UiState newState) {
        changeState(newState, null);
    }

    public void changeState(UiState newState, UiAction action) {
        changeState(newState, action, null);
    }

    public void changeState(UiState newState, UiAction action, Object actionObject) {
        if (newState == currentState) {
            return;
        }

        UiState tempPreviousState = previousState;

        currentState.onExit(newState, action, actionObject);

        previousState = currentState;
        currentState = newState;

        currentState.onEnter(tempPreviousState, action, actionObject);
    }

    public void start(UiState startState) {
        previousState = null;
        currentState = startState;
        currentState.onEnter(null, null, null);
    }

    public void end() {
        currentState.onExit(null, null, null);
        previousState = null;
    }

    public UiState getCurrentState() {
        return currentState;
    }

    public UiState getPreviousState() {
        return previousState;
    }
}
