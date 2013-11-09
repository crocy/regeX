package com.crocx.regex.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crocx.regex.R;
import com.crocx.regex.main.view.MainView;
import com.crocx.regex.ui.UiStateManager;

/**
 * Created by Croc on 9.11.2013.
 */
public class MainFragment extends Fragment {

    private UiStateManager uiStateManager;

    private MainView mainView;

    public MainFragment(UiStateManager uiStateManager) {
        this.uiStateManager = uiStateManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = (MainView) inflater.inflate(R.layout.fragment_main, container, false);
        mainView.init(uiStateManager);
        return mainView;
    }

}
