package com.crocx.regex.ui;

import com.crocx.regex.util.Logger;

/**
 * Created by Croc on 9.11.2013.
 */
public abstract class UiState {

    private boolean stateEntered = false;

    abstract public void onAction(UiAction action, Object actionObject);

    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        Logger.verbose("Entering state: " + toString() + ", action: " + action + ", actionObject: " + actionObject
                + ", previousState: " + previousState);
        stateEntered = true;
    }

    public void onExit(UiState newState, UiAction action, Object actionObject) {
        Logger.verbose("Exiting state: " + toString() + ", action: " + action + ", actionObject: " + actionObject
                + ", newState: " + newState);
        stateEntered = false;
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

}
