package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class face_results extends AppCompatActivity {
    private Button button;
    private EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_results);

        button = findViewById(R.id.btn);
//        result = findViewById(R.id.result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the new activity to open
                Intent intent = new Intent(face_results.this, Videorecorderactivity.class); // Replace NewActivity.class with the actual activity you want to open

                // Start the new activity
                startActivity(intent);
            }
        });
    }
}
