package com.crocx.regex.tutorial.control;

import android.support.v4.app.FragmentTransaction;

import com.crocx.regex.CommonAction;
import com.crocx.regex.MainActivity;
import com.crocx.regex.R;
import com.crocx.regex.tutorial.TutorialAction;
import com.crocx.regex.tutorial.TutorialFragment;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;

/**
 * Created by Croc on 24.11.2013.
 */
public class TutorialState extends UiState {

    private MainActivity mainActivity;
    private TutorialFragment fragment;

    public TutorialState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        fragment = new TutorialFragment();
        fragment.setUiStateManager(mainActivity.getUiStateManager());

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
            switch ((TutorialAction) action) {
                case START:
                    fragment.getTutorialView().updateView("foo+", "aa fo foo fooo foooo bar");
                    break;

                case BUTTON_NEXT:
                    fragment.getTutorialView().nextStep();
                    break;

                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }

}
