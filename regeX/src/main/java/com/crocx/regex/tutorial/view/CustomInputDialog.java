package com.crocx.regex.tutorial.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crocx.regex.R;

import java.util.regex.Pattern;

/**
 * Created by Croc on 31.12.2013.
 */
public class CustomInputDialog extends LinearLayout {

    private EditText editRegex;
    private TextView regexMessage; // error feedback
    private EditText editInput;

    private boolean regexValid = false;

    private RegexValidationCallback regexValidationCallback;

    public interface RegexValidationCallback {
        public void regexValid(boolean valid);
    }

    public CustomInputDialog(Context context) {
        super(context);
    }

    public CustomInputDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        editRegex = (EditText) findViewById(R.id.dialogEditRegex);
        regexMessage = (TextView) findViewById(R.id.dialogRegexMessage);
        editInput = (EditText) findViewById(R.id.dialogEditInput);

        init();
    }

    private void init() {
        editRegex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateRegex(s.toString());
            }
        });

        regexMessage.setVisibility(GONE);
    }

    private void validateRegex(String regex) {
        try {
            Pattern.compile(regex);
            regexValid = true;
            //            regexMessage.setText("RRegex ok");
            regexMessage.setVisibility(GONE);
        } catch (Exception e) {
            regexValid = false;
            regexMessage.setVisibility(VISIBLE);
            regexMessage.setText(e.getMessage());
        }

        if (regexValidationCallback != null) {
            regexValidationCallback.regexValid(regexValid);
        }
    }

    public String getRegex() {
        return editRegex.getText().toString();
    }

    public void setRegex(String regex) {
        editRegex.setText(regex.trim());
    }

    public String getInput() {
        return editInput.getText().toString();
    }

    public void setInput(String input) {
        editInput.setText(input.trim());
    }

    public boolean isRegexValid() {
        return regexValid;
    }

    public void setRegexValidationCallback(RegexValidationCallback regexValidationCallback) {
        this.regexValidationCallback = regexValidationCallback;
    }
}
