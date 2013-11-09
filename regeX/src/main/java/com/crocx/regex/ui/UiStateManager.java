package com.crocx.regex.ui;

import com.crocx.regex.util.Logger;

/**
 * Created by Croc on 9.11.2013.
 */
public class UiStateManager {

    private UiState currentState;
    private UiState previousState;

    public void fireAction(UiAction action) {
        Logger.debug("Firing action: " + action + " in state: "
                + (currentState != null ? currentState.getClass().getSimpleName() : null));

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

        Logger.debug("Changing state from: "
                + (previousState != null ? previousState.getClass().getSimpleName() : null) + " to: "
                + (currentState != null ? currentState.getClass().getSimpleName() : null));

        UiState tempPreviousState = previousState;

        currentState.onExit(newState, action, actionObject);

        previousState = currentState;
        currentState = newState;

        currentState.onEnter(tempPreviousState, action, actionObject);
    }

    public void start(UiState startState) {
        Logger.debug("Starting UI state manager with state: " + startState.getClass().getSimpleName());

        previousState = null;
        currentState = startState;
        currentState.onEnter(null, null, null);
    }

    public void end() {
        Logger.debug("Ending UI state manager.");

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
