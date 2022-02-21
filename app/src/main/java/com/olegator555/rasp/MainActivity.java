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
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.olegator555.rasp.Adapter.AutoCompleteStationAdapter;
import com.olegator555.rasp.DB.DBManager;

import java.util.ArrayList;
import java.util.Calendar;

import static Utils.AppLevelUtilsAndConstants.IntentKeys.*;
import static Utils.AppLevelUtilsAndConstants.PreferencesKeys.IS_MAIN_ACTIVITY_VISITED;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView from_editText;
    private AutoCompleteTextView to_edit_text;
    private TextView date_editText;
    private TextView test_field;
    private Button find_button;
    private ConstraintLayout constraintLayout;
    private DBManager dbManager;
    private ServerAnswerModel selected_departure_item;
    private ServerAnswerModel selected_destination_item;
    private Date date;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
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
        from_editText = findViewById(R.id.from_editText);
        to_edit_text = findViewById(R.id.to_editText);
        date_editText = findViewById(R.id.editTextDate);
        test_field = findViewById(R.id.test_field);
        find_button = findViewById(R.id.show_result_button);
        date_editText.setOnClickListener(this::findButton);
        find_button.setOnClickListener(this::findButton);
        date_editText.setOnClickListener(this::showDateDialog);


        dbManager = new DBManager(this);
        selected_departure_item = new ServerAnswerModel();
        selected_destination_item = new ServerAnswerModel();
        ArrayList<ServerAnswerModel> model_list = dbManager.getFromDb();
        AutoCompleteStationAdapter adapter = new AutoCompleteStationAdapter(this, model_list);
        to_edit_text.setAdapter(adapter);
        from_editText.setAdapter(adapter);
        from_editText.setOnItemClickListener((adapterView, view, i, l) ->
                selected_departure_item = ((ServerAnswerModel) adapterView.getItemAtPosition(i)));
        to_edit_text.setOnItemClickListener((adapterView, view, i, l) ->
                selected_destination_item = ((ServerAnswerModel) adapterView.getItemAtPosition(i)));

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