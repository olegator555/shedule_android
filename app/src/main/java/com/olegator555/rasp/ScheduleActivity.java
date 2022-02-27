package com.olegator555.rasp;

import Model.Date;
import Model.ScheduleModel;
import Model.ServerAnswerModel;
import Utils.UrlCreator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.olegator555.rasp.Adapter.ScheduleListAdapter;
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
    private ArrayList<ScheduleModel> scheduleModels = new ArrayList<>();
    private ArrayList<ScheduleModel> tempList = new ArrayList<>();
    private ScheduleListAdapter adapter = new ScheduleListAdapter(scheduleModels);

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        suburbans_list.setLayoutManager(linearLayoutManager);
        suburbans_list.setAdapter(adapter);
        linearLayoutManager.scrollToPositionWithOffset(15,0);

        Log.d("Codes: ", departure_item.getYandex_code() + " - " + destination_item.getYandex_code());
        date_text_view.setText(date.toString());
        header_text_view.setText(MessageFormat.format("{0} - {1}", departure_item.getStation_name(),
                destination_item.getStation_name()));
        requestQueue = Volley.newRequestQueue(this);
        Log.d("url", new UrlCreator(departure_item.getYandex_code(), destination_item.getYandex_code(), date)
                .getScheduleUrl());
        getSchedule(new UrlCreator(departure_item.getYandex_code(), destination_item.getYandex_code(),date)
                .getScheduleUrl());
        progressBar.setVisibility(View.GONE);
        suburbans_list.setVisibility(View.VISIBLE);
        Log.d("Models size", String.valueOf(scheduleModels.size()));

    }

    private void getSchedule(String url) {
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
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
                    String title = threadObject.getString("title"); // Откуда - куда идет электричка
                    JSONObject transportSubtypeObject = threadObject.getJSONObject("transport_subtype");
                    String type_title = transportSubtypeObject.getString("title");
                    tempList.add(new ScheduleModel(departure, departure_platform, arrival, arrival_platform,
                            duration, stops, number, title, type_title));
                    Log.d("Size from thread", String.valueOf(scheduleModels.size()));

                }
                scheduleModels.clear();
                scheduleModels.addAll(tempList);
                adapter.updateModelList(scheduleModels);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

}