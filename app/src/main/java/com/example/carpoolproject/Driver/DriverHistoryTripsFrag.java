package com.example.carpoolproject.Driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carpoolproject.Adapter.DriverActivitiesAdapter;
import com.example.carpoolproject.Adapter.PassengerActivitiesAdapter;
import com.example.carpoolproject.Model.firebase.ManageRideQuery;
import com.example.carpoolproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class DriverHistoryTripsFrag extends Fragment {

    private RecyclerView recyclerViewOngoingRides;
    ManageRideQuery manageRideQuery = ManageRideQuery.getInstance();



    public DriverHistoryTripsFrag() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_ongoing_history, container, false);

        recyclerViewOngoingRides = view.findViewById(R.id.recycler_passenger_ongoing_history_rides_fragment);
        manageRideQuery.getDriverRide("complete",
                rides ->{
                    PassengerActivitiesAdapter actAdapter = new PassengerActivitiesAdapter(rides);
                    recyclerViewOngoingRides.setAdapter(actAdapter);
                }
                ,
                exception -> {
                    // Handle failure to get driver data
                    Log.e("Driver HistoryTripsFrag", "Error getting driver Rides: " + exception.getMessage());});

        return view;
    }
}