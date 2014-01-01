package com.crocx.regex.ui;

import com.crocx.regex.util.Logger;

import java.util.LinkedList;

/**
 * Created by Croc on 9.11.2013.
 */
public class UiStateManager {

    private LinkedList<UiState> statesStack = new LinkedList<UiState>();

    private UiState currentState;
    private UiState previousState;

    public void fireAction(UiAction action) {
        Logger.debug("Firing action: " + action.getClass().getSimpleName() + "." + action + " in state: "
                + currentState);

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
            Logger.warning("Not changing state because we are already in the given state: " + newState);
            return;
        }

        Logger.debug("Changing state from: " + currentState + " to: " + newState);

        currentState.onExit(newState, action, actionObject);

        previousState = currentState;
        currentState = newState;
        statesStack.push(currentState);

        currentState.onEnter(previousState, action, actionObject);
    }

    public void start(UiState startState) {
        Logger.debug("Starting UI state manager with state: " + startState);

        previousState = null;
        currentState = startState;
        currentState.onEnter(null, null, null);
        statesStack.push(currentState);
    }

    public void end() {
        Logger.debug("Ending UI state manager.");

        currentState.onExit(null, null, null);
        previousState = null;
        currentState = null;
        resetStatesStack();
    }

    public void goBack() {
        goBack(null);
    }

    public void goBack(UiAction action) {
        goBack(action, null);
    }

    public void goBack(UiAction action, Object actionObject) {
        statesStack.pop();
        changeState(statesStack.peek(), action, actionObject);
        statesStack.pop(); // pop once again, because changeState() puts a new state on the stack
    }

    public void resetStatesStack() {
        statesStack.clear();
    }

    public UiState getCurrentState() {
        return currentState;
    }

    public UiState getPreviousState() {
        return previousState;
    }
}
