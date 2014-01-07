package com.crocx.regex.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.crocx.regex.R;
import com.crocx.regex.main.MainAction;
import com.crocx.regex.ui.UiStateManager;

/**
 * Created by Croc on 9.11.2013.
 */
public class MainView extends RelativeLayout {

    private Button buttonIntroduction;
    private Button buttonExamples;
    private Button buttonExercises;
    private Button buttonLinks;

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        buttonIntroduction = (Button) findViewById(R.id.mainButtonIntroduction);
        buttonExamples = (Button) findViewById(R.id.mainButtonExamples);
        buttonExercises = (Button) findViewById(R.id.mainButtonExercises);
        buttonLinks = (Button) findViewById(R.id.mainButtonLinks);
    }

    public void init(final UiStateManager uiStateManager) {
        buttonIntroduction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(MainAction.BUTTON_INTRODUCTION);
            }
        });

        buttonExamples.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(MainAction.BUTTON_TUTORIAL);
            }
        });

        buttonExercises.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(MainAction.BUTTON_EXERCISES);
            }
        });

        buttonLinks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(MainAction.BUTTON_LINKS);
            }
        });
    }
}
