package com.olegator555.rasp;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class StationInfoActivity extends AppCompatActivity {
    private TextView tempTextView;
    private String url;
    private ArrayList<String> list;
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @JavascriptInterface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        tempTextView = findViewById(R.id.tempTextView);
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
        WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setSaveFormData(true);
        webview.getSettings().setSavePassword(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36");
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "htmlHandler");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:window.htmlHandler.handleHtml" +
                        "('<html>'+document.getElementsByTagName('script')[0]+'</html>');");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

        });
        webview.loadUrl(url);

    }
    private static class MyJavaScriptInterface {
        @JavascriptInterface
        public void handleHtml(String html) {
            Document doc = Jsoup.parse(html);
            Log.d("html", html);
        }
    }
}