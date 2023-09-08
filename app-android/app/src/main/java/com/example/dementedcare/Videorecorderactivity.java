package com.example.dementedcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
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
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videorecorderactivity);
        recordButton = findViewById(R.id.recordButton);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("videos");

        recordButton.setOnClickListener(view -> {
            // Open the video recorder
            Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(videoCaptureIntent, VIDEO_CAPTURE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_CAPTURE_REQUEST && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            if (videoUri != null) {
                uploadVideoToFirebase(videoUri);
            }
        }
    }

    private void uploadVideoToFirebase(Uri videoUri) {
        StorageReference videoRef = storageReference.child(System.currentTimeMillis() + ".mp4");
        UploadTask uploadTask = videoRef.putFile(videoUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Video uploaded successfully
            // You can get the download URL or perform any other actions here
        }).addOnFailureListener(e -> {
            // Handle errors while uploading the video
        });
    }
}