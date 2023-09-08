package com.example.dementedcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Vibrator;
public class Restriedarea extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView messageTextView;
    private ImageButton changeStatusButton;
    private MediaPlayer mediaPlayer; // MediaPlayer for playing the alarm sound
    private boolean isAlarmOn = false; // Track the alarm state
    private Vibrator vibrator; // Vibrator for phone vibration
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restriedarea);

        databaseReference = FirebaseDatabase.getInstance().getReference("AI/isDetected");
        messageTextView = findViewById(R.id.messageTextView);
        changeStatusButton = findViewById(R.id.changeStatusButton);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound); // Load the alarm sound

        // Set the MediaPlayer to loop
        mediaPlayer.setLooping(true);

        // Initialize vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isDetected = dataSnapshot.getValue(Boolean.class);
                if (isDetected != null && isDetected) {
                    messageTextView.setText("Some of the patients are going to the restricted area");
                    if (!isAlarmOn) {
                        playAlarmSound(); // Play the alarm sound when isDetected is true and alarm is off
                        vibratePhone(); // Vibrate the phone when isDetected is true and alarm is off
                        isAlarmOn = true; // Set alarm state to on
                    }
                } else {
                    messageTextView.setText("");
                    if (isAlarmOn) {
                        stopAlarmSound(); // Stop the alarm sound when isDetected is false and alarm is on
                        stopPhoneVibration(); // Stop phone vibration when isDetected is false and alarm is on
                        isAlarmOn = false; // Set alarm state to off
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the "isDetected" value when the button is clicked
                databaseReference.setValue(!isAlarmOn);
                if (isAlarmOn) {
                    stopAlarmSound(); // Stop the alarm sound when the button is clicked
                    isAlarmOn = false; // Set alarm state to off
                }
            }
        });
    }

    // Function to play the alarm sound
    private void playAlarmSound() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Function to stop the alarm sound
    private void stopAlarmSound() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause the MediaPlayer instead of stopping it
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to free up resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    // Function to vibrate the phone
    private void vibratePhone() {
        if (vibrator != null) {
            // Vibrate for 1000 milliseconds (1 second)
            vibrator.vibrate(6000);
        }
    }
    // Function to stop phone vibration
    private void stopPhoneVibration() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
