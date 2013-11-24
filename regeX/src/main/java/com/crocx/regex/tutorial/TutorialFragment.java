package com.crocx.regex.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crocx.regex.R;

/**
 * Created by Croc on 24.11.2013.
 */
public class TutorialFragment extends Fragment {

    /*
     * Fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public TutorialFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

}
