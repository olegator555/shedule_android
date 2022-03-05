package com.olegator555.rasp;

import Model.ScheduleModel;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityScheduleElementSelected extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_element_selected);
        ScheduleModel model = getIntent().getParcelableExtra("SelectedElement");
        textView = findViewById(R.id.headerTextViev);
        textView.setText(String.format("%s - %s", model.getDeparture(), model.getArrival()));
    }
}