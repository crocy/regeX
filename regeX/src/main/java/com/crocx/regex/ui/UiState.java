package com.crocx.regex.ui;

import com.crocx.regex.CommonAction;
import com.crocx.regex.util.Logger;

/**
 * Created by Croc on 9.11.2013.
 */
public abstract class UiState {

    private boolean stateEntered = false;
    /**
     * Save previousState on any action if this (savePreviousStateOnBackAction) is set to true. Else, save previousState
     * <b>only</b> if current onEnter() <b>action</b> is not a "back action".
     */
    private boolean savePreviousStateOnBackAction = false;
    private UiAction backAction = CommonAction.BACK;

    /**
     * Set when onEnter() is called.
     */
    protected UiState previousState;

    /**
     * Set when onExit() is called.
     */
    protected UiState newState;

    abstract public void onAction(UiAction action, Object actionObject);

    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        Logger.verbose("Entering state: " + toString() + ", action: " + action + ", actionObject: " + actionObject
                + ", previousState: " + previousState);
        stateEntered = true;

        /*
         * Save previousState on any action if this (savePreviousStateOnBackAction) is set to true. Else, save
         * previousState *only* if current onEnter() action is not a "back action".
         */
        if (savePreviousStateOnBackAction || (action != null && action != backAction)) {
            this.previousState = previousState;
        }
    }

    public void onExit(UiState newState, UiAction action, Object actionObject) {
        Logger.verbose("Exiting state: " + toString() + ", action: " + action + ", actionObject: " + actionObject
                + ", newState: " + newState);
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

    public boolean isSavePreviousStateOnBackAction() {
        return savePreviousStateOnBackAction;
    }

    public void setSavePreviousStateOnBackAction(boolean savePreviousStateOnBackAction) {
        this.savePreviousStateOnBackAction = savePreviousStateOnBackAction;
    }

    public UiAction getBackAction() {
        return backAction;
    }

    public void setBackAction(UiAction backAction) {
        this.backAction = backAction;
    }
}
