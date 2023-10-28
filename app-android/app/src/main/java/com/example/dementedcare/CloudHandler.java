package com.example.dementedcare;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dementedcare.model.Audio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CloudHandler {
    private Uri path;
    StorageReference folder;
    public CloudHandler(){

        folder = FirebaseStorage.getInstance().getReference().child("audios");

    }

    public void SaveOnCloud(Audio audio) {
        path = audio.getPath();
        StorageReference imagename = folder.child(path.getLastPathSegment());

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg") // Set the content type to audio/mpeg
                .build();
        // Adding the file to cloud storage
        UploadTask uploadTask = imagename.putFile(path,metadata);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Successfully uploaded
                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl1 = String.valueOf(uri);
                        // Handle the successful upload here
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure, e.g., show an error message to the user
                Log.e("Firebase", "Upload failed: " + e.getMessage());
            }
        });
    }


}
