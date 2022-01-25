package com.olegator555.rasp;

import Model.ServerAnswerModel;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class First_launch extends AppCompatActivity {
    public static final String HEADER_KEY = "header_key";
    public static final String DATA_LIST_KEY = "data_list_key";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        ArrayList<ServerAnswerModel> list = getIntent().getParcelableArrayListExtra("Response");
        ArrayList<String> stringArrayList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (ServerAnswerModel object:list
        ) {
            stringArrayList.add(object.getCountry());
        }
        First_launch_fragment fragment = new First_launch_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();


    }


}