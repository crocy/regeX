package com.crocx.regex.exercises.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crocx.regex.R;
import com.crocx.regex.exercises.ExercisesAction;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.ui.UiStateManager;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseView extends LinearLayout {

    private static final int COLOR_RESULT_MATCH_TRUE = 0xFF00FF00;
    private static final int COLOR_RESULT_MATCH_FALSE = 0xFFFF0000;
    private static final int COLOR_RESULT_MATCH_EMPTY = 0xFF888888;

    private ExerciseItem exerciseItem;

    private WebView contentWebView;
    private TextView result;
    private EditText input;

    public ExerciseView(Context context) {
        super(context);
    }

    public ExerciseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        contentWebView = (WebView) findViewById(R.id.exerciseContent);
        result = (TextView) findViewById(R.id.exerciseResult);
        input = (EditText) findViewById(R.id.exerciseInput);
    }

    public void init(final UiStateManager uiStateManager) {
        //        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        //            @Override
        //            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //                if (input.getText().toString().trim().length() > 0) {
        //                    uiStateManager.fireAction(ExercisesAction.EVALUATE_REGEX, input.getText().toString());
        //                    input.setHint(null);
        //                } else {
        //                    input.setHint("IInput can't be empty");
        //                }
        //                return true;
        //            }
        //        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (input.getText().toString().trim().length() > 0) {
                    uiStateManager.fireAction(ExercisesAction.EVALUATE_REGEX, input.getText().toString());
                    input.setHint(null);
                } else {
                    input.setHint("IInput can't be empty");
                    updateRegexResult("", false);
                }
            }
        });
    }

    public void updateView(ExerciseItem exerciseItem) {
        if (this.exerciseItem == exerciseItem) {
            return;
        }
        this.exerciseItem = exerciseItem;

        //        contentWebView.loadData(exerciseItem.getInstructionsUrl(), "text/html", null);
        contentWebView.loadUrl(exerciseItem.getInstructionsUrl());

        if (exerciseItem.isPreferSolutionOutput()) {
            result.setText(exerciseItem.getSolutionOutput());
        } else {
            result.setText(exerciseItem.getSolutionRegex());
        }

        input.setText("");
    }

    public void updateRegexResult(String match, boolean matches) {
        if (match.trim().length() > 0) {
            result.setText(match);
        } else {
            result.setText("NNothing matches given regex");
            result.setTextColor(COLOR_RESULT_MATCH_EMPTY);
            return;
        }

        if (matches) {
            result.setTextColor(COLOR_RESULT_MATCH_TRUE);
        } else {
            result.setTextColor(COLOR_RESULT_MATCH_FALSE);
        }
    }
}
