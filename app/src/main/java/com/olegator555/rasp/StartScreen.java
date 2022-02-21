package com.olegator555.rasp;

import Utils.AppLevelUtilsAndConstants;
import Utils.GetRequest;
import Utils.UrlCreator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.IS_MAIN_ACTIVITY_VISITED;
import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.JSON_STRING_KEY;


public class StartScreen extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        textView = findViewById(R.id.textView);
        if((Boolean)AppLevelUtilsAndConstants.readFromPreferences(this, IS_MAIN_ACTIVITY_VISITED,
                Boolean.class)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Log.d("Bool", "already written, redirect");
        }
        else {
            Log.d("Bool", "Not written yet");
            GetRequest stations_list_request = new StationLisRequest();
            stations_list_request.execute(new UrlCreator().getStationsListUrl());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("StaticFieldLeak")
    private class StationLisRequest extends GetRequest {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Intent intent = new Intent(StartScreen.this, First_launch.class);
            AppLevelUtilsAndConstants.writeToPreferences(getBaseContext(), JSON_STRING_KEY, res, String.class);
            startActivity(intent);
        }
    }
}