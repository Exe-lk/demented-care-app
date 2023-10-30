package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Open_activity extends AppCompatActivity {

    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        nameTextView = findViewById(R.id.textView);

        String name = getIntent().getStringExtra("keyname");
        nameTextView.setText(name); // Set the text of the TextView
    }
}
