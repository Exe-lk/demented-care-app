package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Videorecorderactivity extends AppCompatActivity {

    private static final int VIDEO_CAPTURE_REQUEST = 1;
    private Button recordButton;
    private Button uploadButton;
    private Button cancelButton;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri videoUri; // Store the recorded video URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videorecorderactivity);
        recordButton = findViewById(R.id.recordButton);
        uploadButton = findViewById(R.id.uploadBtn);
        cancelButton = findViewById(R.id.caseldBtn);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("videos");

        recordButton.setOnClickListener(view -> {
            // Open the video recorder
            Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(videoCaptureIntent, VIDEO_CAPTURE_REQUEST);
            }
        });

        uploadButton.setOnClickListener(view -> { //set on click listener
            if (videoUri != null) {
                uploadVideoToFirebase(videoUri);
            }
        });

        cancelButton.setOnClickListener(view -> { //set on click listener
            // Cancel the recording (if it's in progress)
            if (VIDEO_CAPTURE_REQUEST == RESULT_OK) {
                // If the recording is in progress, cancel it
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_CAPTURE_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
        }
    }

    private void uploadVideoToFirebase(Uri videoUri) {
        StorageReference videoRef = storageReference.child(System.currentTimeMillis() + ".mp4");
        UploadTask uploadTask = videoRef.putFile(videoUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Video uploaded successfully
            // You can get the download URL or perform any other actions here

            // Show a Toast message

            Toast.makeText(getApplicationContext(), "Video uploaded successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Handle errors while uploading the video

            // Show a Toast message for failure
            Toast.makeText(getApplicationContext(), "Failed to upload video", Toast.LENGTH_SHORT).show();
        });
    }

}
