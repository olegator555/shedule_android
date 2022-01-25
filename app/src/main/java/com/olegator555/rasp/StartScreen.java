package com.olegator555.rasp;

import Model.ServerAnswerModel;
import Utils.GetRequest;
import Utils.UrlCreator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StartScreen extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        progressBar = findViewById(R.id.progressBar2);
        textView = findViewById(R.id.textView);
        GetRequest stations_list_request = new StationLisRequest();
        stations_list_request.execute(new UrlCreator().getStationsListUrl());
    }
    private class StationLisRequest extends GetRequest {

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(StartScreen.this, First_launch.class);
            ArrayList<ServerAnswerModel> stations_list = new ArrayList<>();
            try {
                JSONObject top_levelObject = new JSONObject(res);
                JSONArray jsonArray = top_levelObject.getJSONArray("countries");
                for(int i=0; i<jsonArray.length();i++){
                    String country = jsonArray.getJSONObject(i).getString("title");
                    ServerAnswerModel serverAnswerModel = new ServerAnswerModel();
                    serverAnswerModel.setCountry(country);
                    stations_list.add(serverAnswerModel);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent.putExtra("Response", stations_list);
            startActivity(intent);

        }
    }
}