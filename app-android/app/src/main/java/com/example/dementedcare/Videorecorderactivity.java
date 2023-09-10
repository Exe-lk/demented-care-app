package com.example.dementedcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    private ImageView videoImageView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videorecorderactivity);
        recordButton = findViewById(R.id.recordButton);
        uploadButton = findViewById(R.id.uploadBtn);
        cancelButton = findViewById(R.id.caseldBtn);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("videos");

        videoImageView = findViewById(R.id.videoImageView);
        videoImageView.setVisibility(View.VISIBLE); // Initially visible
        videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.GONE); // Initially gone

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

        cancelButton.setOnClickListener(view -> {
            // Cancel the recording (if it's in progress)
            if (videoUri != null) { // Check if a video has been recorded
                // If the recording is in progress, cancel it
                setResult(RESULT_CANCELED);
                finish();
                Intent intent = new Intent(this, Videorecorderactivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_CAPTURE_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
            // Display the recorded video in the VideoView
            videoImageView.setVisibility(View.GONE); // Hide the image
            videoView.setVisibility(View.VISIBLE); // Show the VideoView
            videoView.setVideoURI(videoUri);
            videoView.start(); // Start video playback
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
