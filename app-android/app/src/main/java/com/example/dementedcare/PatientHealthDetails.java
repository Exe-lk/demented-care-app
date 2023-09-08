package com.example.dementedcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PatientHealthDetails extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView tempTextView;
    private TextView bloodPressureTextView;
    private TextView heartRateTextView;
    private TextView bloodO2TextView;

    private EditText nicNumberEditText;
    private Button addDataButton;
    private FirebaseFirestore firestore;
    private Double bodyTemperatureDouble;
    private Double bloodPressureDouble;
    private Double heartRateDouble;
    private Double bloodO2Double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_health_details);

        firestore = FirebaseFirestore.getInstance();
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("health_device");

        // Initialize TextView elements
        tempTextView = findViewById(R.id.temp);
        bloodPressureTextView = findViewById(R.id.bloodox);
        heartRateTextView = findViewById(R.id.heartrate);
        bloodO2TextView = findViewById(R.id.bloodox2);
        Button aiPredictionButton = findViewById(R.id.aibtn);

        nicNumberEditText = findViewById(R.id.nicNumberEditText);
        addDataButton = findViewById(R.id.addDataButton);

        // Add ValueEventListener to listen for data changes

        aiPredictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open AIPredictionActivity
                Intent intent = new Intent(PatientHealthDetails.this, predictform.class);
                startActivity(intent);
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve data for each health parameter
                bodyTemperatureDouble = dataSnapshot.child("body_temperature").getValue(Double.class);
                bloodPressureDouble = dataSnapshot.child("blood_pressure").getValue(Double.class);
                heartRateDouble = dataSnapshot.child("heart_rate").getValue(Double.class);
                bloodO2Double = dataSnapshot.child("blood_oxygen_level").getValue(Double.class);

                // Convert Double values to String
                String bodyTemperature = String.valueOf(bodyTemperatureDouble);
                String bloodPressure = String.valueOf(bloodPressureDouble);
                String heartRate = String.valueOf(heartRateDouble);
                String bloodO2 = String.valueOf(bloodO2Double);

                // Update TextView elements with retrieved data
                tempTextView.setText(bodyTemperature);
                bloodPressureTextView.setText(bloodPressure);
                heartRateTextView.setText(heartRate);
                bloodO2TextView.setText(bloodO2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });

        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the NIC number entered by the user
                String patientId = nicNumberEditText.getText().toString().trim(); // Assuming patientId is the same as NIC number

                // Check if patientId is not empty
                if (!patientId.isEmpty()) {
                    // Create a map to store the data
                    Map<String, Object> data = new HashMap<>();
                    data.put("bodyTemperature", bodyTemperatureDouble);
                    data.put("bloodPressure", bloodPressureDouble);
                    data.put("heartRate", heartRateDouble);
                    data.put("bloodO2", bloodO2Double);
                    data.put("timestamp", FieldValue.serverTimestamp()); // Real-time timestamp
                    data.put("patientId", patientId);

                    // Store data in Firestore
                    firestore.collection("patient_past_data") // Collection name is "patient_past_data"
                            .document(patientId) // Document name is the patientId
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Data added successfully
                                    Toast.makeText(PatientHealthDetails.this, "Data added to Firestore", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error handling
                                    Toast.makeText(PatientHealthDetails.this, "Failed to add data to Firestore", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // NIC number is empty, show an error message
                    Toast.makeText(PatientHealthDetails.this, "Please enter patientId", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Helper method to get the current timestamp as a string
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
