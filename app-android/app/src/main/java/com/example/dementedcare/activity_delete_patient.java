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
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_delete_patient extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_patient);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get reference to the NIC EditText and Delete Button
        EditText nicEditText = findViewById(R.id.delete_nic_edit_text);
        Button deleteButton = findViewById(R.id.delete_button);

        // Set an OnClickListener for the Delete Button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the NIC number from the EditText
                String nicNumber = nicEditText.getText().toString();

                // Front-end validation: Check if the NIC number field is not empty
                if (nicNumber.isEmpty()) {
                    Toast.makeText(activity_delete_patient.this, "Please enter a NIC number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use the NIC number to delete the patient data from Firestore
                DocumentReference patientRef = firestore.collection("users").document(nicNumber);
                patientRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data deleted successfully
                                Toast.makeText(activity_delete_patient.this, "Patient data deleted successfully", Toast.LENGTH_SHORT).show();
                                // Clear the NIC EditText after successful deletion
                                nicEditText.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error if data deletion fails
                                Toast.makeText(activity_delete_patient.this, "Error deleting patient data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
