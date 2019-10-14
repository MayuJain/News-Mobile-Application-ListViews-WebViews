package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewActivity extends AppCompatActivity {

    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        myWebView = findViewById(R.id.webViewId);

        String url = getIntent().getExtras().getString(NewsActivity.NEWS_KEY);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(url);

    }
}
