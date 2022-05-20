package Utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.olegator555.rasp.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public abstract class AsyncHtmlParser extends Thread {
    private final String str_url;
    private final Activity activity;
    private Document doc;
    private ArrayList<String> list;

    public AsyncHtmlParser(String str_url, Activity activity) {
        this.str_url = str_url;
        this.activity = activity;
        list = new ArrayList<>();
    }

    private Document receiveDocument() {
        /*HttpsURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str_url);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(newSslSocketFactory());
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Android 10; Mobile; rv:97.0) Gecko/97.0 Firefox/97.0");
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null)
                sb.append(str).append('\n');
            return Jsoup.parse(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;*/
        WebView webview = new WebView(activity);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setSaveFormData(true);
        webview.getSettings().setSavePassword(true);
        webview.getSettings().setLoadsImagesAutomatically(false);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlHandler");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                webview.loadUrl("javascript:window.HtmlHandler.handleHtml" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");


            }
        });
        webview.getSettings().setUserAgentString("Chrome/41.0.2228.0 Safari/537.36");
        webview.loadUrl("");
        return null;

    }


    private SSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            InputStream inputStream = activity.getResources().openRawResource(R.raw.local_keystore);
            keyStore.load(inputStream, "123456".toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return sslContext.getSocketFactory();

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }


    public abstract void parseData(Document document);
    public abstract void onDataReceived();

    @Override
    public void run() {
        doc = receiveDocument();
        parseData(doc);
        activity.runOnUiThread(this::onDataReceived);
    }
    private class MyJavaScriptInterface {
        @JavascriptInterface
        public void handleHtml(String html) {
            Document doc = Jsoup.parse(html);
        }
    }
}
