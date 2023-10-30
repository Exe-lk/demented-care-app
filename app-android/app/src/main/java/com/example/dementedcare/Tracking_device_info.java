package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Tracking_device_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_device_info);

        // Retrieve the device ID from the intent
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("device_id");

        // Use the device ID as needed in your activity
        // For example, you can set it to a TextView
        TextView deviceIdTextView = findViewById(R.id.text_device_id);
        deviceIdTextView.setText("Device ID: " + deviceId);
    }
}


