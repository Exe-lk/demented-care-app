package com.example.dementedcare;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;


public class AIPredictionActivity extends AppCompatActivity {

    private Button aiPredictionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiprediction);
        aiPredictionButton = findViewById(R.id.btn);

        Intent intent = getIntent();
        int age = intent.getIntExtra("age",0);
        int sex = intent.getIntExtra("sex",0);
        int cp = intent.getIntExtra("cp",0);
        int trtbps = intent.getIntExtra("trtbps",0);
        int chol = intent.getIntExtra("chol",0);
        int fbs = intent.getIntExtra("fbs",0);
        int restecg = intent.getIntExtra("restecg",0);
        int thalachh = intent.getIntExtra("thalachh",0);
        int exng = intent.getIntExtra("exng",0);
//        int oldpeak = intent.getIntExtra("oldpeak",0);
        int slp = intent.getIntExtra("slp",0);
        int caa = intent.getIntExtra("caa",0);
        int thall = intent.getIntExtra("thall",0);

        // Add ValueEventListener to listen for data changes
        aiPredictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open AIPredictionActivity
                Intent intent = new Intent(AIPredictionActivity.this, PatientHealthDetails.class);
                startActivity(intent);
            }
        });
    }
}