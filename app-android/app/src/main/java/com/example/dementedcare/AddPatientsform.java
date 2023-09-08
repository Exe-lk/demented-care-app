package com.example.dementedcare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPatientsform extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patientsform);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("gps_tracking_devices"); // Replace with your database reference

        // Create a list to store the gps_device_id values
        List<String> deviceIdList = new ArrayList<>();

        // Attach a ValueEventListener to fetch the data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming your data structure is consistent, get the gps_device_id value
                    String deviceId = snapshot.child("gps_device_id").getValue(String.class);
                    if (deviceId != null && !deviceId.isEmpty()) {
                        deviceIdList.add(deviceId);
                    }
                }
                // Once you have retrieved the data, populate the dropdown (Spinner) in your XML layout
                Spinner gpsDeviceSpinner = findViewById(R.id.gps_device_spinner); // Replace with your Spinner ID
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPatientsform.this, android.R.layout.simple_spinner_item, deviceIdList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gpsDeviceSpinner.setAdapter(adapter);

                // Get references to the NIC EditText and Submit Button
                EditText nicEditText = findViewById(R.id.nic_edit_text);
                Button submitButton = findViewById(R.id.submit_button);

                // Set an OnClickListener for the Submit Button
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the selected GPS device ID from the Spinner
                        String selectedDeviceId = gpsDeviceSpinner.getSelectedItem().toString();

                        // Get the NIC number from the EditText
                        String nicNumber = nicEditText.getText().toString();

                        // Update the "userId" field in the Firebase Realtime Database for the selected device
                        DatabaseReference selectedDeviceReference = databaseReference.child(selectedDeviceId);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("userId", nicNumber);
                        selectedDeviceReference.updateChildren(updates);

                        // Clear the NIC EditText
                        nicEditText.setText("");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors if needed
            }
        });
    }
}
