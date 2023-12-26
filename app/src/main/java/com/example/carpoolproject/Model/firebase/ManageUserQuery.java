package com.example.carpoolproject.Model.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.carpoolproject.Driver.Driver;
import com.example.carpoolproject.Passenger.Passenger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ManageUserQuery {
    private static final ManageUserQuery instance = new ManageUserQuery();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static ManageUserQuery getInstance() {
        return instance;
    }

    public void addDriverToDatabase(Driver driver, ManageUserQuery.OnSuccessCallback onSuccess,
                                     ManageUserQuery.OnFailureCallback onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("drivers")
                .document(mAuth.getCurrentUser().getUid()) // Use UID as the document ID
                .set(driver)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firestore", "Driver data added successfully.");
                        } else {
                            Log.e("Firestore", "Error adding passenger data to Firestore: " + task.getException());
                        }
                    }
                });
    }


    public void addPassengerToDatabase(Passenger passenger, ManageUserQuery.OnSuccessCallback onSuccess,
                                        ManageUserQuery.OnFailureCallback onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("passengers")
                .document(mAuth.getCurrentUser().getUid()) // Use UID as the document ID
                .set(passenger)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firestore", "Passenger data added successfully.");
                        } else {
                            Log.e("Firestore", "Error adding passenger data to Firestore: " + task.getException());
                        }
                    }
                });
    }
    public void getDriverData(String driverId, OnSuccessCallback<Driver> onSuccess, OnFailureCallback onFailure) {
        db.collection("drivers")
                .document(driverId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && task.getResult().exists()) {
                            Driver driver = task.getResult().toObject(Driver.class);
                            onSuccess.onSuccess(driver);
                        } else {
                            onFailure.onFailure(new Exception("Driver not found"));
                        }
                    } else {
                        onFailure.onFailure(task.getException());
                    }
                }) .addOnFailureListener(e -> {
                    // Additional logging for debugging
                    Log.e("FirestoreError", "Error getting driver data", e);
                });
    }

    public void getPassengerData(String passengerId, OnSuccessCallback<Passenger> onSuccess, OnFailureCallback onFailure) {
        db.collection("passengers")
                .document(passengerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && task.getResult().exists()) {
                            Passenger passenger = task.getResult().toObject(Passenger.class);
                            onSuccess.onSuccess(passenger);
                        } else {
                            onFailure.onFailure(new Exception("Passenger not found"));
                        }
                    } else {
                        onFailure.onFailure(task.getException());
                    }
                });
    }
    public void getPassengersData(List<String> passengerIds,
                                  ManageRideQuery.OnSuccessCallback<List<Passenger>> onSuccess,
                                  ManageRideQuery.OnFailureCallback onFailure) {

        List<Passenger> passengers = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(passengerIds.size());

        for (String passengerId : passengerIds) {
            db.collection("passengers")
                    .document(passengerId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && task.getResult().exists()) {
                                Passenger passenger = task.getResult().toObject(Passenger.class);
                                passengers.add(passenger);
                            }
                        }

                        // Decrement count and check if all requests are completed
                        if (count.decrementAndGet() == 0) {
                            onSuccess.onSuccess(passengers);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to get passenger data
                        onFailure.onFailure(e);
                    });
        }
    }
    public interface OnSuccessCallback<T> {
        void onSuccess(T result);
    }

    public interface OnFailureCallback {
        void onFailure(Exception exception);
    }
}
