package com.olegator555.rasp;

import Utils.AsyncDataReceiver;
import Utils.UrlCreator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.IS_MAIN_ACTIVITY_VISITED;
import static Utils.AppLevelUtilsAndConstants.readFromPreferences;


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
            new AsyncDataReceiver(new UrlCreator().getStationsListUrl(), this) {
                @Override
                public void parseJson(String receivedObject) {
                    Log.d("GET", "VISITED");
                    /*Gson gson = new Gson();
                    Type listType = new TypeToken<List<ServerAnswerModel>>(){}.getType();
                    List<ServerAnswerModel> models = gson.fromJson(receivedObject, listType);
                    new AsyncDBWriter(StartScreen.this, AsyncDBWriter.INSERT, ServerAnswerModel.class).start();*/
                }

                @Override
                public void onThreadCompleted() {
                    Intent intent = new Intent(StartScreen.this, First_launch.class);
                    startActivity(intent);
                }
            };
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.setVisibility(View.GONE);
    }

}