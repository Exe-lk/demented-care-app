package com.example.dementedcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class UpdatePatientActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get references to the UI elements
        EditText nicEditText = findViewById(R.id.update_nic_edit_text);
        EditText firstNameEditText = findViewById(R.id.update_first_name_edit_text);
        EditText lastNameEditText = findViewById(R.id.update_last_name_edit_text);
        EditText homeContactEditText = findViewById(R.id.update_home_contact_edit_text);
        EditText guardianNameEditText = findViewById(R.id.update_guardian_name_edit_text);
        EditText homeAddressEditText = findViewById(R.id.update_home_address_edit_text);
        EditText guardianContactNumberEditText = findViewById(R.id.update_guardian_contact_number_edit_text);
        EditText guardianNicEditText = findViewById(R.id.update_guardian_nic_edit_text);

        Button updateButton = findViewById(R.id.update_button);

        // Set an OnClickListener for the Update Button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the NIC number from the EditText
                String nicNumber = nicEditText.getText().toString();

                // Use the NIC number to fetch the patient data from Firestore
                DocumentReference patientRef = firestore.collection("users").document(nicNumber);
                patientRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    // Patient data found, update it
                                    // Retrieve the updated data from EditText fields
                                    String firstName = firstNameEditText.getText().toString();
                                    String lastName = lastNameEditText.getText().toString();
                                    String homeContact = homeContactEditText.getText().toString();
                                    String guardianName = guardianNameEditText.getText().toString();
                                    String homeAddress = homeAddressEditText.getText().toString();
                                    String guardianContactNumber = guardianContactNumberEditText.getText().toString();
                                    String guardianNic = guardianNicEditText.getText().toString();

                                    Map<String, Object> updatedData = new HashMap<>();
                                    updatedData.put("first_name", firstName);
                                    updatedData.put("last_name", lastName);
                                    updatedData.put("home_contact", homeContact);
                                    updatedData.put("guardian_full_name", guardianName);
                                    updatedData.put("home_address", homeAddress);
                                    updatedData.put("gaurdian_contact_number", guardianContactNumber);
                                    updatedData.put("gaurdian_NIC_Number", guardianNic);

                                    // Update the Firestore document with the new data
                                    patientRef.update(updatedData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Data updated successfully
                                                    // You can show a success message
                                                    Toast.makeText(UpdatePatientActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle the error if data couldn't be updated
                                                    Toast.makeText(UpdatePatientActivity.this, "Error updating data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Handle the case where the NIC number doesn't exist in Firestore
                                    Toast.makeText(UpdatePatientActivity.this, "Patient not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error if data retrieval fails
                                Toast.makeText(UpdatePatientActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
