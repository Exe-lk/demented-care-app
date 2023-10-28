package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DoctorScreen extends AppCompatActivity {

    private ImageButton patient_health_check;
    private ImageButton check_patient_emotions;
    private ImageButton voice_recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_screen);

        // Initialize ImageButtons
        patient_health_check = findViewById(R.id.button);
        check_patient_emotions = findViewById(R.id.button2);
        voice_recorder = findViewById(R.id.managepatients);

        ImageButton ai_Chat = findViewById(R.id.trackingdevice);  // ai chatbot button
        ImageButton restried_area = findViewById(R.id.restritpatient);  // check patient in Restrited area button
        ImageButton logout = findViewById(R.id.loginButton);  // logout button button
        ImageButton track_patient = findViewById(R.id.location);  // vaichatbot button

        // Set onClickListeners for the ImageButtons
        patient_health_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPatientHealthChecker();
            }
        });

        check_patient_emotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCheckPatientEmotions();
            }
        });

        voice_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVoiceRecorder();
            }
        });
        ai_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openchat();
            }
        });
        restried_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openrestried_area();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogout();
            }
        });
        track_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opentrack_patient();
            }
        });
    }

    private void openPatientHealthChecker() {
        Intent intent = new Intent(this, PatientHealthDetails.class);
        startActivity(intent);
    }

    private void openCheckPatientEmotions() {
        Intent intent = new Intent(this, Videorecorderactivity.class);
        startActivity(intent);
    }

    private void openVoiceRecorder() {
        Intent intent = new Intent(this, voicerecorder.class);
        startActivity(intent);
    }
    private void openchat() {
        Intent intent = new Intent(this, Chatbot.class);
        startActivity(intent);
    }
    private void openrestried_area() {
        Intent intent = new Intent(this, Restriedarea.class);
        startActivity(intent);
    }
    private void openLogout() {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
    private void opentrack_patient() {
        Intent intent = new Intent(this, MapViewActivity.class);
        startActivity(intent);
    }

}
