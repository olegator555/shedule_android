package com.olegator555.rasp;

import Model.ScheduleModel;
import Model.StationInRouteModel;
import Utils.AsyncDataReceiver;
import Utils.UrlCreator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.olegator555.rasp.Adapter.RouteStationsRecyclerViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityScheduleElementSelected extends AppCompatActivity {
    private TextView textView;
    private ProgressBar progressBar;
    private ImageButton backButton;
    private String number;
    private String title;
    private String uid;
    private String days;
    private RecyclerView routeStationsRecyclerView;
    private ArrayList<StationInRouteModel> routeStationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_element_selected);
        Gson gson = new Gson();
        String serializedString = getIntent().getStringExtra("SelectedElement");
        ScheduleModel model = gson.fromJson(serializedString, ScheduleModel.class);
        textView = findViewById(R.id.headerTextView);
        progressBar = findViewById(R.id.progressBar3);
        backButton = findViewById(R.id.backButton);
        routeStationsRecyclerView = findViewById(R.id.routeStationsRecyclerView);
        routeStationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        String url = new UrlCreator(model.getUid()).getRouteUrl();
        Log.d("Uid url", url);
        new GetJsonObject(url, ActivityScheduleElementSelected.this).start();
        backButton.setOnClickListener(view -> onBackPressed());
    }
    private void onDataReceived() {
        progressBar.setVisibility(View.GONE);
        textView.setText(title);
        textView.setVisibility(View.VISIBLE);
        routeStationsRecyclerView.setAdapter(new RouteStationsRecyclerViewAdapter(routeStationsList));
        routeStationsRecyclerView.setVisibility(View.VISIBLE);
    }

    class GetJsonObject extends AsyncDataReceiver {

        public GetJsonObject(String url, Activity activity) {
            super(url, activity);
        }

        @Override
        public void parseJson(JSONObject receivedObject) {
            if(receivedObject==null)
                throw new RuntimeException("Bad request to api");
            try {
                number = receivedObject.getString("number");
                title = receivedObject.getString("title");
                uid = receivedObject.getString("uid");
                days = receivedObject.getString("days");
                final JSONArray stops = receivedObject.getJSONArray("stops");
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
        public void onThreadCompleted() {
            onDataReceived();
        }
    }
}