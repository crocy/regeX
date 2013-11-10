package com.crocx.regex.main.control;

import android.support.v4.app.FragmentTransaction;

import com.crocx.regex.CommonAction;
import com.crocx.regex.MainActivity;
import com.crocx.regex.R;
import com.crocx.regex.main.MainAction;
import com.crocx.regex.main.MainFragment;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;

/**
 * Created by Croc on 9.11.2013.
 */
public class MainState extends UiState {

    private MainActivity mainActivity;
    private MainFragment fragment;

    public MainState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        fragment = new MainFragment();
        fragment.setUiStateManager(mainActivity.getUiStateManager());

        // only add this fragment at app start (no previous state)
        if (previousState == null) {
            FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onAction(UiAction action, Object actionObject) {
        if (action instanceof CommonAction) {
            switch ((CommonAction) action) {
                case BACK:
                    mainActivity.getUiStateManager().end();
                    break;
            }
        } else {
            switch ((MainAction) action) {
                case BUTTON_INTRODUCTION:
                    mainActivity.getUiStateManager().changeState(mainActivity.getIntroductionState());
                    break;

                case BUTTON_EXAMPLES:
                    break;

                case BUTTON_EXERCISES:
                    mainActivity.getUiStateManager().changeState(mainActivity.getExercisesState());
                    break;

                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }
}
