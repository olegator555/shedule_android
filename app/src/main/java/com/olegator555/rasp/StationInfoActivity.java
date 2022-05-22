package com.olegator555.rasp;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StationInfoActivity extends AppCompatActivity {
    private TextView tempTextView;
    private String url;
    private ProgressBar progressBar;
    private WebView webView;
    private ArrayList<String> list;
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @JavascriptInterface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        tempTextView = findViewById(R.id.tempTextView);
        progressBar = findViewById(R.id.webVIewProgressBar);
        String stationName = getIntent().getStringExtra("StationName");
        url = "https://central-ppk.ru/new/imap/map_cl.php?searching=1&searchstation=" + stationName;
        Log.d("Url", url);
        list = new ArrayList<>();
        /*new AsyncHtmlParser(url, this) {
            @Override
            public void parseData(Document document) {
                Elements eleme nts =  document.getElementsByClass("blocklinewith_infotip");
                for(Element element : elements) {
                    list.add(element.text());
                }
                Log.d("List", list.toString());
            }

            @Override
            public void onDataReceived() {
                tempTextView.setText(list.toString());
            }
        }.start();*/
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36");
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                //webview.loadUrl("javascript:window.HTMLOUT.handleHtml(document.getElementsByClassName('ymaps'));");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

        });
        webView.loadUrl(url);

    }
    private static class MyJavaScriptInterface {
        @JavascriptInterface
        public void handleHtml(String html) {
            Log.d("Caller", "CALLED!");
            //Document doc = Jsoup.parse(html);
            Log.d("html", html);
        }
    }
}