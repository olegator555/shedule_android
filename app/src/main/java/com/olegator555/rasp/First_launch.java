package com.olegator555.rasp;

import Model.ServerAnswerModel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.olegator555.rasp.DB.DBManager;

import java.util.ArrayList;

public class First_launch extends AppCompatActivity {
    public static final String HEADER_KEY = "header_key";
    public static final String DATA_LIST_KEY = "data_list_key";
    private String selected_country;
    private String selected_region;
    private boolean isPressed = false;
    private ImageButton button;
    private DBManager dbManager;

    @Override
    public void onBackPressed() {
        if (!isPressed) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        dbManager = new DBManager(this);
        dbManager.openDB();
        ArrayList<ServerAnswerModel> modelList = dbManager.getStationsList(null);
        ArrayList<String> countryArrayList = new ArrayList<>();
        modelList.forEach(element-> {
            if (!countryArrayList.contains(element.getCountry())) {
                countryArrayList.add(element.getCountry());
            }
        });
        First_launch_fragment fragment = First_launch_fragment.newInstance("Введите страну",
                countryArrayList);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        button = findViewById(R.id.imageButton);
        button.setOnClickListener(view -> {
            isPressed = true;
            selected_country = fragment.get_field_data();
            ArrayList<String> regionArrayList = dbManager.getTitleList(selected_country);
            First_launch_fragment fragment2 = First_launch_fragment.newInstance("Введите регион",regionArrayList);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment2).commit();
            button.setOnClickListener(view1 -> {
                selected_region = fragment2.get_field_data();
                Intent intent = new Intent(First_launch.this, MainActivity.class);
                intent.putExtra("Country", selected_country);
                intent.putExtra("Region", selected_region);
                startActivity(intent);
            });
        });
    }

}