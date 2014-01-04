package com.crocx.regex.tutorial.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crocx.regex.R;
import com.crocx.regex.engine.MatcherResult;
import com.crocx.regex.engine.RegexEngine;
import com.crocx.regex.engine.RegexExplanation;
import com.crocx.regex.tutorial.TutorialAction;
import com.crocx.regex.ui.UiStateManager;
import com.crocx.regex.util.Logger;

import java.util.LinkedList;

/**
 * Created by Croc on 24.11.2013.
 */
public class TutorialView extends LinearLayout {

    private TextView tutorialRegex;
    private TextView tutorialInput;
    private Button buttonNext;
    private Button buttonPrevious;
    private Button buttonRestart;
    private TextView tutorialExplanation;
    private ListView explanationsListView;

    private RegexEngine engine;
    private LinkedList<MatcherResult> results;
    private LinkedList<MatcherResult> seenResults;
    private String regex;
    private String input;

    private ArrayAdapter<RegexExplanation> explanationAdapter;

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
        buttonPrevious = (Button) findViewById(R.id.tutorialButtonPrevious);
        buttonRestart = (Button) findViewById(R.id.tutorialButtonRestart);
        tutorialExplanation = (TextView) findViewById(R.id.tutorialExplanations);
        explanationsListView = (ListView) findViewById(R.id.tutorialExplanationList);
    }

    public void init(final UiStateManager uiStateManager) {
        buttonNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(TutorialAction.BUTTON_NEXT);
            }
        });

        buttonPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(TutorialAction.BUTTON_PREVIOUS);
            }
        });

        buttonRestart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uiStateManager.fireAction(TutorialAction.BUTTON_RESTART);
            }
        });

        explanationAdapter = new ArrayAdapter<RegexExplanation>(getContext(), 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view;
                if (convertView == null) {
                    view = new TextView(getContext(), null, R.style.textRegexMedium);
                } else {
                    view = (TextView) convertView;
                }

                RegexExplanation explanation = explanationAdapter.getItem(position);
                updateExplanation(explanation, view);

                return view;
            }
        };
        explanationsListView.setAdapter(explanationAdapter);
    }

    public void updateView(String regex, String input) {
        this.regex = regex;
        this.input = input;

        tutorialRegex.setText(regex);
        tutorialInput.setText(input);

        engine = new RegexEngine();
        //        engine.setProcessPerWord(false);
        results = engine.processRegexAndInput(regex, input);
        seenResults = new LinkedList<MatcherResult>();

        buttonNext.setEnabled(false);
        buttonPrevious.setEnabled(false);
        buttonRestart.setEnabled(false);

        if (results != null && !results.isEmpty()) {
            buttonNext.setEnabled(true);
            //            buttonRestart.setEnabled(true);
        }

        loadExplanations();
    }

    private void updateExplanation(RegexExplanation explanation, TextView view) {
        SpannableString spannable;
        switch (explanation.getEmphasiseType()) {
            case EMPHASISE_MATCH:
                spannable = new SpannableString(explanation.getExplanationMessage());
                spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                view.setText(spannable);
                break;

            case EMPHASISE_MISMATCH:
                spannable = new SpannableString(explanation.getExplanationMessage());
                spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, spannable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                view.setText(spannable);
                break;

            default:
                view.setText(explanation.getExplanationMessage());
        }

        //        ((LayoutParams) view.getLayoutParams()).
        view.setPadding(
                explanation.getLevel() * getResources().getDimensionPixelOffset(R.dimen.explanation_child_offset), 0,
                0, 0);

        //        if (explanation.getChildExplanation() != null) {
        //            updateExplanation(explanation.getChildExplanation(), view);
        //        }
    }

    public void nextStep() {
        MatcherResult result = results.removeFirst();
        seenResults.add(result);

        step(result);
    }

    public void previousStep() {
        MatcherResult result = seenResults.removeLast();
        results.addFirst(result);

        step(seenResults.getLast());
    }

    public void restartMatching() {
        seenResults.addAll(results); // add the remaining unseen results
        results = seenResults; // and start from the beginning
        seenResults = new LinkedList<MatcherResult>();

        nextStep();

        buttonRestart.setEnabled(false);
    }

    private void step(MatcherResult result) {
        buttonNext.setEnabled(!results.isEmpty());
        buttonPrevious.setEnabled(seenResults.size() > 1);
        buttonRestart.setEnabled(true);

        Logger.debug("Showing result: " + result);

        int offset = Math.max(0, result.getMatchStart() - result.getPatternStart());
        String offsetSpaces = addSpacesOffset(offset);

        emphasiseExplanation(result);

        if (result.getType() == MatcherResult.ResultType.MISMATCH) {
            SpannableString spannableRegex = new SpannableString(offsetSpaces + regex);
            //            spannableRegex.setSpan(new ForegroundColorSpan(Color.RED), offset, offset + regex.length(),
            //                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableRegex.setSpan(new ForegroundColorSpan(Color.RED), offset + result.getPatternStart(), offset
                    + result.getPatternEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tutorialRegex.setText(spannableRegex);

            SpannableString spannableInput = new SpannableString(input);
            //        SpannableString spannableInput = new SpannableString(result.getMatch());
            spannableInput.setSpan(new ForegroundColorSpan(Color.RED), result.getMatchStart(), result.getMatchEnd(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tutorialInput.setText(spannableInput);
            //            tutorialInput.setText(input);
            return;
        }

        //        SpannableString spannableRegex = new SpannableString(result.group(RegexEngine.GROUP_LITERALS));
        SpannableString spannableRegex = new SpannableString(offsetSpaces + regex);
        //        SpannableString spannableRegex = new SpannableString(offsetSpaces + result.getPattern());
        spannableRegex.setSpan(new ForegroundColorSpan(Color.GREEN), offset + result.getPatternStart(),
                offset + result.getPatternEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //        spannableRegex.setSpan(new ForegroundColorSpan(Color.YELLOW), offset + result.getPatternEnd(),
        //                spannableRegex.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

    private void loadExplanations() {
        if (engine.getExplanations() != null) {
            explanationAdapter.clear();
            explanationAdapter.setNotifyOnChange(false);

            for (RegexExplanation explanation : engine.getExplanations()) {
                //                explanationAdapter.add(explanation);
                loadChildExplanations(explanation, explanationAdapter);
            }
            explanationAdapter.notifyDataSetChanged();
        }
    }

    private void loadChildExplanations(RegexExplanation explanation, ArrayAdapter adapter) {
        adapter.add(explanation);

        if (explanation.getChildExplanation() != null) {
            loadChildExplanations(explanation.getChildExplanation(), adapter);
        }
    }

    /**
     * Emphasise the given explanation.
     * 
     * @param result the explanation to emphasise (as in this is the current regex/explanation we are looking at).
     */
    //    private void emphasiseExplanation(RegexExplanation emphasiseExplanation) {
    private void emphasiseExplanation(MatcherResult result) {
        explanationAdapter.setNotifyOnChange(false);
        RegexExplanation emphasiseExplanation = result.getExplanation();

        if (engine.getExplanations() != null) {
            for (RegexExplanation explanation : engine.getExplanations()) {
                if (explanation == emphasiseExplanation) {
                    switch (result.getType()) {
                        case MATCH:
                            explanation.setEmphasiseType(RegexExplanation.EmphasiseType.EMPHASISE_MATCH);
                            break;

                        case MISMATCH:
                            explanation.setEmphasiseType(RegexExplanation.EmphasiseType.EMPHASISE_MISMATCH);
                            break;
                    }
                } else {
                    explanation.setEmphasiseType(RegexExplanation.EmphasiseType.NONE);
                }
            }
        } else {
            tutorialExplanation.setText(null);
        }

        explanationsListView.smoothScrollToPosition(explanationAdapter.getPosition(emphasiseExplanation));

        explanationAdapter.notifyDataSetChanged();
    }

    public String getTutorialRegex() {
        return tutorialRegex.getText().toString();
    }

    public String getTutorialInput() {
        return tutorialInput.getText().toString();
    }
}
