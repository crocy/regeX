package com.crocx.regex.main.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

        if (fragment == null) {
            fragment = new MainFragment();
            fragment.setUiStateManager(mainActivity.getUiStateManager());
        }

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
                    mainActivity.getUiStateManager().end();
                    mainActivity.onSuperBackPressed();
                    break;
            }
        } else {
            switch ((MainAction) action) {
                case BUTTON_INTRODUCTION:
                    mainActivity.getUiStateManager().changeState(mainActivity.getIntroductionState());
                    break;

                case BUTTON_TUTORIAL:
                    mainActivity.getUiStateManager().changeState(mainActivity.getTutorialState());
                    break;

                case BUTTON_EXERCISES:
                    mainActivity.getUiStateManager().changeState(mainActivity.getExercisesState());
                    break;

                case BUTTON_LINKS:
                    showLinksPopupDialog();
                    break;

                default:
                    throwOnUnknownAction(action, actionObject);
            }
        }
    }

    private void showLinksPopupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setItems(R.array.usefulLinks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] links = mainActivity.getResources().getStringArray(R.array.usefulLinks);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(links[which]));
                mainActivity.startActivity(intent);
            }
        });
        builder.show();
    }
}
