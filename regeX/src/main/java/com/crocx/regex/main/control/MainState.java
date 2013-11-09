package com.crocx.regex.main.control;

import com.crocx.regex.main.MainAction;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;

/**
 * Created by Croc on 9.11.2013.
 */
public class MainState extends UiState {

    @Override
    public void onAction(UiAction action, Object actionObject) {
        switch ((MainAction) action) {
            case BACK:
                break;

            case BUTTON_INTRODUCTION:
                break;

            case BUTTON_EXAMPLES:
                break;

            case BUTTON_EXERCISES:
                break;

            default:
                throwOnUnknownAction(action, actionObject);
        }
    }
}
