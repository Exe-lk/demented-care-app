package com.example.dementedcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class pair_health_device extends AppCompatActivity {

    private EditText name;
    private Button add;
    private TextView deviceIdTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_health_device);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize your TextView from your XML layout
        deviceIdTextView = findViewById(R.id.deviceIdTextView);

        // Initialize the QR code scanner
        final IntentIntegrator qrScanner = new IntentIntegrator(this);
        qrScanner.setOrientationLocked(false);

        // Start scanning when the "Scan QR Code" button is clicked
        add = findViewById(R.id.scanQRButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScanner.initiateScan();
            }
        });

        // Query the "device_id" field from the database
        databaseReference.child("health_checking_device").child("HLD001").child("device_id")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String deviceId = dataSnapshot.getValue(String.class);
                        if (deviceId != null) {
                            deviceIdTextView.setText(deviceId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors here
                    }
                });
    }

    // Override onActivityResult to handle the result of the QR code scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String scannedValue = result.getContents();
            String deviceId = deviceIdTextView.getText().toString();

            if (scannedValue != null && scannedValue.equals(deviceId)) {
                // QR code value matches device_id, open a new activity
                Intent intent = new Intent(this, PatientHealthDetails.class); // Replace YourNewActivity with the actual activity class you want to open
                startActivity(intent);
            } else {
                // Handle the case where the QR code value doesn't match
                // You can show a message to the user or take appropriate action here
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
