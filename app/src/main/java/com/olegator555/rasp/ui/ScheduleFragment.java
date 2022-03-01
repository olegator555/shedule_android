package com.olegator555.rasp.ui;

import Model.Date;
import Model.ScheduleModel;
import Model.ServerAnswerModel;
import Utils.UrlCreator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.olegator555.rasp.Adapter.ScheduleListAdapter;
import com.olegator555.rasp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    private RecyclerView recyclerView;
    private ServerAnswerModel departureItem;
    private ServerAnswerModel destinationItem;
    private Date dateItem;
    private RequestQueue requestQueue;
    private int listPosition;
    private ArrayList<ScheduleModel> tempList;
    private ArrayList<ScheduleModel> scheduleModels;
    private ScheduleListAdapter adapter;

    public ScheduleFragment(ServerAnswerModel departureItem, ServerAnswerModel destinationItem, Date dateItem) {
        this.departureItem = departureItem;
        this.destinationItem = destinationItem;
        this.dateItem = dateItem;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        recyclerView = view.findViewById(R.id.scheduleRecyclerView);
        scheduleModels = new ArrayList<>();
        tempList = new ArrayList<>();
        adapter = new ScheduleListAdapter(scheduleModels);
        requestQueue = Volley.newRequestQueue(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        getSchedule(new UrlCreator(departureItem.getYandex_code(), destinationItem.getYandex_code(),dateItem)
                .getScheduleUrl());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void getSchedule(String url) {
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, response -> {
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
                listPosition = adapter.updateModelList(scheduleModels);
                recyclerView.scrollToPosition(listPosition);
                Log.d("Position", String.valueOf(listPosition));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(request);
    }
}
