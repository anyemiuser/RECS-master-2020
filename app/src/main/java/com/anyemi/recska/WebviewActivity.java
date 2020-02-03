package com.anyemi.recska;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;


public class WebviewActivity extends AppCompatActivity  {
    WebView browser;
    EditText editText;
    DrawerLayout drawer;
    int userId=-1;
    NavigationView navigationView;
    String page;
    SharedPreferences prefs;
    String url;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);



       browser= findViewById(R.id.webview);
        editText= findViewById(R.id.et_search);

        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");



       // browser.getSettings().setJavaScriptEnabled(true);


        if(editText.getText().toString().equals("")){
            editText.setText("https://www.google.co.in");
            browser.loadUrl("https://www.google.co.in");
        }else{
            browser.loadUrl(editText.getText().toString());
        }



        browser.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                browser.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

    }

    void loadTime(String s) {


        browser.loadDataWithBaseURL("x-data://base", page,
                "text/html", "UTF-8",
                null);
    }

    private class Callback extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            loadTime(page);

            return(true);
        }
    }
}
