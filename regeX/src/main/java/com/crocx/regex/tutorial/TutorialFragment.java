package com.crocx.regex.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    public TutorialFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tutorialView = (TutorialView) inflater.inflate(R.layout.fragment_tutorial, container, false);
        tutorialView.init(uiStateManager);
        tutorialView.updateView("foo+", "aa fo foo fooo foooo bar");
        return tutorialView;
    }

    public TutorialView getTutorialView() {
        return tutorialView;
    }

    public void setUiStateManager(UiStateManager uiStateManager) {
        this.uiStateManager = uiStateManager;
    }
}
