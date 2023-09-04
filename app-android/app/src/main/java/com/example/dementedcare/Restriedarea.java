package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.Button;
public class Restriedarea extends AppCompatActivity {

    //Declare  Variables
    private DatabaseReference databaseReference;
    private TextView messageTextView;
    private ImageButton changeStatusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restriedarea);
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("AI/isDetected");

        messageTextView = findViewById(R.id.messageTextView);

        changeStatusButton = findViewById(R.id.changeStatusButton);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the value is true
                boolean isDetected = dataSnapshot.getValue(Boolean.class);
                if (isDetected) {
                    messageTextView.setText("The some of the patient is going to the restricted area");
                } else {
                    messageTextView.setText(""); // Clear the message if not true
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        // Add a click listener to the button to change the status
        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the "isDetected" value to false
                databaseReference.setValue(false);
            }
        });

    }
}