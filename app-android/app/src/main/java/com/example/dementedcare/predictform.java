package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.util.Log; // Import Log for logging
import android.widget.Toast; // Import Toast for displaying error messages


public class predictform extends AppCompatActivity {

    private String receivedData;
    private int receivedData1;
    private double receivedData2;

    private Spinner genderSpinner,chestSpinner,bloodsugar,ecgSpprinter,eiaSpprinter,pealexSpprinter,fluSpprinter,thresultSpprinter;

    private Button btnSubmit,btncansel;

    private EditText etAge,etRbp,etChl,etconnum,etDp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictform);

        Intent intent = getIntent();
        receivedData = intent.getStringExtra("heartRate");
        if(receivedData != null){
            receivedData2 = Double.parseDouble(receivedData);
            receivedData1 = (int) receivedData2;
        }

        // Spinners
        genderSpinner = findViewById(R.id.etGender);
        chestSpinner  = findViewById(R.id.etCps);
        bloodsugar = findViewById(R.id.etFbs);
        ecgSpprinter = findViewById(R.id.etEcg);
        eiaSpprinter = findViewById(R.id.etEia);
        pealexSpprinter = findViewById(R.id.etPeakEX);
        fluSpprinter = findViewById(R.id.etFlu);
        thresultSpprinter = findViewById(R.id.etthreresult);

        // Buttons
        btnSubmit = findViewById(R.id.btnSubmit);
        btncansel = findViewById(R.id.btncansel);

        // User input values
        etAge = findViewById(R.id.etAge);
        etRbp = findViewById(R.id.etRbp);
        etChl = findViewById(R.id.etChl);
        etconnum = findViewById(R.id.etconnum);
        etDp = findViewById(R.id.etDp);  //ST Depresssion induced by Exercise Relative Rest

        etconnum.setText(receivedData.toString());

        // Define spinner options
        String[] genderOptions = {"Male", "Female", "Other"};
        String[] chestOptions  = {"0","1","2","3"};
        String[] bloodSugarOptions = {"0 for <=120 mg/dl","1 for >120 mg/dl"};
        String[] ecfOptions = {"0","1","2"};
        String[] eiaOptions = {"Yes","No"};
        String[] peakEXOptions = {"0","1","2"};
        String[] FluOptions = {"0","1","2","3"};
        String[] thresultOptions = {"0","1","2","3"};

        // Create ArrayAdapter and set it on Spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        ArrayAdapter<String> Chestadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chestOptions);
        Chestadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chestSpinner.setAdapter(Chestadapter);

        ArrayAdapter<String> BloodSugaradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodSugarOptions);
        BloodSugaradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodsugar.setAdapter(BloodSugaradapter);

        ArrayAdapter<String> Ecgadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ecfOptions);
        Ecgadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ecgSpprinter.setAdapter(Ecgadapter);

        ArrayAdapter<String> Eiaadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eiaOptions);
        Eiaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eiaSpprinter.setAdapter(Eiaadapter);

        ArrayAdapter<String> peakEXadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, peakEXOptions);
        peakEXadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pealexSpprinter.setAdapter(peakEXadapter);

        ArrayAdapter<String> Fluadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FluOptions);
        Fluadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fluSpprinter.setAdapter(Fluadapter);

        ArrayAdapter<String> threresultadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thresultOptions);
        threresultadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thresultSpprinter.setAdapter(threresultadapter);

        // OnClickListener for the Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input values
                String age = etAge.getText().toString();
                String rbp = etRbp.getText().toString();
                String chl = etChl.getText().toString();
                String oldpeek = etDp.getText().toString();

                // Retrieve selected spinner values
                String selectedGender = genderSpinner.getSelectedItem().toString();
                String selectedChest = chestSpinner.getSelectedItem().toString();
                String selectedBloodSugar = bloodsugar.getSelectedItem().toString();
                String selectedEcg = ecgSpprinter.getSelectedItem().toString();
                String selectedEia = eiaSpprinter.getSelectedItem().toString();
                String selectedPeakEX = pealexSpprinter.getSelectedItem().toString();
                String selectedFlu = fluSpprinter.getSelectedItem().toString();
                String selectedThresult = thresultSpprinter.getSelectedItem().toString();

                int genderValue;
                if (selectedGender.equals("Male")) {
                    genderValue = 0;
                } else if (selectedGender.equals("Female")) {
                    genderValue = 1;
                } else {
                    genderValue = 2; // Assuming "Other" corresponds to 2
                }

                int fastingbloodsugarValue;
                if (selectedBloodSugar.equals("<=120 mg/dl")) {
                    fastingbloodsugarValue = 0;
                } else if (selectedBloodSugar.equals(">120 mg/dl")) {
                    fastingbloodsugarValue = 1;
                } else {
                    fastingbloodsugarValue = 2; // Handle the case when neither option is selected
                }

                int eiaValue;
                if (selectedEia.equals("No")) {
                    eiaValue = 0;
                } else if (selectedEia.equals("Yes")) {
                    eiaValue = 1;
                } else {
                    eiaValue = 2; // Handle the case when neither option is selected
                }

                // Perform backend validation checks
                if (isInputValid(age, rbp, chl, selectedGender, selectedChest, selectedBloodSugar, selectedEcg, selectedEia, selectedPeakEX, selectedFlu, selectedThresult)) {
                    // Log user input values
                    Log.d("User Input", "Age: " + age);
                    Log.d("User Input", "Rbp: " + rbp);
                    Log.d("User Input", "Chl: " + chl);
                    // ... Log other input data

//


                    // Create an intent to start the new activity
                    Intent intent = new Intent(predictform.this, AIPredictionActivity.class);
                    // Add the user input data as extras to the intent

                    // ... Add other input data as extras
                    intent.putExtra("age",age);
                    intent.putExtra("sex", genderValue);
                    intent.putExtra("cp", Integer.parseInt(selectedChest));
                    intent.putExtra("trtbps", rbp);
                    intent.putExtra("chol", chl);
                    intent.putExtra("fbs", fastingbloodsugarValue);
                    intent.putExtra("restecg", selectedEcg);

                    intent.putExtra("thalachh", receivedData1);
                    intent.putExtra("exng", eiaValue);
//                    Fix ST Depression Induced by Exercise Relative to Rest
                    intent.putExtra("oldpeak", oldpeek);
                    intent.putExtra("slp", selectedPeakEX);
                    intent.putExtra("caa", selectedFlu);
                    intent.putExtra("thall", selectedThresult);
                    // Start the new activity
                    startActivity(intent);
                } else {
                    // Display an error message to the user if validation fails
                    // For example, you can show a Toast message or set an error message on the EditText fields.
                    Toast.makeText(predictform.this, "Invalid input. Please check your input values.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // OnClickListener for the Cancel button
        btncansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the new activity
                Intent intent = new Intent(predictform.this, PatientHealthDetails.class);

                // Add any data you want to pass to the new activity using intent extras
                // intent.putExtra("key", "value");


                // Start the new activity
                startActivity(intent);
            }
        });
    }

    // Backend validation function
    private boolean isInputValid(String age, String rbp, String chl, String selectedGender, String selectedChest, String selectedBloodSugar, String selectedEcg, String selectedEia, String selectedPeakEX, String selectedFlu, String selectedThresult) {
        // Add your validation logic here
        // Return true if input is valid, false otherwise
        // Example: Check if age, rbp, and chl are not empty and meet certain criteria
        // You can customize this logic based on your requirements.

        if (age.isEmpty() || rbp.isEmpty() || chl.isEmpty()) {
            return false; // Input fields should not be empty
        }

        // Check if a spinner value is not selected
        if (selectedGender.equals("Select Gender") || selectedChest.equals("Select Chest Pain Type") || selectedBloodSugar.equals("Select Blood Sugar Level") || selectedEcg.equals("Select ECG Result") || selectedEia.equals("Select Exercise Induced Angina") || selectedPeakEX.equals("Select Peak Exercise ST Segment") || selectedFlu.equals("Select Fluoroscopy Result") || selectedThresult.equals("Select Thalassemia Result")) {
            return false; // Spinner values should be selected
        }

        // Add more validation checks here as needed
        try {
            int ageValue = Integer.parseInt(age);
            int rbpValue = Integer.parseInt(rbp);
            int chlValue = Integer.parseInt(chl);

            if (ageValue <= 0 || rbpValue <= 0 || chlValue <= 0) {
                return false; // Values should be greater than 0
            }

            // Add more specific validation checks here as needed

        } catch (NumberFormatException e) {
            return false; // Input values should be valid integers
        }

        return true; // Input is valid
    }
}
