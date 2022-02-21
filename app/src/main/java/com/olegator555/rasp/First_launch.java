package com.olegator555.rasp;

import Utils.AppLevelUtilsAndConstants;
import Utils.JsonParser;
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
        String jsonString = (String) AppLevelUtilsAndConstants.readFromPreferences(this,
                AppLevelUtilsAndConstants.PreferencesKeys.JSON_STRING_KEY, String.class);
        JsonParser jsonParser = new JsonParser(jsonString, this);
        ArrayList<String> countryArrayList = jsonParser.getCountriesList();
        First_launch_fragment fragment = First_launch_fragment.newInstance("Введите страну",
                countryArrayList);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

        button = findViewById(R.id.imageButton);
        button.setOnClickListener(view -> {
            isPressed = true;
            selected_country = fragment.get_field_data();
            ArrayList<String> regionArrayList = jsonParser.getRegionsList(selected_country);
            First_launch_fragment fragment2 = First_launch_fragment.newInstance("Введите регион",regionArrayList);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment2).commit();
            button.setOnClickListener(view1 -> {
                selected_region = fragment2.get_field_data();
                jsonParser.writeToDb(selected_country, selected_region);
                Intent intent = new Intent(First_launch.this, MainActivity.class);
                startActivity(intent);
            });
        });
    }

}