package com.crocx.regex.exercises.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;

import com.crocx.regex.CommonAction;
import com.crocx.regex.MainActivity;
import com.crocx.regex.R;
import com.crocx.regex.exercises.ExercisesAction;
import com.crocx.regex.exercises.fragment.ExerciseFragment;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.exercises.view.ExerciseView;
import com.crocx.regex.exercises.view.RegexValidDialogView;
import com.crocx.regex.exercises.view.WebViewDialog;
import com.crocx.regex.tutorial.TutorialAction;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;
import com.crocx.regex.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseState extends UiState {

    private MainActivity mainActivity;
    private ExerciseFragment fragment;

    private boolean showRegexValidDialog = true;

    public ExerciseState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        // if exercise item was clicked (re)create fragment from scratch
        if (action == ExercisesAction.EXERCISE_ITEM_CLICKED) {
            fragment = new ExerciseFragment();
            fragment.setUiStateManager(mainActivity.getUiStateManager());
            fragment.setExerciseItem((ExerciseItem) actionObject);

            showRegexValidDialog = true;

        } else if (fragment == null) {
            fragment = new ExerciseFragment();
            fragment.setUiStateManager(mainActivity.getUiStateManager());
        }

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onExit(UiState newState, UiAction action, Object actionObject) {
        super.onExit(newState, action, actionObject);
        Util.hideSoftKeyboard(mainActivity, fragment.getExerciseView());
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
                    exerciseClicked((ExerciseItem) actionObject);
                    break;

                case EVALUATE_REGEX: {
                    String regex = (String) actionObject;
                    boolean regexValid = evaluateRegex(regex);
                    if (regexValid) {
                        mainActivity.getUiStateManager().fireAction(ExercisesAction.REGEX_VALID, regex);
                    }
                    break;
                }

                case REGEX_VALID:
                    if (showRegexValidDialog) {
                        showRegexValidDialog((String) actionObject);
                    }
                    break;

                case MENU_EXPLAIN_REGEX: {
                    String regex = fragment.getExerciseView().getRegex();
                    String input = fragment.getExerciseItem().getData();

                    Pair<String, String> pair = new Pair<String, String>(regex, input);
                    mainActivity.getUiStateManager().changeState(mainActivity.getTutorialState(),
                            TutorialAction.EXPLAIN_REGEX, pair);
                    break;
                }

                case MENU_SHOW_SYNTAX:
                    showSyntaxDialog();
                    break;

                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }

    private void exerciseClicked(ExerciseItem exerciseItem) {
        fragment.setExerciseItem(exerciseItem);
        fragment.getExerciseView().updateView(exerciseItem);
    }

    /**
     * Evaluate the given regex against the exercise's data.
     * 
     * @param regex
     * @return <b>true</b> if regex properly matches the exercise's data, <b>false</b> otherwise
     */
    private boolean evaluateRegex(String regex) {
        ExerciseItem exercise = fragment.getExerciseItem();
        ExerciseView view = fragment.getExerciseView();

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
            return false;
        }

        boolean regexValid = match.equals(exercise.getSolutionOutput());
        view.updateRegexResult(match, regexValid);
        return regexValid;
    }

    private void showSyntaxDialog() {
        WebViewDialog webViewDialog = new WebViewDialog();
        webViewDialog.setLoadUrl(MainActivity.FILE_OFFLINE_ANDROID_PATTERN);
        webViewDialog.show(mainActivity);
    }

    private void showRegexValidDialog(String userRegex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        RegexValidDialogView dialogView = (RegexValidDialogView) mainActivity.getLayoutInflater().inflate(
                R.layout.dialog_view_regex_valid, null);
        dialogView.init(userRegex, fragment.getExerciseItem().getSolutionRegex());
        builder.setView(dialogView);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        showRegexValidDialog = false;
    }
}
