package com.crocx.regex.tutorial.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;

import com.crocx.regex.CommonAction;
import com.crocx.regex.MainActivity;
import com.crocx.regex.R;
import com.crocx.regex.tutorial.TutorialAction;
import com.crocx.regex.tutorial.TutorialFragment;
import com.crocx.regex.tutorial.view.CustomInputDialogView;
import com.crocx.regex.tutorial.view.TutorialView;
import com.crocx.regex.ui.UiAction;
import com.crocx.regex.ui.UiState;

/**
 * Created by Croc on 24.11.2013.
 */
public class TutorialState extends UiState {

    private MainActivity mainActivity;
    private TutorialFragment fragment;
    private TutorialView tutorialView;

    public TutorialState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onEnter(UiState previousState, UiAction action, Object actionObject) {
        super.onEnter(previousState, action, actionObject);

        if (fragment == null) {
            fragment = new TutorialFragment();
            fragment.setUiStateManager(mainActivity.getUiStateManager());
        }

        if (action != null && action == TutorialAction.EXPLAIN_REGEX) {
            fragment.setFireActionOnCreate(action);
            fragment.setActionObjectOnCreate(actionObject);
        }

        FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onAction(UiAction action, Object actionObject) {
        tutorialView = fragment.getTutorialView();

        if (action instanceof CommonAction) {
            switch ((CommonAction) action) {
                case BACK:
                    mainActivity.getUiStateManager().goBack(CommonAction.BACK);
                    break;
            }
        } else {
            switch ((TutorialAction) action) {
                case START:
                    //                    tutorialView.updateView("foo+", "aa fa fo foo fooo foooo bar");
                    tutorialView.updateView("\\woo+", "aa fa fo foo fooo foooo bar");
                    break;

                case BUTTON_NEXT:
                    tutorialView.nextStep();
                    break;

                case BUTTON_PREVIOUS:
                    tutorialView.previousStep();
                    break;

                case BUTTON_RESTART:
                    tutorialView.restartMatching();
                    break;

                case SETTINGS_ACTION_CHANGE_INPUT_OUTPUT:
                    changeInputAndOutput();
                    break;

                case EXPLAIN_REGEX:
                    Pair<String, String> pair = (Pair<String, String>) actionObject;
                    tutorialView.updateView(pair.first, pair.second);
                    break;

                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }

    private void changeInputAndOutput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(R.string.menu_tutorial_change_input_output);

        final CustomInputDialogView view = (CustomInputDialogView) mainActivity.getLayoutInflater().inflate(
                R.layout.dialog_custom_input, null);
        view.setRegex(tutorialView.getTutorialRegex());
        view.setInput(tutorialView.getTutorialInput());

        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tutorialView.updateView(view.getRegex().trim(), view.getInput().trim());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();

        CustomInputDialogView.RegexValidationCallback validationCallback = new CustomInputDialogView.RegexValidationCallback() {
            @Override
            public void regexValid(boolean valid) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(valid);
            }

            @Override
            public void regexValidated() {
                dialog.dismiss();
                tutorialView.updateView(view.getRegex().trim(), view.getInput().trim());
            }
        };
        view.setRegexValidationCallback(validationCallback);

        dialog.show();
    }
}
