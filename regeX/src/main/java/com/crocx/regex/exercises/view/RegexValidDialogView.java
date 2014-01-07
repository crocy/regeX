package com.crocx.regex.exercises.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crocx.regex.R;

/**
 * Created by Croc on 7.1.2014.
 */
public class RegexValidDialogView extends LinearLayout {

    private TextView userRegexText;
    private TextView ourSolutionText;
    private TextView solutionRegexText;

    public RegexValidDialogView(Context context) {
        super(context);
    }

    public RegexValidDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        userRegexText = (TextView) findViewById(R.id.dialogRegexValidUserRegex);
        ourSolutionText = (TextView) findViewById(R.id.dialogRegexValidOurSolution);
        solutionRegexText = (TextView) findViewById(R.id.dialogRegexValidSolutionRegex);
    }

    public void init(String userRegex, String solutionRegex) {
        userRegexText.setText(userRegex);

        if (solutionRegex != null && !solutionRegex.isEmpty()) {
            ourSolutionText.setVisibility(VISIBLE);
            solutionRegexText.setVisibility(VISIBLE);
            solutionRegexText.setText(solutionRegex);
        } else {
            ourSolutionText.setVisibility(GONE);
            solutionRegexText.setVisibility(GONE);
        }
    }
}
