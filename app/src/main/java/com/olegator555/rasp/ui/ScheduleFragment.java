package com.olegator555.rasp.ui;

import Model.Date;
import Model.ScheduleModel;
import Model.ServerAnswerModel;
import Utils.AsyncDataReceiver;
import Utils.UrlCreator;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private ArrayList<ScheduleModel> scheduleModels;
    private ScheduleListAdapter adapter;
    private final Activity currentActivity;
    private ProgressBar progressBar;
    private java.util.Date currentTime;

    public ScheduleFragment(ServerAnswerModel departureItem, ServerAnswerModel destinationItem, Date dateItem, Activity currentActivity) {
        this.departureItem = departureItem;
        this.destinationItem = destinationItem;
        this.dateItem = dateItem;
        this.currentActivity = currentActivity;
        initializeList();
    }

    public void initializeList() {
        String url = new UrlCreator(departureItem.getYandex_code(), destinationItem.getYandex_code(),dateItem)
                .getScheduleUrl();
        scheduleModels = new ArrayList<>();
        new AsyncDataReceiver(url, currentActivity) {
            @Override
            public void parseJson(String receivedObject) {
                try {
                    JSONObject jsonObject = new JSONObject(receivedObject);
                    JSONArray segmentsArray = jsonObject.getJSONArray("segments");
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
                        String uid = threadObject.getString("uid");
                        JSONObject transportSubtypeObject = threadObject.getJSONObject("transport_subtype");
                        String type_title = transportSubtypeObject.getString("title");
                        scheduleModels.add(new ScheduleModel(departure, departure_platform, arrival, arrival_platform,
                                duration, stops, number, title, type_title, uid));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onThreadCompleted() {
                onDataReceived();
            }
        }.start();
    }
    public void onDataReceived() {
        adapter = new ScheduleListAdapter(scheduleModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.scrollToPosition(createOffsetIndex());
    }
    private int createOffsetIndex() {
        int offsetIndex = 0;
        for (int i=0; i<scheduleModels.size();i++) {
            if(scheduleModels.get(i).getDeparture().getTime()>currentTime.getTime()) {
                offsetIndex = i;
                break;
            }
        }
        return offsetIndex;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        recyclerView = view.findViewById(R.id.scheduleRecyclerView);
        currentTime = new java.util.Date();
        if (adapter!=null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(createOffsetIndex());
        }
        else {
            progressBar = view.findViewById(R.id.progressBar4);
            progressBar.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
