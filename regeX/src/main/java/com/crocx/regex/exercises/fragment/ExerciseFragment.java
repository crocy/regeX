package com.crocx.regex.exercises.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crocx.regex.R;
import com.crocx.regex.exercises.ExercisesAction;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.exercises.view.ExerciseView;
import com.crocx.regex.ui.UiStateManager;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseFragment extends Fragment {

    private UiStateManager uiStateManager;
    private ExerciseItem exerciseItem;

    private ExerciseView exerciseView;

    /*
     * Fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public ExerciseFragment() {
        setHasOptionsMenu(true); // this shows settings menu in the action bar
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exerciseView = (ExerciseView) inflater.inflate(R.layout.exercise, container, false);
        exerciseView.init(uiStateManager);
        exerciseView.updateView(exerciseItem);
        return exerciseView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.exercise, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_menu_exercise_explain);
        if (exerciseView.getRegex() != null && !exerciseView.getRegex().isEmpty()) {
            item.setEnabled(true);
        } else {
            item.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_menu_exercise_explain:
                uiStateManager.fireAction(ExercisesAction.MENU_EXPLAIN_REGEX);
                return true;

            case R.id.action_menu_exercise_syntax:
                uiStateManager.fireAction(ExercisesAction.MENU_SHOW_SYNTAX);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ExerciseItem getExerciseItem() {
        return exerciseItem;
    }

    public void setExerciseItem(ExerciseItem exerciseItem) {
        this.exerciseItem = exerciseItem;
    }

    public void setUiStateManager(UiStateManager uiStateManager) {
        this.uiStateManager = uiStateManager;
    }

    public ExerciseView getExerciseView() {
        return exerciseView;
    }
}
