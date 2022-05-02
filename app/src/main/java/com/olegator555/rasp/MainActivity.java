package com.olegator555.rasp;

import Model.Date;
import Model.ServerAnswerModel;
import Utils.AppLevelUtilsAndConstants;
import Utils.OnSwipeListener;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.gson.Gson;
import com.olegator555.rasp.DB.DBManager;

import java.util.ArrayList;
import java.util.Calendar;

import static Utils.AppLevelUtilsAndConstants.IntentKeys.*;
import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.*;

public class MainActivity extends AppCompatActivity {
    private final String DEPARTURE_TEXT_VIEW = "DepartureTextView";
    private final String DESTINATION_TEXT_VIEW = "DestinationTextView";
    private String json_string;
    private boolean isRedirectedToSuggestionsList = false;
    private TextView from_editText;
    private TextView to_edit_text;
    private TextView date_editText;
    private TextView test_field;
    private Button find_button;
    private ConstraintLayout constraintLayout;
    private DBManager dbManager;
    private ArrayList<ServerAnswerModel> model_list;
    private ServerAnswerModel selected_departure_item;
    private ServerAnswerModel selected_destination_item;
    private Date date;

    @Override
    public void onBackPressed() {
        AppLevelUtilsAndConstants.emulateHomePressed(this);
}

    @Override
    protected void onResume() {
        super.onResume();
        if(isRedirectedToSuggestionsList) {
            isRedirectedToSuggestionsList = false;
            setTextViewWithSelectedElement(DEPARTURE_STATION_GSON_STRING, DEPARTURE_TEXT_VIEW);
            setTextViewWithSelectedElement(DESTINATION_STATION_GSON_STRING, DESTINATION_TEXT_VIEW);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!(Boolean)AppLevelUtilsAndConstants.readFromPreferences(this, IS_MAIN_ACTIVITY_VISITED,Boolean.class)) {
            AppLevelUtilsAndConstants.writeToPreferences(this,IS_MAIN_ACTIVITY_VISITED, true,
                    Boolean.class);
            Log.d("Saved", "Written to sp");
        }
        from_editText = findViewById(R.id.fromTextView);
        to_edit_text = findViewById(R.id.toTextView);
        date_editText = findViewById(R.id.dateEditText);
        test_field = findViewById(R.id.test_field);
        find_button = findViewById(R.id.show_result_button);
        date_editText.setOnClickListener(this::findButton);
        find_button.setOnClickListener(this::findButton);
        date_editText.setOnClickListener(this::showDateDialog);
        Calendar calendar = Calendar.getInstance();
        date = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));

        dbManager = new DBManager(this);
        selected_departure_item = new ServerAnswerModel();
        selected_destination_item = new ServerAnswerModel();
        model_list = dbManager.getFromDb();
        from_editText.setOnClickListener(this::onTextViewClickListener);
        to_edit_text.setOnClickListener(this::onTextViewClickListener);
        setTextViewWithSelectedElement(DEPARTURE_STATION_GSON_STRING, DEPARTURE_TEXT_VIEW);
        setTextViewWithSelectedElement(DESTINATION_STATION_GSON_STRING, DESTINATION_TEXT_VIEW);

        constraintLayout = findViewById(R.id.main_constraint_layout);
        constraintLayout.setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeRight() {
                showToast("swipe to bottom");
            }

            @Override
            public void onSwipeLeft() {
                showToast("swipe to bottom");
            }

            @Override
            public void onSwipeTop() {
                showToast("swipe to bottom");
            }

            @Override
            public void onSwipeBottom() {
                showToast("yes");
            }
        });

    }
    private void setTextViewWithSelectedElement(String json_key, String textViewName) {
        if((AppLevelUtilsAndConstants.readFromPreferences(this,json_key, String.class)
                != null)) {
            json_string = (String)AppLevelUtilsAndConstants.readFromPreferences(this,json_key, String.class);
            Gson gson = new Gson();
            switch (textViewName) {
                case DEPARTURE_TEXT_VIEW:
                    selected_departure_item = gson.fromJson(json_string, ServerAnswerModel.class);
                    from_editText.setText(selected_departure_item.getStation_name());
                    break;
                case DESTINATION_TEXT_VIEW:
                    selected_destination_item= gson.fromJson(json_string, ServerAnswerModel.class);
                    to_edit_text.setText(selected_destination_item.getStation_name());
                    break;
            }

        }
    }
    @SuppressLint("NonConstantResourceId")
    private void onTextViewClickListener(View view) {
        Intent intent = new Intent(this,StationSelectionActivityActivity.class);
        intent.putExtra("models_list", model_list);
        intent.putExtra("caller_id", view.getId());
        isRedirectedToSuggestionsList = true;
        startActivity(intent);
    }

    private void findButton(View view)  {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(DEPARTURE_ITEM, selected_departure_item);
        intent.putExtra(DESTINATION_ITEM, selected_destination_item);
        intent.putExtra(DATE_ITEM,date);
        startActivity(intent);
    }
    private void showDateDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        final int YEAR = calendar.get(Calendar.YEAR);
        final int MONTH = calendar.get(Calendar.MONTH);
        final int DAY = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                date = new Date(year, month, day);
                date_editText.setText(date.toString());
                findButton(view);
            }
        },YEAR, MONTH, DAY);
        datePickerDialog.show();
    }
    public void showToast( String message ){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    private void swap(ServerAnswerModel model1, ServerAnswerModel model2) {
        ServerAnswerModel tmp = model1.clone();
        model1 = model2;
        model2 = tmp;
    }

}