package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class pair_health_device extends AppCompatActivity {

    private EditText name;
    private Button add;

    private TextView deviceIdTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_health_device);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize your TextView from your XML layout
        deviceIdTextView = findViewById(R.id.deviceIdTextView);

        // Query the "device_id" field from the database
        databaseReference.child("health_checking_device").child("HLD001").child("device_id")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String deviceId = dataSnapshot.getValue(String.class);
                        if (deviceId != null) {
                            deviceIdTextView.setText(deviceId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors here
                    }
                });
    }
}