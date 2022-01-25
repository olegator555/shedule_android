package com.olegator555.rasp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText from_editText;
    private EditText to_edit_text;
    private AutoCompleteTextView date_editText;
    private TextView test_field;
    private Button find_button;
    ProgressBar progressBar;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        from_editText = findViewById(R.id.from_editText);
        to_edit_text = findViewById(R.id.to_editText);
        date_editText = findViewById(R.id.editTextDate);
        test_field = findViewById(R.id.test_field);



        String[] list_dates = getResources().getStringArray(R.array.date_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_main, list_dates);
        date_editText.setAdapter(adapter);

        find_button = findViewById(R.id.show_result_button);
        test_field = findViewById(R.id.test_field);
        date_editText.setOnClickListener(this::setFind_button);
        find_button.setOnClickListener(this::setFind_button);


    }
    public void setFind_button(View view)  {

    }


}