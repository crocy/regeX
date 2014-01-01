package com.crocx.regex.exercises.control;

import android.support.v4.app.FragmentTransaction;

import com.crocx.regex.CommonAction;
import com.crocx.regex.MainActivity;
import com.crocx.regex.R;
import com.crocx.regex.exercises.ExercisesAction;
import com.crocx.regex.exercises.fragment.ExercisesFragment;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExercisesState extends UiState {

    private MainActivity mainActivity;
    private ExercisesFragment fragment;

    public ExercisesState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        fragment = new ExercisesFragment();
        fragment.setUiStateManager(mainActivity.getUiStateManager());

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onAction(UiAction action, Object actionObject) {
        if (action instanceof CommonAction) {
            switch ((CommonAction) action) {
                case BACK:
                    mainActivity.getUiStateManager().goBack(CommonAction.BACK);
                    break;
            }
        } else if (action instanceof ExercisesAction) {
            switch ((ExercisesAction) action) {
                case EXERCISE_ITEM_CLICKED:
                    mainActivity.getUiStateManager().changeState(mainActivity.getExerciseState(),
                            ExercisesAction.EXERCISE_ITEM_CLICKED, actionObject);
                    break;

                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }

}
