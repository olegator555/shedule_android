package com.olegator555.rasp;

import Model.ServerAnswerModel;
import Utils.OnSwipeListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.olegator555.rasp.DB.DBManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView from_editText;
    private AutoCompleteTextView to_edit_text;
    private AutoCompleteTextView date_editText;
    private TextView test_field;
    private Button find_button;
    private ConstraintLayout constraintLayout;
    private DBManager dbManager;

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

        from_editText = findViewById(R.id.from_editText);
        to_edit_text = findViewById(R.id.to_editText);
        date_editText = findViewById(R.id.editTextDate);
        test_field = findViewById(R.id.test_field);
        find_button = findViewById(R.id.show_result_button);
        date_editText.setOnClickListener(this::setFind_button);
        find_button.setOnClickListener(this::setFind_button);

        String selected_country = getIntent().getStringExtra("Country");
        String selected_region = getIntent().getStringExtra("Region");
        dbManager = new DBManager(this);
        dbManager.openDB();
        ArrayList<ServerAnswerModel> model_list = dbManager.getStationsList(new String[] {selected_country,
                selected_region});
        dbManager.closeDB();
        ArrayList<String> addapter_list = new ArrayList<>();
        model_list.forEach(element -> addapter_list.add(element.getStation_name() + "\n" + element.getDirection()));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                addapter_list);
        to_edit_text.setAdapter(arrayAdapter);
        from_editText.setAdapter(arrayAdapter); 

        constraintLayout = findViewById(R.id.main_constraint_layout);
        constraintLayout.setOnTouchListener(new OnSwipeListener(this){
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
    public void setFind_button(View view)  {

    }
    public void showToast( String message ){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}