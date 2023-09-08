package com.example.dementedcare;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dementedcare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private double latitude;  // To store latitude value
    private double longitude; // To store longitude value
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        // Retrieve the device ID and location coordinates from the intent
        Intent intent = getIntent();
        deviceId = intent.getStringExtra("device_id");
        latitude = intent.getDoubleExtra("latitute", 0.0);
        longitude = intent.getDoubleExtra("longotude", 0.0);

        // Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Enable zoom controls on the map (optional)
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Create a LatLng object with the retrieved latitude and longitude
        LatLng deviceLocation = new LatLng(latitude, longitude);

        // Add a marker to the map at the device's location
        googleMap.addMarker(new MarkerOptions()
                .position(deviceLocation)
                .title("Device ID: " + deviceId));

        // Move the camera to the device's location and set an appropriate zoom level
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 15));
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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
