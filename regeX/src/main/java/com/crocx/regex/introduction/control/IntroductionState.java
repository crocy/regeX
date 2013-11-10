package com.crocx.regex.introduction.control;

import android.support.v4.app.FragmentTransaction;

import com.crocx.regex.CommonAction;
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
    private IntroductionFragment fragment;

    public IntroductionState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        //        fragment = new IntroductionFragment(mainActivity.getUiStateManager());
        fragment = new IntroductionFragment();
        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAction(UiAction action, Object actionObject) {
        if (action instanceof CommonAction) {
            switch ((CommonAction) action) {
                case BACK:
                    mainActivity.getUiStateManager().changeState(mainActivity.getMainState());
                    break;
            }
        } else {
            switch ((IntroductionAction) action) {
                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }

}
