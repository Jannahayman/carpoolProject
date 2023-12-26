package com.example.carpoolproject.Model.firebase;

public class BookingRequest {
    private String rideId;
    private String driverId;
    private String passengerId;

    private String status;
    public BookingRequest(String rideId, String driverId, String passengerId, String status) {

        this.rideId = rideId;
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }


}
