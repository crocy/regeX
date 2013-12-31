package com.crocx.regex;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.crocx.regex.exercises.control.ExercisesState;
import com.crocx.regex.introduction.control.IntroductionState;
import com.crocx.regex.main.control.MainState;
import com.crocx.regex.tutorial.control.TutorialState;
import com.crocx.regex.ui.UiStateManager;

public class MainActivity extends ActionBarActivity {

    private UiStateManager uiStateManager;
    private MainState mainState;
    private IntroductionState introductionState;
    private TutorialState tutorialState;
    private ExercisesState exercisesState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_main);

        uiStateManager = new UiStateManager();
        mainState = new MainState(this);
        introductionState = new IntroductionState(this);
        tutorialState = new TutorialState(this);
        exercisesState = new ExercisesState(this);

        uiStateManager.start(mainState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //        getMenuInflater().inflate(R.menu.main, menu);
        //        return true;

        return false; // don't show general settings menu (yet)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        uiStateManager.fireAction(CommonAction.BACK);
        super.onBackPressed();
    }

    //    public void onSuperBackPressed() {
    //        super.onBackPressed();
    //    }

    public UiStateManager getUiStateManager() {
        return uiStateManager;
    }

    public MainState getMainState() {
        return mainState;
    }

    public IntroductionState getIntroductionState() {
        return introductionState;
    }

    public ExercisesState getExercisesState() {
        return exercisesState;
    }

    public TutorialState getTutorialState() {
        return tutorialState;
    }
}
