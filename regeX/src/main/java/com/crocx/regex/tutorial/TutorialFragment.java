package com.crocx.regex.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crocx.regex.R;
import com.crocx.regex.tutorial.view.TutorialView;
import com.crocx.regex.ui.UiStateManager;

/**
 * Created by Croc on 24.11.2013.
 */
public class TutorialFragment extends Fragment {

    private UiStateManager uiStateManager;

    private TutorialView tutorialView;

    /*
     * Fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public TutorialFragment() {
        setHasOptionsMenu(true); // this shows settings menu in the action bar
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tutorialView = (TutorialView) inflater.inflate(R.layout.fragment_tutorial, container, false);
        tutorialView.init(uiStateManager);
        uiStateManager.fireAction(TutorialAction.START);
        return tutorialView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.tutorial, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_menu_tutorial_change_input_output:
                uiStateManager.fireAction(TutorialAction.SETTINGS_ACTION_CHANGE_INPUT_OUTPUT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public TutorialView getTutorialView() {
        return tutorialView;
    }

    public void setUiStateManager(UiStateManager uiStateManager) {
        this.uiStateManager = uiStateManager;
    }
}
