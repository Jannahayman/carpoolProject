package com.example.carpoolproject.Model.firebase;

import com.google.firebase.Timestamp;

public class Ride {
    private String pickup;

    public Ride() {
    }

    private String dropOff;
    private String time;
    private String driverId;
    private String status;
    private Timestamp timestamp; // Updated attribute to store timestamp
    private int available_seats =4;
    private double rate;

    public Ride(String pickup, String dropOff, String time, String driverId, String status, Timestamp timestamp) {
        this.pickup = pickup;
        this.dropOff = dropOff;
        this.time = time;
        this.driverId = driverId;
        this.status = status;
        this.timestamp = timestamp;
        this.rate = 50.0;
        this.available_seats=4;
    }

    public Ride(String pickup, String dropOff, String time, String driverId, String status, Timestamp timestamp, int available_seats) {
        this.pickup = pickup;
        this.dropOff = dropOff;
        this.time = time;
        this.driverId = driverId;
        this.status = status;
        this.timestamp = timestamp;
        this.rate = 50.0;
        this.available_seats=available_seats;
    }


    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDropOff() {
        return dropOff;
    }

    public void setDropOff(String dropOff) {
        this.dropOff = dropOff;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(int available_seats) {
        this.available_seats = available_seats;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }

}
