package com.kevinlu.watstats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private WebView webDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webDriver = findViewById(R.id.webview);
        webDriver.loadUrl("https://watcard.uwaterloo.ca/OneWeb/Account/LogOn");
        webDriver.getSettings().setJavaScriptEnabled(true);
        webDriver.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                String user = "watcard number";
                String pwd = "1234";
                view.loadUrl("javascript:document.getElementById('Account').value = '" + user + "';document.getElementById('Password').value='" + pwd + "';"+"(function(){ l=document.getElementsByClassName('btn ow-btn-primary btn-block-xs pull-right')[0]; l.click();})()");
            }
        });
    }
}











