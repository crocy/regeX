package com.crocx.regex.exercises.control;

import android.support.v4.app.FragmentTransaction;
import android.util.Pair;

import com.crocx.regex.CommonAction;
import com.crocx.regex.MainActivity;
import com.crocx.regex.R;
import com.crocx.regex.exercises.ExercisesAction;
import com.crocx.regex.exercises.fragment.ExerciseFragment;
import com.crocx.regex.exercises.fragment.ExercisesFragment;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.exercises.view.ExerciseView;
import com.crocx.regex.tutorial.TutorialAction;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;
import com.crocx.regex.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExercisesState extends UiState {

    private MainActivity mainActivity;
    private ExercisesFragment fragment;
    private ExerciseFragment exerciseFragment;

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
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onExit(UiState newState, UiAction action, Object actionObject) {
        super.onExit(newState, action, actionObject);

        Util.hideSoftKeyboard(mainActivity, exerciseFragment.getExerciseView());
    }

    @Override
    public void onAction(UiAction action, Object actionObject) {
        if (action instanceof CommonAction) {
            switch ((CommonAction) action) {
                case BACK:
                    // if no exercise (fragment) currently shown, go back to main menu
                    if (mainActivity.getSupportFragmentManager().findFragmentByTag(ExerciseFragment.FRAGMENT_TAG) == null) {
                        mainActivity.getUiStateManager().changeState(mainActivity.getMainState());
                    }
                    //                    mainActivity.getUiStateManager().changeState(getPreviousState());
                    break;
            }
        } else if (action instanceof ExercisesAction) {
            switch ((ExercisesAction) action) {
                case EXERCISE_ITEM_CLICKED:
                    exerciseClicked((ExerciseItem) actionObject);
                    break;

                case EVALUATE_REGEX:
                    evaluateRegex((String) actionObject);
                    break;

                case EXPLAIN_REGEX:
                    String regex = exerciseFragment.getExerciseView().getRegex();
                    String input = exerciseFragment.getExerciseItem().getData();

                    Pair<String, String> pair = new Pair<String, String>(regex, input);
                    mainActivity.getUiStateManager().changeState(mainActivity.getTutorialState(),
                            TutorialAction.EXPLAIN_REGEX, pair);
                    break;

                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }

    private void exerciseClicked(ExerciseItem exerciseItem) {
        if (exerciseFragment == null) {
            exerciseFragment = new ExerciseFragment();
            exerciseFragment.setUiStateManager(mainActivity.getUiStateManager());
        }

        exerciseFragment.setExerciseItem(exerciseItem);

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, exerciseFragment, ExerciseFragment.FRAGMENT_TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void evaluateRegex(String regex) {
        ExerciseItem exercise = exerciseFragment.getExerciseItem();
        ExerciseView view = exerciseFragment.getExerciseView();

        String match;
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(exercise.getData());

            StringBuffer buffer = new StringBuffer();

            // TODO: rather return a list of matches then all as one string
            while (matcher.find()) {
                buffer.append(matcher.group() + " ");
            }

            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }

            match = buffer.toString();
        } catch (Exception e) {
            view.updateRegexResult(regex, false, "Error: " + e.getMessage());
            return;
        }

        view.updateRegexResult(match, match.equals(exercise.getSolutionOutput()));
        //        if (exercise.isPreferSolutionOutput() && exercise.getSolutionOutput() != null) {
        //            view.updateRegexResult(match, match.equals(exercise.getSolutionOutput()));
        //        } else {
        //            view.updateRegexResult(match, regex.equals(exercise.getSolutionRegex()));
        //        }
    }

}
