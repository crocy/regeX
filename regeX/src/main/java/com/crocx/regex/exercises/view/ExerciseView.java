package com.crocx.regex.exercises.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
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
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (input.getText().length() > 0) {
                    uiStateManager.fireAction(ExercisesAction.EVALUATE_REGEX, input.getText());
                    input.setHint(null);
                } else {
                    input.setHint("IInput can't be empty");
                }
                return true;
            }
        });
    }

    public void updateView(ExerciseItem exerciseItem) {
        if (this.exerciseItem == exerciseItem) {
            return;
        }
        this.exerciseItem = exerciseItem;

        contentWebView.loadData(exerciseItem.getInstructionsUrl(), "text/html", null);

        if (exerciseItem.isPreferSolutionOutput()) {
            result.setText(exerciseItem.getSolutionOutput());
        } else {
            result.setText(exerciseItem.getSolutionRegex());
        }

        input.setText("");
    }

    public void updateRegexResult(String result, boolean matches) {
        this.result.setText(result);

        if (matches) {
            this.result.setTextColor(COLOR_RESULT_MATCH_TRUE);
        } else {
            this.result.setTextColor(COLOR_RESULT_MATCH_FALSE);
        }
    }
}
