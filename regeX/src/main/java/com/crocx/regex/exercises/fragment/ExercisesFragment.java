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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExercisesFragment extends Fragment {

    private static final String PATH_TO_ASSETS_EXERCISES = "exercises";

    private UiStateManager uiStateManager;
    private List<ExerciseItem> exercises;

    private ExercisesView exercisesView;

    /*
     * Fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public ExercisesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] exercisesList;
        try {
            exercisesList = getResources().getAssets().list(PATH_TO_ASSETS_EXERCISES);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String assetsUrlPrefix = "file:///android_asset/";
        String assetsUrl;

        exercises = new LinkedList<ExerciseItem>();
        for (int i = 0; i < exercisesList.length; i++) {
            assetsUrl = assetsUrlPrefix + PATH_TO_ASSETS_EXERCISES + "/" + exercisesList[i];
            ExerciseItem exerciseItem = new ExerciseItem(i);
            exerciseItem.setName("Exercise: " + exercisesList[i]);
            exerciseItem.loadContentFromAssetsUrl(assetsUrl, getResources().getAssets());
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

    public void setUiStateManager(UiStateManager uiStateManager) {
        this.uiStateManager = uiStateManager;
    }
}
