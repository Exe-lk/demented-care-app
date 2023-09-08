package com.example.dementedcare;

public class Device {
    private String gps_device_id;
    private String userId;
    private String longitude;
    private String latitude;

    // Default constructor (no-argument constructor)
    public Device() {
        // Default constructor is required for Firebase deserialization.
    }

    public Device(String gps_device_id, String userId, String longitude, String latitude) {
        this.gps_device_id = gps_device_id;
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getGps_device_id() {
        return gps_device_id;
    }

    public void setGps_device_id(String gps_device_id) {
        this.gps_device_id = gps_device_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


}
