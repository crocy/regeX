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
import com.crocx.regex.engine.MatcherResult;
import com.crocx.regex.engine.RegexEngine;
import com.crocx.regex.tutorial.TutorialAction;
import com.crocx.regex.ui.UiStateManager;

import java.util.LinkedList;

/**
 * Created by Croc on 24.11.2013.
 */
public class TutorialView extends LinearLayout {

    private TextView tutorialRegex;
    private TextView tutorialInput;
    private Button buttonNext;

    private RegexEngine engine;
    private LinkedList<MatcherResult> results;
    private LinkedList<MatcherResult> seenResults;
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

        tutorialRegex.setText(regex);
        tutorialInput.setText(input);

        engine = new RegexEngine();
        results = engine.processRegexAndInput(regex, input);
        seenResults = new LinkedList<MatcherResult>();

        if (results != null && !results.isEmpty()) {
            buttonNext.setEnabled(true);
        } else {
            buttonNext.setEnabled(false);
        }
    }

    public void nextStep() {
        MatcherResult result = results.removeFirst();
        seenResults.add(result);

        if (results.isEmpty()) {
            //                    buttonNext.setEnabled(false);
            buttonNext.setText("RRestart");
            restartMatching();
        } else {
            buttonNext.setText("NNext");
        }

        if (result.getType() == MatcherResult.ResultType.MISMATCH) {
            SpannableString spannableRegex = new SpannableString(regex);
            spannableRegex.setSpan(new ForegroundColorSpan(Color.RED), 0, regex.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tutorialRegex.setText(spannableRegex);

            tutorialInput.setText(input);
            return;
        }

        int offset = result.getMatchStart();
        String offsetSpaces = addSpacesOffset(offset);
        //        SpannableString spannableRegex = new SpannableString(result.group(RegexEngine.GROUP_LITERALS));
        SpannableString spannableRegex = new SpannableString(offsetSpaces + regex);
        //        SpannableString spannableRegex = new SpannableString(offsetSpaces + result.getPattern());
        spannableRegex.setSpan(new ForegroundColorSpan(Color.GREEN), offset + result.getPatternStart(),
                offset + result.getPatternEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableRegex.setSpan(new ForegroundColorSpan(Color.YELLOW), offset + result.getPatternEnd(),
                spannableRegex.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tutorialRegex.setText(spannableRegex);

        SpannableString spannableInput = new SpannableString(input);
        //        SpannableString spannableInput = new SpannableString(result.getMatch());
        spannableInput.setSpan(new ForegroundColorSpan(Color.GREEN), result.getMatchStart(), result.getMatchEnd(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tutorialInput.setText(spannableInput);
    }

    private String addSpacesOffset(int numOfSpaces) {
        StringBuffer buffer = new StringBuffer(numOfSpaces);
        for (int i = 0; i < numOfSpaces; i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }

    private void restartMatching() {
        results = seenResults;
        seenResults = new LinkedList<MatcherResult>();
    }
}
