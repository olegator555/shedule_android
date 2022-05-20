package Utils;

import android.app.Activity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AsyncDataReceiver extends Thread {
    private final String url;
    private final Activity activity;

    protected AsyncDataReceiver(String url, Activity activity) {
        this.url = url;
        this.activity = activity;
    }


    private JSONObject getJsonObject(String str_url) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str=bufferedReader.readLine())!=null)
                sb.append(str).append('\n');
            return new JSONObject(sb.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection !=null) {
                connection.disconnect();
            }
            if (bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public abstract void parseJson(JSONObject receivedObject);
    public abstract void onThreadCompleted();
    @Override
    public void run() {
        JSONObject receivedObject = getJsonObject(url);
        parseJson(receivedObject);
        activity.runOnUiThread(this::onThreadCompleted);
    }
}
