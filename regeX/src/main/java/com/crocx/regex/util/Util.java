package com.crocx.regex.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Croc on 31.12.2013.
 */
public class Util {

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
