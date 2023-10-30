package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class manage_patients extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patients);

        ImageButton manageDoctorsButton = findViewById(R.id.button);  // doctor button
        ImageButton manageNurseButton = findViewById(R.id.button2);  // nurse button
        ImageButton TrackingButton = findViewById(R.id.trackingdevice);  // nurse button
        ImageButton managepatients= findViewById(R.id.managepatients);  // manage patients button

        manageDoctorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDoctorScreen();
            }
        });

        manageNurseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageNurseScreen();
            }
        });
        TrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddHealthTrackerDevice();
            }
        });
        managepatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddmanagepatients();
            }
        });
        managepatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddmanagepatients();
            }
        });
    }
    private void openAddDoctorScreen() {
        Intent intent = new Intent(this, AddPatientsform.class);
        startActivity(intent);
    }

    private void openManageNurseScreen() {
        Intent intent = new Intent(this,UpdatePatientActivity.class);
        startActivity(intent);
    }
    private void openAddHealthTrackerDevice() {
        Intent intent = new Intent(this,SearchPatientActivity.class);
        startActivity(intent);
    }
    private void openAddmanagepatients() {
        Intent intent = new Intent(this,activity_delete_patient.class);
        startActivity(intent);
    }

    }
