package com.crocx.regex.ui;

/**
 * Created by Croc on 9.11.2013.
 */
public abstract class UiState {

    private boolean stateEntered = false;

    abstract public void onAction(UiAction action, Object actionObject);

    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        stateEntered = true;
    }

    public void onExit(UiState newState, UiAction action, Object actionObject) {
        stateEntered = false;
    }

    public void throwOnUnknownAction(UiAction action, Object actionObject) {
        throw new RuntimeException("Unknown action: " + action + " in state: " + getClass().getSimpleName()
                + ", with actionObject: " + actionObject);
    }

    public boolean isStateEntered() {
        return stateEntered;
    }
}
