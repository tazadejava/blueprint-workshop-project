package org.hackmit.workshopproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        WebView myWebView = findViewById(R.id.webView);
        myWebView.loadUrl("https://www.google.com");
    }
}