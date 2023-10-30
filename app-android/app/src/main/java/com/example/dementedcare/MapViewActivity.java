package com.example.dementedcare;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dementedcare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private double latitude;  // To store latitude value
    private double longitude; // To store longitude value
    private String deviceId;
    private DatabaseReference databaseReference;


    //alarm
    private MediaPlayer mediaPlayer;
    private boolean isAlarmOn = false;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        // Set the MediaPlayer to loop
        mediaPlayer.setLooping(true);
        // Initialize vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Retrieve the device ID and location coordinates from the intent
        Intent intent = getIntent();
        deviceId = intent.getStringExtra("device_id");
        latitude = intent.getDoubleExtra("latitute", 6.91);
        longitude = intent.getDoubleExtra("longotude", 79.97);

        // Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("gps_tracking_devices");

        // Listen for changes in the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                googleMap.clear(); // Clear all markers
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String deviceId = userSnapshot.getKey();
                    double latitude = userSnapshot.child("latitude").getValue(Double.class);
                    double longitude = userSnapshot.child("longitude").getValue(Double.class);
                    boolean isOutside = userSnapshot.child("outSide").getValue(Boolean.class);
                    String userID = userSnapshot.child("userID").getValue(String.class);
                    checkAndUpdateLocation(latitude,longitude,deviceId,userID);

                }
                getDevicesWithIsOutsideTrue(new IsOutsideCallback() {
                    @Override
                    public void onIsOutsideResult(boolean isOutside) {
                        if (isOutside) {
                            if (!isAlarmOn) {
                                playAlarmSound();
                                vibratePhone();
                                isAlarmOn = true;
                            }
                        } else {
                            if (isAlarmOn) {
                                stopAlarmSound();
                                stopPhoneVibration();
                                isAlarmOn = false;
                            }
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapViewActivity.this, "Failed to read data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Enable zoom controls on the map (optional)
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Create a LatLng object with the retrieved latitude and longitude
        LatLng deviceLocation = new LatLng(latitude, longitude);

        // Add a marker to the map at the device's location
//        googleMap.addMarker(new MarkerOptions()
//                .position(deviceLocation)
//                .title("Device ID: " + deviceId));

        // Move the camera to the device's location and set an appropriate zoom level
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 8));
    }



    private void addMarker(double latitude, double longitude, String userId, boolean isOutside) {
        LatLng userLocation = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(userLocation)
                .title("User ID: " + userId);

        if (isOutside) {
            // Marker style for devices outside the area
            // Customize the icon or color as needed
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            // Marker style for devices inside the area
            // Customize the icon or color as needed
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }

        googleMap.addMarker(markerOptions);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 3));
    }


    private void checkAndUpdateLocation(double deviceLatitude, double deviceLongitude, String deviceID,String userID) {
        // Define the boundaries of your area
        double boundaryLatitude = latitude;
        double boundaryLongitude = longitude;


        double distance = distanceBetweenCoordinates(deviceLatitude, deviceLongitude, boundaryLatitude, boundaryLongitude);

        boolean isOutside = distance > 5.0; // Set yourRadius to the desired threshold


        databaseReference.child(deviceID).child("outSide").setValue(isOutside)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addMarker(deviceLatitude, deviceLongitude, userID, isOutside);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private double distanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
    private void getDevicesWithIsOutsideTrue(final IsOutsideCallback callback) {
        // Create a list to store the device IDs with isOutside set to true
        List<String> deviceIdsWithIsOutsideTrue = new ArrayList<>();

        // Reference to the "gps_tracking_devices" node in your Firebase database
        DatabaseReference devicesRef = FirebaseDatabase.getInstance().getReference("gps_tracking_devices");

        devicesRef.orderByChild("outSide").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
                            String deviceId = deviceSnapshot.getKey();
                            deviceIdsWithIsOutsideTrue.add(deviceId);
                        }

                        boolean isOutside = !deviceIdsWithIsOutsideTrue.isEmpty();
                        callback.onIsOutsideResult(isOutside);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onIsOutsideResult(false); // Handle error by returning false
                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

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

    public interface IsOutsideCallback {
        void onIsOutsideResult(boolean isOutside);
    }
}
