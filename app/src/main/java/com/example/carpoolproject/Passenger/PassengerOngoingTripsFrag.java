package com.example.carpoolproject.Passenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.carpoolproject.Adapter.PassengerActivitiesAdapter;
import com.example.carpoolproject.Model.firebase.ManageRideQuery;
import com.example.carpoolproject.R;
import com.google.firebase.auth.FirebaseAuth;


public class PassengerOngoingTripsFrag extends Fragment {
    private RecyclerView recyclerViewOngoingRides;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ManageRideQuery manageRideQuery=ManageRideQuery.getInstance();



    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger_ongoing_history, container, false);

        recyclerViewOngoingRides = view.findViewById(R.id.recycler_passenger_ongoing_history_rides_fragment);

        if (!isNetworkConnected()) {
            // If there is no network connection, return the empty view
            return view;
        }

        String passengerId = mAuth.getCurrentUser().getUid();

        manageRideQuery.getPassengerRidesIDs(passengerId,
                rideIds -> {
                    // Check if rideIDs is null or empty
                    if (rideIds == null || rideIds.isEmpty()) {
                        return;
                    } else {
                        System.out.println(rideIds);
                        manageRideQuery.getRidesWithUpdatedStatus(rideIds,
                                rides -> {
                                    // Check if rides is null or empty
                                    if (rides == null || rides.isEmpty()) {
                                        return;
                                    } else {
                                        PassengerActivitiesAdapter actAdapter = new PassengerActivitiesAdapter(rides);
                                        recyclerViewOngoingRides.setAdapter(actAdapter);
                                        actAdapter.notifyDataSetChanged(); // Notify the adapter of the data change
                                    }
                                },
                                exception ->
                                        Log.e("PassengerHistoryTripsFrag", "Error getting passenger Rides: " + exception.getMessage())
                        );
                    }
                },
                exception ->
                        Log.e("PassengerOngoingTripsFrag", "Error getting Ride IDs: " + exception.getMessage())
        );

        return view;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }
}