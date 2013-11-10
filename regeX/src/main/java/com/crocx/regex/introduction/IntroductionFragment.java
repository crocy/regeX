package com.crocx.regex.introduction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crocx.regex.R;

/**
 * Created by Croc on 9.11.2013.
 */
public class IntroductionFragment extends Fragment {

    /*
     * Fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public IntroductionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduction, container, false);
    }

}
