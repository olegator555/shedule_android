package com.olegator555.rasp;

import Model.Date;
import Model.ServerAnswerModel;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.olegator555.rasp.ui.ScheduleFragment;

import java.text.MessageFormat;

import static Utils.AppLevelUtilsAndConstants.IntentKeys.*;

public class ScheduleActivity extends AppCompatActivity {
    private ServerAnswerModel departure_item;
    private ServerAnswerModel destination_item;
    private Date date;
    private TextView header_text_view;
    private Fragment straightFragment;
    private Fragment backwardFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        departure_item = getIntent().getParcelableExtra(DEPARTURE_ITEM);
        destination_item = getIntent().getParcelableExtra(DESTINATION_ITEM);
        date = getIntent().getParcelableExtra(DATE_ITEM);

        header_text_view = findViewById(R.id.ScheduleHeader);
        header_text_view.setText(MessageFormat.format("{0} - {1}", departure_item.getStation_name(),
                destination_item.getStation_name()));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(navbarListener);
        straightFragment = new ScheduleFragment(departure_item, destination_item, date, this);
        getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, straightFragment).commit();
    }

    private NavigationBarView.OnItemSelectedListener navbarListener = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    header_text_view.setText(MessageFormat.format("{0} - {1}", departure_item.getStation_name(),
                            destination_item.getStation_name()));
                    if(straightFragment==null)
                        straightFragment = new ScheduleFragment(departure_item, destination_item, date, ScheduleActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, straightFragment)
                            .commit();
                    break;
                case R.id.navigation_backwards:
                    header_text_view.setText(MessageFormat.format("{0} - {1}", destination_item.getStation_name(),
                            departure_item.getStation_name()));
                    if(backwardFragment==null)
                        backwardFragment = new ScheduleFragment(destination_item, departure_item, date, ScheduleActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, backwardFragment)
                            .commit();
                    break;
            }
            return true;
        }
    };



}