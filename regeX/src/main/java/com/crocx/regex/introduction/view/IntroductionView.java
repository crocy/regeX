package com.crocx.regex.introduction.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crocx.regex.MainActivity;
import com.crocx.regex.R;

/**
 * Created by Croc on 9.11.2013.
 */
public class IntroductionView extends RelativeLayout {

    private WebView webView;

    public IntroductionView(Context context) {
        super(context);
    }

    public IntroductionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntroductionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        webView = (WebView) findViewById(R.id.introductionWebView);

        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        webView.getSettings().setJavaScriptEnabled(true);
        //        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        // Let's display the progress in the activity title bar, like the
        // browser app does.
        final Activity activity = ((Activity) getContext());
        //        activity.getWindow().requestFeature(Window.FEATURE_PROGRESS);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        //        webView.loadUrl("http://en.wikipedia.org/wiki/Regular_expression");
        //        webView.loadUrl("file:///android_asset/offlineContent/syntax/AndroidAPI-Pattern.htm");
        webView.loadUrl(MainActivity.FILE_OFFLINE_REGEX_QUICKSTART);
        //        webView.loadUrl("http://docs.oracle.com/javase/tutorial/essential/regex/index.html");
    }
}
