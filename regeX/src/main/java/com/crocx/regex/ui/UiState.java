package com.crocx.regex.ui;

import com.crocx.regex.util.Logger;

/**
 * Created by Croc on 9.11.2013.
 */
public abstract class UiState {

    private boolean stateEntered = false;
    /**
     * Set when onEnter() is called.
     */
    private UiState previousState;

    /**
     * Set when onExit() is called.
     */
    private UiState newState;

    abstract public void onAction(UiAction action, Object actionObject);

    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        Logger.verbose("Entering state: " + toString() + ", action: " + action + ", actionObject: " + actionObject);
        stateEntered = true;
        this.previousState = previousState;
    }

    public void onExit(UiState newState, UiAction action, Object actionObject) {
        Logger.verbose("Exiting state: " + toString() + ", action: " + action + ", actionObject: " + actionObject);
        stateEntered = false;
        this.newState = newState;
    }

    public void throwOnUnknownAction(UiAction action, Object actionObject) {
        throw new RuntimeException("Unknown action: " + action.getClass().getSimpleName() + "." + action
                + " in state: " + toString() + ", with actionObject: " + actionObject);
    }

    public boolean isStateEntered() {
        return stateEntered;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public UiState getPreviousState() {
        return previousState;
    }

    public UiState getNewState() {
        return newState;
    }
}
