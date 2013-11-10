package com.crocx.regex.exercises.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crocx.regex.R;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.exercises.view.ExercisesView;
import com.crocx.regex.ui.UiStateManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExercisesFragment extends Fragment {

    private UiStateManager uiStateManager;
    private List<ExerciseItem> exercises;

    private ExercisesView exercisesView;

    public ExercisesFragment(UiStateManager uiStateManager) {
        this.uiStateManager = uiStateManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exercises = new LinkedList<ExerciseItem>();
        for (int i = 0; i < 5; i++) {
            ExerciseItem exerciseItem = new ExerciseItem(i);
            exerciseItem.setExerciseName("Exercise " + i);
            exerciseItem.setContent("Content " + i);
            exerciseItem.setSolution("lol " + i);

            exercises.add(exerciseItem);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exercisesView = (ExercisesView) inflater.inflate(R.layout.fragment_exercises, container, false);
        exercisesView.init(uiStateManager);
        exercisesView.updateView(exercises);
        return exercisesView;
    }

}
