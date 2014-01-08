package com.crocx.regex.exercises.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crocx.regex.R;
import com.crocx.regex.exercises.ExercisesAction;
import com.crocx.regex.exercises.model.ExerciseItem;
import com.crocx.regex.ui.UiStateManager;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseView extends RelativeLayout {

    private static final int COLOR_RESULT_MATCH_TRUE = 0xFF00FF00;
    private static final int COLOR_RESULT_MATCH_FALSE = 0xFFFF0000;
    private static final int COLOR_RESULT_MATCH_EMPTY = 0xFF888888;

    private ExerciseItem exerciseItem;

    private WebView contentWebView;
    private TextView result;
    private TextView resultError;
    private EditText input;
    private View animationArrow;

    public ExerciseView(Context context) {
        super(context);
    }

    public ExerciseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExerciseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        contentWebView = (WebView) findViewById(R.id.exerciseContent);
        result = (TextView) findViewById(R.id.exerciseResult);
        resultError = (TextView) findViewById(R.id.exerciseResultError);
        input = (EditText) findViewById(R.id.exerciseInput);
        animationArrow = findViewById(R.id.exerciseAnimationArrow);
    }

    public void init(final UiStateManager uiStateManager) {
        // manual check on Done button press (on the keyboard) - not sure if this is needed...
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkInputAndFireAction(uiStateManager);
                return true;
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkInputAndFireAction(uiStateManager);
            }
        });

        resultError.setTextColor(COLOR_RESULT_MATCH_FALSE);

        animationArrow.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.exercise_arrow_down_animation));
    }

    public void updateView(ExerciseItem exerciseItem) {
        if (this.exerciseItem == exerciseItem) {
            return;
        }
        this.exerciseItem = exerciseItem;

        contentWebView.loadUrl(exerciseItem.getInstructionsUrl());

        resultError.setText(null);
        resultError.setVisibility(GONE);

        input.setText(""); // clean possible previous input
    }

    public void updateRegexResult(String match, boolean matches) {
        updateRegexResult(match, matches, null);
    }

    public void updateRegexResult(String match, boolean matches, String error) {
        if (error != null) {
            resultError.setText(error);
            resultError.setVisibility(VISIBLE);
        } else {
            resultError.setText(null);
            resultError.setVisibility(GONE);
        }

        if (match.trim().length() > 0) {
            result.setText(match);
        } else {
            result.setText(R.string.exercise_hint_input_nothing_matches);
            result.setTextColor(COLOR_RESULT_MATCH_EMPTY);
            return;
        }

        if (matches) {
            result.setTextColor(COLOR_RESULT_MATCH_TRUE);
        } else {
            result.setTextColor(COLOR_RESULT_MATCH_FALSE);
        }
    }

    private void checkInputAndFireAction(UiStateManager uiStateManager) {
        if (input.getText() != null && input.getText().toString().trim().length() > 0) {
            result.setVisibility(VISIBLE);
            uiStateManager.fireAction(ExercisesAction.EVALUATE_REGEX, input.getText().toString());
            input.setHint(null);
        } else {
            //            input.setHint(R.string.exercise_hint_input_cant_be_empty);
            input.setHint(R.string.exercise_hint_input_here);
            updateRegexResult("", false);
            result.setVisibility(GONE);
        }
    }

    public String getRegex() {
        return input.getText().toString();
    }
}
