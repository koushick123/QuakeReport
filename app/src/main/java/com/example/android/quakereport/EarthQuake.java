package com.example.android.quakereport;

/**
 * Created by Koushick on 22-08-2016.
 */

public class EarthQuake {

    private double magnitude;
    private String location;
    private long date_time;

    public EarthQuake(double magnitude, String location, long date_time) {
        this.magnitude = magnitude;
        this.location = location;
        this.date_time = date_time;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDate_time() {
        return date_time;
    }

    public void setDate_time(long date_time) {
        this.date_time = date_time;
    }
}
