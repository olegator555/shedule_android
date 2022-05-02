package com.olegator555.rasp;

import Model.ScheduleModel;
import Model.StationInRouteModel;
import Utils.UrlCreator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityScheduleElementSelected extends AppCompatActivity {
    private TextView textView;
    private ProgressBar progressBar;
    private JSONObject result;
    private ImageButton backButton;
    private String number;
    private String title;
    private String uid;
    private String days;
    private List<StationInRouteModel> routeStationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_element_selected);
        ScheduleModel model = getIntent().getParcelableExtra("SelectedElement");
        textView = findViewById(R.id.headerTextViev);
        progressBar = findViewById(R.id.progressBar3);
        backButton = findViewById(R.id.backButton);
        String url = new UrlCreator(model.getUid()).getRouteUrl();
        Log.d("Uid url", url);
        new GetJsonObject(url).start();
        backButton.setOnClickListener(view -> onBackPressed());
    }
    private void onThreadCompleted() {
        progressBar.setVisibility(View.GONE);
        textView.setText(title);
        textView.setVisibility(View.VISIBLE);
    }

    class GetJsonObject extends Thread {
        private final String url;

        GetJsonObject(String url) {
            this.url = url;
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
        private void parseJson() {
            try {
                number = result.getString("number");
                title = result.getString("title");
                uid = result.getString("uid");
                days = result.getString("days");
                final JSONArray stops = result.getJSONArray("stops");
                routeStationsList = new ArrayList<>();
                for(int i=0;i<stops.length(); i++) {
                    final JSONObject current_stop = stops.getJSONObject(i);
                    final String arrival = current_stop.getString("arrival");
                    final String departure = current_stop.getString("departure");
                    final int duration = current_stop.getInt("duration");
                    final String platform = current_stop.getString("platform");
                    final JSONObject station = current_stop.getJSONObject("station");
                    final String station_code = station.getString("code");
                    final String station_title = station.getString("title");
                    routeStationsList.add(new StationInRouteModel(arrival, departure,duration, platform, station_code,
                            station_title));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            result = getJsonObject(url);
            parseJson();
            runOnUiThread(ActivityScheduleElementSelected.this::onThreadCompleted);
        }
    }
}