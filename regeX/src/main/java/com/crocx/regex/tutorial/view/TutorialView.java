package com.crocx.regex.tutorial.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crocx.regex.R;
import com.crocx.regex.tutorial.RegexEngine;
import com.crocx.regex.tutorial.TutorialAction;
import com.crocx.regex.ui.UiStateManager;

import java.util.LinkedList;
import java.util.regex.MatchResult;

/**
 * Created by Croc on 24.11.2013.
 */
public class TutorialView extends LinearLayout {

    private TextView tutorialRegex;
    private TextView tutorialInput;
    private Button buttonNext;

    private RegexEngine engine;
    private LinkedList<MatchResult> results;
    private String regex;
    private String input;

    public TutorialView(Context context) {
        super(context);
    }

    public TutorialView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tutorialRegex = (TextView) findViewById(R.id.tutorialRegex);
        tutorialInput = (TextView) findViewById(R.id.tutorialInput);
        buttonNext = (Button) findViewById(R.id.tutorialButtonNext);
    }

    public void init(final UiStateManager uiStateManager) {
        buttonNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(TutorialAction.BUTTON_NEXT);
            }
        });
    }

    public void updateView(String regex, String input) {
        this.regex = regex;
        this.input = input;

        engine = new RegexEngine();
        results = engine.processRegexAndInput(regex, input);

        if (results != null && !results.isEmpty()) {
            buttonNext.setEnabled(true);
        } else {
            buttonNext.setEnabled(false);
        }
    }

    public void nextStep() {
        MatchResult result = results.removeFirst();

        if (results.isEmpty()) {
            buttonNext.setEnabled(false);
        }

        //        SpannableString spannableRegex = new SpannableString(result.group(RegexEngine.GROUP_LITERALS));
        SpannableString spannableRegex = new SpannableString(input);
        spannableRegex.setSpan(new ForegroundColorSpan(Color.GREEN), result.start(), result.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tutorialInput.setText(spannableRegex);
    }
}
