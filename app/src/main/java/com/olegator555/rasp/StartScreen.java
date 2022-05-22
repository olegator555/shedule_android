package com.olegator555.rasp;

import Utils.GetRequest;
import Utils.UrlCreator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.IS_MAIN_ACTIVITY_VISITED;
import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.JSON_STRING_KEY;
import static Utils.AppLevelUtilsAndConstants.readFromPreferences;
import static Utils.AppLevelUtilsAndConstants.writeToPreferences;


public class StartScreen extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    private int splashDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        progressBar = findViewById(R.id.progressBar2);
        textView = findViewById(R.id.textView);
        if((Boolean) readFromPreferences(this, IS_MAIN_ACTIVITY_VISITED,
                Boolean.class)) {
            splashDuration = 100;
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Log.d("Bool", "already written, redirect");
            }, splashDuration);
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
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
            writeToPreferences(getBaseContext(), JSON_STRING_KEY, res, String.class);
            startActivity(intent);
        }
    }
}