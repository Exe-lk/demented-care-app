package com.example.dementedcare;

import android.os.Parcel;
import android.os.Parcelable;

public class data implements Parcelable {
    String email;


    public data() {
        // Default constructor
    }

    protected data(Parcel in) {
        email = in.readString();
    }

    public static final Creator<data> CREATOR = new Creator<data>() {
        @Override
        public data createFromParcel(Parcel in) {
            return new data(in);
        }

        @Override
        public data[] newArray(int size) {
            return new data[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Implement the Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
    }
}
