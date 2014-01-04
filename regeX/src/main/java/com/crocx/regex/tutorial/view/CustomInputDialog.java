package com.crocx.regex.tutorial.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
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

        public void regexValidated();
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

        editRegex.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return validateRegexOnKeyDone();
            }
        });

        editInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return validateRegexOnKeyDone();
            }
        });

        regexMessage.setVisibility(GONE);
    }

    private void validateRegex(String regex) {
        if (regex != null && !regex.trim().isEmpty()) {
            try {
                Pattern.compile(regex);
                regexValid = true;
                regexMessage.setVisibility(GONE);
            } catch (Exception e) {
                regexValid = false;
                regexMessage.setVisibility(VISIBLE);
                regexMessage.setText(e.getMessage());
            }
        } else {
            regexValid = false;
            regexMessage.setVisibility(VISIBLE);
            regexMessage.setText(R.string.tutorial_change_dialog_error_empty_regex);
        }

        if (regexValidationCallback != null) {
            regexValidationCallback.regexValid(regexValid);
        }
    }

    private boolean validateRegexOnKeyDone() {
        validateRegex(editRegex.getText().toString());

        if (regexValid && regexValidationCallback != null) {
            regexValidationCallback.regexValidated();
        }

        return regexValid;
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
