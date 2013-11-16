package com.crocx.regex.exercises.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crocx.regex.R;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.exercises.view.ExerciseView;
import com.crocx.regex.ui.UiStateManager;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseFragment extends Fragment {

    public static final String FRAGMENT_TAG = "exerciseFragment";

    private UiStateManager uiStateManager;
    private ExerciseItem exerciseItem;

    private ExerciseView exerciseView;

    /*
     * Fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public ExerciseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exerciseView = (ExerciseView) inflater.inflate(R.layout.exercise, container, false);
        exerciseView.init(uiStateManager);
        return exerciseView;
    }

    @Override
    public void onResume() {
        super.onResume();

        exerciseView.updateView(exerciseItem);
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
