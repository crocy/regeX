package com.crocx.regex.exercises.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

/**
 * Created by Croc on 5.1.2014.
 */
public class WebViewDialog extends DialogFragment {

    private WebView webView;
    private String loadUrl;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        webView = new WebView(getActivity());
        webView.loadUrl(loadUrl);

        builder.setView(webView);

        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    public void show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), "WebViewDialog");
    }
}
