package com.example.dementedcare;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPatientsform extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patientsform);

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("gps_tracking_devices"); // Replace with your database reference (Real TimeDB)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Create a list to store the gps_device_id values with isAvalibele=true
        List<String> deviceIdList = new ArrayList<>();

        // Attach a ValueEventListener to fetch the data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deviceIdList.clear(); // Clear the list to avoid duplicates on data change
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming your data structure is consistent
                    String deviceId = snapshot.child("gps_device_id").getValue(String.class);
                    Boolean isAvailableValue = snapshot.child("isAvalibele").getValue(Boolean.class);

                    // Check if isAvalibele is not null and is true
                    if (deviceId != null && isAvailableValue != null && isAvailableValue) {
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
                EditText firstnameText = findViewById(R.id.etfirstname);
                EditText lastnameText = findViewById(R.id.etlastname);
                EditText contactHomeText = findViewById(R.id.etconnum);
                EditText guardiannameText = findViewById(R.id.etgname);
                EditText homeaddressText = findViewById(R.id.etaddress);
                EditText gaurdiancontactnumberText = findViewById(R.id.etcontact);
                EditText guardianNICText = findViewById(R.id.et_g_nic);

                Button submitButton = findViewById(R.id.submit_button);

                // Set an OnClickListener for the Submit Button
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the selected GPS device ID from the Spinner
                        String selectedDeviceId = gpsDeviceSpinner.getSelectedItem().toString();

                        // Get the NIC number and other field values from the EditText fields
                        String nicNumber = nicEditText.getText().toString();
                        String field1Value = firstnameText.getText().toString();
                        String field2Value = lastnameText.getText().toString();
                        String field3Value = contactHomeText.getText().toString();
                        String field4Value = guardiannameText.getText().toString();
                        String field5Value = homeaddressText.getText().toString();
                        String field6Value = gaurdiancontactnumberText.getText().toString();
                        String field7Value = guardianNICText.getText().toString();

                        if (!selectedDeviceId.isEmpty() && !nicNumber.isEmpty() && !field1Value.isEmpty() && !field2Value.isEmpty()) {
                            // Use the NIC number as the Firestore document name
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("selectedDeviceId", selectedDeviceId);
                            userData.put("nicNumber", nicNumber);
                            userData.put("first_name", field1Value);
                            userData.put("last_name", field2Value);
                            userData.put("home_contact", field3Value);
                            userData.put("guardian_full_name", field4Value);
                            userData.put("home_address", field5Value);
                            userData.put("gaurdian_contact_number", field6Value);
                            userData.put("gaurdian_NIC_Number", field7Value);

                            // Create a new Firestore document with the NIC number as the document name
                            firestore.collection("users").document(nicNumber).set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Data successfully written to Firestore
                                            // Clear the NIC EditText and other field EditTexts
                                            nicEditText.setText("");
                                            firstnameText.setText("");
                                            lastnameText.setText("");
                                            contactHomeText.setText("");
                                            guardiannameText.setText("");
                                            homeaddressText.setText("");
                                            gaurdiancontactnumberText.setText("");
                                            guardianNICText.setText("");

                                            // Change the isAvalibele value to false for the selected GPS device
                                            updateIsAvailableValue(selectedDeviceId, false);

                                            // Provide a success message to the user
                                            Toast.makeText(AddPatientsform.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error if data couldn't be written to Firestore
                                            Toast.makeText(AddPatientsform.this, "Error saving data to Firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Handle empty fields or other validation as needed
                            // You can show an error message to the user
                            Toast.makeText(AddPatientsform.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors if needed
                Toast.makeText(AddPatientsform.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateIsAvailableValue(String deviceId, boolean isAvailable) {
        // Update the isAvalibele value in the Realtime Database for the selected GPS device
        DatabaseReference deviceRef = databaseReference.child(deviceId);
        deviceRef.child("isAvalibele").setValue(isAvailable)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // isAvalibele value updated successfully
                        Toast.makeText(AddPatientsform.this, "Device availability updated to true", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error if isAvalibele value couldn't be updated
                        Toast.makeText(AddPatientsform.this, "Error updating device availability", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
