package com.crocx.regex.main.control;

import android.support.v4.app.FragmentTransaction;

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
    private MainFragment mainFragment;

    public MainState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        mainFragment = new MainFragment(mainActivity.getUiStateManager());

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, mainFragment);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onExit(UiState newState, UiAction action, Object actionObject) {
        super.onExit(newState, action, actionObject);

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(mainFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
    }

    @Override
    public void onAction(UiAction action, Object actionObject) {
        switch ((MainAction) action) {
            case BACK:
                mainActivity.getSupportFragmentManager().popBackStack();
                break;

            case BUTTON_INTRODUCTION:
                mainActivity.getUiStateManager().changeState(mainActivity.getIntroductionState());
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
