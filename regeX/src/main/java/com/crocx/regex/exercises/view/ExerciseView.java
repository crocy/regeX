package com.crocx.regex.exercises.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crocx.regex.R;
import com.crocx.regex.exercises.model.ExerciseItem;

/**
 * Created by Croc on 10.11.2013.
 */
public class ExerciseView extends LinearLayout {

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

    public void updateView(ExerciseItem exerciseItem) {
        if (this.exerciseItem == exerciseItem) {
            return;
        }
        this.exerciseItem = exerciseItem;

        contentWebView.loadData(exerciseItem.getContent(), "text/plain", null);
        result.setText(exerciseItem.getSolution());
        input.setText("");
    }
}
