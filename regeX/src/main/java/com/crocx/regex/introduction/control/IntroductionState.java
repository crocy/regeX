package com.crocx.regex.introduction.control;

import android.support.v4.app.FragmentTransaction;

import com.crocx.regex.MainActivity;
import com.crocx.regex.R;
import com.crocx.regex.introduction.IntroductionAction;
import com.crocx.regex.introduction.IntroductionFragment;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;

/**
 * Created by Croc on 9.11.2013.
 */
public class IntroductionState extends UiState {

    private MainActivity mainActivity;
    private IntroductionFragment introductionFragment;

    public IntroductionState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        introductionFragment = new IntroductionFragment(mainActivity.getUiStateManager());
        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, introductionFragment);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onExit(UiState newState, UiAction action, Object actionObject) {
        super.onExit(newState, action, actionObject);

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(introductionFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
    }

    @Override
    public void onAction(UiAction action, Object actionObject) {
        switch ((IntroductionAction) action) {
            case BACK:
                mainActivity.getSupportFragmentManager().popBackStack();
                mainActivity.getUiStateManager().changeState(mainActivity.getMainState());
                break;

            default:
                throwOnUnknownAction(action, actionObject);
        }
    }

}
