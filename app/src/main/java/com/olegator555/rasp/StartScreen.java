package com.olegator555.rasp;

import Utils.GetRequest;
import Utils.JsonParser;
import Utils.UrlCreator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    @SuppressLint("StaticFieldLeak")
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
            try {
                JSONObject top_levelObject = new JSONObject(res);
                JSONArray jsonArray = top_levelObject.getJSONArray("countries");
                JsonParser jsonParser = new JsonParser(jsonArray, getBaseContext());
                jsonParser.write_to_db();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(intent);

        }
    }
}