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

public class PassengerHistoryTracking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_history_tracking);
        replaceFragment(new PassengerOngoingTripsFrag());


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button activitiesBtn = findViewById(R.id.mainBtn01);
        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(PassengerHistoryTracking.this, PassengerMainPage.class);
                startActivity(goToMain);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button profileBtn = findViewById(R.id.profileBtn01);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotToProfile = new Intent(PassengerHistoryTracking.this, PassengerProfileView.class);
                startActivity(gotToProfile);
            }
        });
        Button buttonShowOngoingTrips = findViewById(R.id.buttonShowOngoingTrips);
        Button buttonShowHistory = findViewById(R.id.buttonShowHistory);

        buttonShowOngoingTrips.setOnClickListener(v ->
                replaceFragment(new PassengerOngoingTripsFrag()));


        buttonShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PassengerHistoryTripsFrag());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    }
