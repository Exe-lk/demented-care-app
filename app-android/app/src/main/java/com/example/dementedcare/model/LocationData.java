package com.example.dementedcare.model;

public class LocationData {
    private double latitude;
    private double longitude;
    private String gps_device_id;
    private Boolean isAvalibele;

    public void setIsAvalibele(Boolean isAvalibele) {
        this.isAvalibele = isAvalibele;
    }

    private String userID;
    private boolean isOutSide;

    public boolean isOutSide() {
        return isOutSide;
    }

    public void setOutSide(boolean outSide) {
        isOutSide = outSide;
    }

    public String getGps_device_id() {
        return gps_device_id;
    }

    public void setGps_device_id(String gps_device_id) {
        this.gps_device_id = gps_device_id;
    }

    public Boolean getAvalibele() {
        return isAvalibele;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocationData() {
        // Default constructor required for Firebase
    }

    public LocationData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
