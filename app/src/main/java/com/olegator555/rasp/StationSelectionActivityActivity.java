package com.olegator555.rasp;

import Model.ServerAnswerModel;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.olegator555.rasp.Adapter.StationsRecyclerViewAdapter;

import java.util.ArrayList;

import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.DEPARTURE_STATION_GSON_STRING;
import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.DESTINATION_STATION_GSON_STRING;

public class StationSelectionActivityActivity extends AppCompatActivity {

    private int caller_id;
    private String preferencesKey;
    private String header;
    private ArrayList<ServerAnswerModel> model_list;
    private RecyclerView suggestionsRecyclerView;
    private EditText headerEditText;
    private StationsRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_selection_activity);
        caller_id = getIntent().getIntExtra("caller_id", 0);
        switch (caller_id) {
            case R.id.fromTextView:
                header = "Откуда";
                preferencesKey = DEPARTURE_STATION_GSON_STRING;
                break;
            case R.id.toTextView:
                header = "Куда";
                preferencesKey = DESTINATION_STATION_GSON_STRING;
                break;
            default:break;
        }
        model_list = getIntent().getParcelableArrayListExtra("models_list");
        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        headerEditText = findViewById(R.id.headerEditText);
        headerEditText.setHint(header);
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StationsRecyclerViewAdapter(model_list, preferencesKey);
        suggestionsRecyclerView.setAdapter(adapter);
        Log.d("ModelListSize", String.valueOf(model_list.size()));
        headerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterResults(editable.toString());
            }
        });


    }
    private void filterResults(String s) {
        ArrayList<ServerAnswerModel> filteredList = new ArrayList<>();
        for (ServerAnswerModel element : model_list) {
            if(element.getStation_name().toLowerCase().trim().contains(s.toLowerCase().trim()))
                filteredList.add(element);
        }
        adapter.filterList(filteredList);

    }
}