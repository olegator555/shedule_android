package com.olegator555.rasp;

import Model.Date;
import Model.ScheduleModel;
import Model.ServerAnswerModel;
import Utils.UrlCreator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

import static Utils.AppLevelUtilsAndConstants.IntentKeys.*;

public class ScheduleActivity extends AppCompatActivity {
    private ServerAnswerModel departure_item;
    private ServerAnswerModel destination_item;
    private Date date;
    private TextView header_text_view;
    private TextView date_text_view;
    private TextView service_text_view;
    private RecyclerView suburbans_list;
    private ProgressBar progressBar;
    private String json_string;
    private RequestQueue requestQueue;
    private ArrayList<ScheduleModel> scheduleModels;

    public synchronized void setJson_string(String json_string) {
        this.json_string = json_string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        departure_item = getIntent().getParcelableExtra(DEPARTURE_ITEM);
        destination_item = getIntent().getParcelableExtra(DESTINATION_ITEM);
        date = getIntent().getParcelableExtra(DATE_ITEM);

        header_text_view = findViewById(R.id.ScheduleHeader);
        date_text_view = findViewById(R.id.schedule_date);
        service_text_view = findViewById(R.id.serviceTextView);
        suburbans_list = findViewById(R.id.scheduleRecyclerView);
        progressBar = findViewById(R.id.scheduleProgressBar);
        service_text_view.setVisibility(View.GONE);

        Log.d("Codes: ", departure_item.getYandex_code() + " - " + destination_item.getYandex_code());
        date_text_view.setText(date.toString());
        header_text_view.setText(MessageFormat.format("{0} - {1}", departure_item.getStation_name(),
                destination_item.getStation_name()));
        requestQueue = Volley.newRequestQueue(this);
        Log.d("url", new UrlCreator(departure_item.getYandex_code(), destination_item.getYandex_code(), date).getScheduleUrl());


    }
    private void getSchedule(String url) {
        scheduleModels = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray segmentsArray = response.getJSONArray("segments");
                    for(int i= 0; i<segmentsArray.length(); i++) {
                        JSONObject trainInfo = segmentsArray.getJSONObject(i);
                        String departure = trainInfo.getString("departure");
                        String departure_platform = trainInfo.getString("departure_platform");
                        String arrival = trainInfo.getString("arrival");
                        String arrival_platform = trainInfo.getString("arrival_platform");
                        int duration = trainInfo.getInt("duration");
                        String stops = trainInfo.getString("stops");
                        JSONObject threadObject = trainInfo.getJSONObject("thread");
                        String number = threadObject.getString("number");
                        String title = threadObject.getString("title");
                        JSONObject transportSubtypeObject = threadObject.getJSONObject("transport_subtype");
                        String type_code = transportSubtypeObject.getString("code");
                        scheduleModels.add(new ScheduleModel(departure, departure_platform, arrival, arrival_platform,
                                duration, stops, number, title, type_code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

}