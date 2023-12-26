package com.example.carpoolproject.Model.firebase;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManageRideQuery {
    private static final ManageRideQuery instance = new ManageRideQuery();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public ManageRideQuery() {
    }
    public static ManageRideQuery getInstance() {
        return instance;
    }

    public void getAvailableRides(
            ManageRideQuery.OnSuccessCallback<List<Ride>> onSuccess,
            ManageRideQuery.OnFailureCallback onFailure
    ) {
        db.collection("rides")
                .whereEqualTo("status","available")
                .get()
                .addOnSuccessListener(result -> {
                    List<Ride> rides = parseRides(result);
                    onSuccess.onSuccess(rides);
                })
                .addOnFailureListener(onFailure::onFailure);
    }

    public void addRideToDatabase(   String pickup,
                                       String dropOff,
                                       String time,
                                       String status,
                                       Timestamp timestamp,
                                       ManageRideQuery.OnSuccessCallback onSuccess,
                                       ManageRideQuery.OnFailureCallback onFailure) {

        String uid = FirebaseAuth.getInstance().getUid();
        // Create a new ride map
        Map<String, Object> ride = new HashMap<>();
        ride.put("pickup", pickup);
        ride.put("dropOff", dropOff);
        ride.put("time", time);
        ride.put("driverId", uid);
        ride.put("status", status);
        ride.put("timestamp", timestamp);
        ride.put("available_seats", 4);
        ride.put("rate", 50.0);
        // Add more attributes as needed
        db.collection("rides")
                .add(ride)
                .addOnSuccessListener(documentReference ->
                        onSuccess.onSuccess("Ride added with ID: " + documentReference.getId()))
                .addOnFailureListener(onFailure::onFailure);
    }
    private List<Ride> parseRides(QuerySnapshot result) {
        List<Ride> rides = new ArrayList<>();
        for (QueryDocumentSnapshot document : result) {
            String pickup = document.getString("pickup") != null ? document.getString("pickup") : "";
            String dropOff = document.getString("dropOff") != null ? document.getString("dropOff") : "";
            String time = document.getString("time") != null ? document.getString("time") : new String();
            String driverId = document.getString("driverId") != null ? document.getString("driverId") : new String();
            String status = document.getString("status") != null ? document.getString("status") : new String();
            Timestamp timestamp = document.getTimestamp("timestamp") != null ? document.getTimestamp("timestamp") : new Timestamp(new Date().getTime()/1000,0);
            int availableSeats = document.get("available_seats") != null ? document.getLong("available_seats").intValue() : 0;

            Ride ride = new Ride(pickup, dropOff, time, driverId, status, timestamp,availableSeats);
            rides.add(ride);
        }
        return rides;
    }

    public void getDriverRide(String rideType, ManageUserQuery.OnSuccessCallback<List<Ride>> onSuccess, ManageUserQuery.OnFailureCallback onFailure) {
        String driverId = mAuth.getUid();
        db.collection("rides")
                .whereEqualTo("driverId", driverId)
                .whereEqualTo("status", rideType)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot result) {
                        List<Ride> rides = new ArrayList<>();

                        for (QueryDocumentSnapshot document : result) {
                            String pickup = document.getString("pickup") != null ? document.getString("pickup") : "";
                            String dropOff = document.getString("dropOff") != null ? document.getString("dropOff") : "";
                            String time = document.getString("time") != null ? document.getString("time") : new String();
                            Timestamp timestamp = document.getTimestamp("timestamp") != null ? document.getTimestamp("timestamp") : new Timestamp(new Date().getTime()/1000,0);
                            String status = document.getString("status") != null ? document.getString("status") : new String();

                            Ride ride = new Ride(pickup, dropOff, time, driverId, status, timestamp);
                            rides.add(ride);
                        }
                        onSuccess.onSuccess(rides);

                    }
                }).addOnFailureListener(onFailure::onFailure);
    }
//
//    public void getRidesIDsFromRequests(String passengerId, String driverId,
//                                        ManageRideQuery.OnSuccessCallback<List<String>> onSuccess,
//                                        ManageRideQuery.OnFailureCallback onFailure) {
//
//        db.collection("requests")
//                .whereEqualTo("passengerId", passengerId)
//                .whereEqualTo("driverId", driverId)
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    List<String> rideIDs = new ArrayList<>();
//
//                    for (QueryDocumentSnapshot document : querySnapshot) {
//                        String rideID = document.getString("rideId");
//                        if (rideID != null) {
//                            rideIDs.add(rideID);
//                        }
//                    }
//
//                    onSuccess.onSuccess(rideIDs);
//                })
//                .addOnFailureListener(onFailure::onFailure);
//    }

    public void getRideID(String pickup, String dropoff, String time, String driverID, String status, Timestamp timestamp, double rate, ManageUserQuery.OnSuccessCallback<String > onSuccess, ManageUserQuery.OnFailureCallback onFailure) {
        db.collection("rides")
                .whereEqualTo("pickup", pickup)
                .whereEqualTo("dropOff", dropoff)
                .whereEqualTo("time", time)
                .whereEqualTo("driverId", driverID)
                .whereEqualTo("status", status)
                .whereEqualTo("timestamp", timestamp)
                .whereEqualTo("rate", rate)
                .get()
                .addOnSuccessListener(result -> {
                    if (!result.isEmpty()) {
                        DocumentSnapshot document = result.getDocuments().get(0);
                        String documentId = document.getId();
                        onSuccess.onSuccess(documentId);
                    } else {
                        onFailure.onFailure(new Exception("Ride not found"));
                    }
                })
                .addOnFailureListener(onFailure::onFailure);
    }


    public void addRequestToDatabase(String passengerId, String rideId, String driverId,
                                     ManageRideQuery.OnSuccessCallback onSuccess,
                                     ManageRideQuery.OnFailureCallback onFailure) {

        // Check if a request with the same attributes already exists
        db.collection("requests")
                .whereEqualTo("passengerId", passengerId)
                .whereEqualTo("rideId", rideId)
                .whereEqualTo("driverId", driverId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // A request with the same attributes already exists
                        onSuccess.onSuccess("Already Requested");
                    } else {
                        // Create a new booking request
                        Map<String, Object> request = new HashMap<>();
                        request.put("passengerId", passengerId);
                        request.put("rideId", rideId);
                        request.put("driverId", driverId);
                        request.put("status", "pending");

                        // Add more attributes as needed
                        db.collection("requests")
                                .add(request)
                                .addOnSuccessListener(documentReference ->
                                        onSuccess.onSuccess(documentReference.getId()))
                                .addOnFailureListener(onFailure::onFailure);
                    }
                })
                .addOnFailureListener(e -> onFailure.onFailure(e));
    }

    public void getPassengerRequests(String driverId,String status,
                                     OnSuccessCallback<List<String>> onSuccess,
                                     ManageRideQuery.OnFailureCallback onFailure) {

        db.collection("requests")
                .whereEqualTo("driverId", driverId)
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<BookingRequest> bookingRequests = new ArrayList<>();
                    List<String> passengerIds = new ArrayList<>();

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String rideId = document.getId();
                        String passengerId = document.getString("passengerId");
                        passengerIds.add(passengerId);

                        BookingRequest bookingRequest = new BookingRequest(rideId, driverId, passengerId,status);
                        bookingRequests.add(bookingRequest);
                    }

                    onSuccess.onSuccess(passengerIds);
                })
                .addOnFailureListener(onFailure::onFailure);
    }


    public void getPassengerRidesIDs(String passengerId,
                                  OnSuccessCallback<List<String>> onSuccess,
                                  ManageRideQuery.OnFailureCallback onFailure) {

        db.collection("requests")
                .whereEqualTo("passengerId", passengerId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Set<String> uniqueRideIDs = new HashSet<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String rideId = document.getString("rideId");
                        if (rideId != null) {
                            uniqueRideIDs.add(rideId);
                        }
                    }
                    List<String> passengerRides = new ArrayList<>(uniqueRideIDs);
                    onSuccess.onSuccess(passengerRides);
                })
                .addOnFailureListener(onFailure::onFailure);
    }
    public void updateRequestStatusToAccepted(String passengerId, String driverId, String status,
                                              ManageRideQuery.OnSuccessCallback<List<String>> onSuccess,
                                              ManageRideQuery.OnFailureCallback onFailure) {

        db.collection("requests")
                .whereEqualTo("passengerId", passengerId)
                .whereEqualTo("driverId", driverId)
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> rideIDsToUpdate = new ArrayList<>();

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String rideId = document.getString("rideId");
                        if (rideId != null) {
                            rideIDsToUpdate.add(rideId);

                            // Update the status in the "requests" collection
                            document.getReference().update("status", "accepted")
                                    .addOnFailureListener(onFailure::onFailure);
                        }
                    }

                    onSuccess.onSuccess(rideIDsToUpdate);
                })
                .addOnFailureListener(onFailure::onFailure);
    }
    public void updateRequestStatusToRejected(String passengerId, String driverId, String status,
                                                 ManageRideQuery.OnSuccessCallback<List<String>> onSuccess,
                                                 ManageRideQuery.OnFailureCallback onFailure) {

        db.collection("requests")
                .whereEqualTo("passengerId", passengerId)
                .whereEqualTo("driverId", driverId)
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> rideIDsToUpdate = new ArrayList<>();

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String rideId = document.getString("rideId");
                        if (rideId != null) {
                            rideIDsToUpdate.add(rideId);

                            // Update the status in the "requests" collection
                            document.getReference().update("status", "rejected")
                                    .addOnFailureListener(onFailure::onFailure);
                        }
                    }

                    onSuccess.onSuccess(rideIDsToUpdate);
                })
                .addOnFailureListener(onFailure::onFailure);
    }
    public void getRidesWithUpdatedStatus(List<String> rideIds,
                                          ManageRideQuery.OnSuccessCallback<List<Ride>> onSuccess,
                                          ManageRideQuery.OnFailureCallback onFailure) {
        List<Ride> updatedRides = new ArrayList<>();

        // Step 1: Query BookingRequest collection
        db.collection("requests")
                .whereIn("rideId",rideIds)
                .get()
                .addOnSuccessListener(bookingRequestSnapshot -> {
                    // Step 2: Obtain ride IDs and status values
                    Map<String, String> rideStatusMap = new HashMap<>();
                    for (QueryDocumentSnapshot bookingRequestDoc : bookingRequestSnapshot) {
                        String rideId = bookingRequestDoc.getString("rideId");
                        String status = bookingRequestDoc.getString("status");
                        if (!"rejected".equals(status)) {
                            rideStatusMap.put(rideId, status);
                        }                    }

                    // Step 3: Query Ride collection and update status
                    db.collection("rides")
                            .whereIn(FieldPath.documentId(), rideIds)
                            .get()
                            .addOnSuccessListener(rideSnapshot -> {
                                for (QueryDocumentSnapshot rideDoc : rideSnapshot) {
                                    Ride ride = rideDoc.toObject(Ride.class);
                                    String rideId = rideDoc.getId();
                                    String updatedStatus = rideStatusMap.get(rideId);

                                    if (updatedStatus != null) {
                                        // Update the status attribute of the Ride object
                                        ride.setStatus(updatedStatus);
                                        updatedRides.add(ride);
                                    }
                                }
                                onSuccess.onSuccess(updatedRides);
                            })
                            .addOnFailureListener(onFailure::onFailure);
                })
                .addOnFailureListener(onFailure::onFailure);
    }

    public void getRidesWithCompletedStatus(List<String> rideIds,
                                          ManageRideQuery.OnSuccessCallback<List<Ride>> onSuccess,
                                          ManageRideQuery.OnFailureCallback onFailure) {
        List<Ride> updatedRides = new ArrayList<>();

        // Step 1: Query BookingRequest collection
        db.collection("requests")
                .whereIn("rideId",rideIds)
                .get()
                .addOnSuccessListener(bookingRequestSnapshot -> {
                    // Step 2: Obtain ride IDs and status values
                    Map<String, String> rideStatusMap = new HashMap<>();
                    for (QueryDocumentSnapshot bookingRequestDoc : bookingRequestSnapshot) {
                        String rideId = bookingRequestDoc.getString("rideId");
                        String status = bookingRequestDoc.getString("status");
                        rideStatusMap.put(rideId, status);
                    }

                    // Step 3: Query Ride collection and update status
                    db.collection("rides")
                            .whereIn(FieldPath.documentId(), rideIds)
                            .get()
                            .addOnSuccessListener(rideSnapshot -> {
                                for (QueryDocumentSnapshot rideDoc : rideSnapshot) {
                                    Ride ride = rideDoc.toObject(Ride.class);
                                    String rideId = rideDoc.getId();
                                    String updatedStatus = rideStatusMap.get(rideId);

                                    if (updatedStatus != null) {
                                        // Update the status attribute of the Ride object
                                        ride.setStatus(updatedStatus);
                                        updatedRides.add(ride);
                                    }
                                }
                                onSuccess.onSuccess(updatedRides);
                            })
                            .addOnFailureListener(onFailure::onFailure);
                })
                .addOnFailureListener(onFailure::onFailure);
    }

    public void getRidesfromRequests(List<String> rideDocumentIDs,
                         OnSuccessCallback<List<Ride>> onSuccess,
                         ManageRideQuery.OnFailureCallback onFailure) {

        List<Ride> rides = new ArrayList<>();

        // Check if there are no ride document IDs
        if (rideDocumentIDs == null || rideDocumentIDs.isEmpty()) {
            onSuccess.onSuccess(rides); // Return an empty list since there are no document IDs
            return;
        }

        db.collection("rides")
                .whereIn(FieldPath.documentId(), rideDocumentIDs)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Ride ride = document.toObject(Ride.class);
                        rides.add(ride);
                    }

                    onSuccess.onSuccess(rides);
                })
                .addOnFailureListener(onFailure::onFailure);
    }
    public void getPassengerCompletedRides(List<String> rideIds,
                                            ManageRideQuery.OnSuccessCallback<List<Ride>> onSuccess,
                                            ManageRideQuery.OnFailureCallback onFailure) {
        List<Ride> updatedRides = new ArrayList<>();

        // Step 1: Query BookingRequest collection
        db.collection("requests")
                .whereIn("rideId", rideIds)
                .get()
                .addOnSuccessListener(bookingRequestSnapshot -> {
                    // Step 2: Obtain filtered ride IDs and status values
                    Map<String, String> rideStatusMap = new HashMap<>();
                    List<String> filteredRideIds = new ArrayList<>();

                    for (QueryDocumentSnapshot bookingRequestDoc : bookingRequestSnapshot) {
                        String rideId = bookingRequestDoc.getString("rideId");
                        String status = bookingRequestDoc.getString("status");

                        // Check if status is "complete" or "canceled"
                        if ("complete".equals(status) || "rejected".equals(status)) {
                            filteredRideIds.add(rideId);
                            rideStatusMap.put(rideId, status);
                        }
                    }

                    // Step 3: Query Ride collection and update status for filtered ride IDs
                    db.collection("rides")
                            .whereIn(FieldPath.documentId(), filteredRideIds)
                            .get()
                            .addOnSuccessListener(rideSnapshot -> {
                                for (QueryDocumentSnapshot rideDoc : rideSnapshot) {
                                    Ride ride = rideDoc.toObject(Ride.class);
                                    String rideId = rideDoc.getId();
                                    String updatedStatus = rideStatusMap.get(rideId);

                                    if (updatedStatus != null) {
                                        // Update the status attribute of the Ride object
                                        ride.setStatus(updatedStatus);
                                        updatedRides.add(ride);
                                    }
                                }
                                onSuccess.onSuccess(updatedRides);
                            })
                            .addOnFailureListener(onFailure::onFailure);
                })
                .addOnFailureListener(onFailure::onFailure);
    }


    public void updateRideStatusToComplete(String rideId,
                                           ManageRideQuery.OnSuccessCallback<Void> onSuccess,
                                           ManageRideQuery.OnFailureCallback onFailure) {
        DocumentReference rideRef = db.collection("rides").document(rideId);
        rideRef.update("status", "complete")
                .addOnSuccessListener(aVoid -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure::onFailure);
    }
    public void makeRideUnavailable(String rideId,
                                           ManageRideQuery.OnSuccessCallback<Void> onSuccess,
                                           ManageRideQuery.OnFailureCallback onFailure) {
        DocumentReference rideRef = db.collection("rides").document(rideId);
        rideRef.update("status", "unavailable")
                .addOnSuccessListener(aVoid -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure::onFailure);
    }

    public void updateRequestStatus(String rideId, String status,
                                    ManageRideQuery.OnSuccessCallback<Void> onSuccess,
                                    ManageRideQuery.OnFailureCallback onFailure) {
        // Step 1: Query the request with the given rideId
        db.collection("requests")
                .whereEqualTo("rideId", rideId)
                .get()
                .addOnSuccessListener(requestSnapshot -> {
                    // Step 2: Update the status of the request to "complete"
                    for (QueryDocumentSnapshot requestDoc : requestSnapshot) {
                        DocumentReference requestRef = db.collection("requests").document(requestDoc.getId());
                        requestRef.update("status", status)
                                .addOnSuccessListener(aVoid -> onSuccess.onSuccess(null))
                                .addOnFailureListener(onFailure::onFailure);
                    }
                })
                .addOnFailureListener(onFailure::onFailure);
    }

    public void removePendingRequests(
            OnSuccessCallback<Void> onSuccess,
            OnFailureCallback onFailure
    ) {
        CollectionReference requestsCollection = db.collection("requests");

        requestsCollection
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot) {
                        String requestId = document.getId();
                        removeRequest(requestId);
                    }

                    onSuccess.onSuccess(null);
                })
                .addOnFailureListener(onFailure::onFailure);
    }

    private void removeRequest(String requestId) {
        db.collection("requests")
                .document(requestId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Document successfully deleted
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }







    public interface OnSuccessCallback<T> {
        void onSuccess(T result);
    }

    public interface OnFailureCallback {
        void onFailure(Exception exception);
    }
}


