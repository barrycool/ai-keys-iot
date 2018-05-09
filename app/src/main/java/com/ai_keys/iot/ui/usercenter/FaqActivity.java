package com.ai_keys.iot.ui.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.TextView;

import com.ai_keys.iot.R;

import java.util.Locale;

public class FaqActivity extends Activity {

    private WebView mWebView;
    private TextView mTitleTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        mWebView = (WebView) findViewById(R.id.faq_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mTitleTxt = (TextView) findViewById(R.id.title_txt);
        mTitleTxt.setText(R.string.faq_alert);

        loadFaqWebview();
    }

    private void loadFaqWebview() {
        Locale curLocale = getResources().getConfiguration().locale;
        if (curLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
            mWebView.loadUrl("file:///android_asset/faq.html");
        } else {
            mWebView.loadUrl("file:///android_asset/faq_en.html");
        }
    }
}
