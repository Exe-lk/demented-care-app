package com.example.dementedcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchPatientActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText searchCriteriaEditText;
    private TextView patientNameTextView;
    private TextView patientAgeTextView;
    private TextView patientGuardianTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get references to UI elements
        searchCriteriaEditText = findViewById(R.id.search_nic_edit_text);
        Button searchButton = findViewById(R.id.search_button);
        patientNameTextView = findViewById(R.id.patient_name_text_view);
        patientAgeTextView = findViewById(R.id.patient_age_text_view);
        patientGuardianTextView = findViewById(R.id.patient_guardiane_text_view);

        // Set an OnClickListener for the Search Button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the search criteria entered by the user
                String searchCriteria = searchCriteriaEditText.getText().toString();

                // Perform the search based on the criteria
                performPatientSearch(searchCriteria);
            }
        });
    }

    private void performPatientSearch(String searchCriteria) {
        // Clear any previous patient data
        clearPatientData();

        // Perform a Firestore query to search for the patient
        firestore.collection("users")
                .whereEqualTo("nicNumber", searchCriteria) // Replace with your search criteria field
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Patient found, display details
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String patientName = documentSnapshot.getString("first_name");
                            String patientAge = documentSnapshot.getString("home_contact");
                            String guardianName = documentSnapshot.getString("guardian_full_name");

                            // Set the TextViews to display patient details
                            patientNameTextView.setText("Patient Name: " + patientName);
                            patientAgeTextView.setText("Home Contact Number: " + patientAge);
                            patientGuardianTextView.setText("Guardian Full Name: " + guardianName);

                            // Show the TextViews
                            patientNameTextView.setVisibility(View.VISIBLE);
                            patientAgeTextView.setVisibility(View.VISIBLE);
                            patientGuardianTextView.setVisibility(View.VISIBLE);
                        } else {
                            // Patient not found
                            Toast.makeText(SearchPatientActivity.this, "Patient not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error if the search fails
                        Toast.makeText(SearchPatientActivity.this, "Error searching for patient", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearPatientData() {
        // Clear patient data and hide TextViews
        patientNameTextView.setText("");
        patientAgeTextView.setText("");
        patientGuardianTextView.setText("");
        patientNameTextView.setVisibility(View.GONE);
        patientAgeTextView.setVisibility(View.GONE);
        patientGuardianTextView.setVisibility(View.GONE);
    }
}
