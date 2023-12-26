package com.example.carpoolproject.Passenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.carpoolproject.R;

public class PassengerMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main_page);
        Passenger_fragment_available_rides fragment =
                (Passenger_fragment_available_rides) getSupportFragmentManager().findFragmentById(R.id.fragment_container_available_rides);

        if (fragment != null) {
            // Now you can access the TextView in the fragment and update its visibility
            fragment.updateTextViewVisibility(View.VISIBLE); // or View.INVISIBLE or View.GONE
        }


        Fragment myFrag = new Passenger_fragment_available_rides();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_available_rides, myFrag);
        transaction.addToBackStack(null);
        transaction.commit();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button activitiesBtn = findViewById(R.id.activitiesBtn01);
        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToActivities = new Intent(PassengerMainPage.this, PassengerHistoryTracking.class);
                startActivity(goToActivities);
            }
            });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button profileBtn = findViewById(R.id.profileBtn01);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent gotToProfile = new Intent(PassengerMainPage.this, PassengerProfileView.class);
            startActivity(gotToProfile);
            }
            });

    }

}