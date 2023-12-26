package com.example.carpoolproject.Passenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolproject.Model.firebase.ManageUserQuery;
import com.example.carpoolproject.Model.room.UserDao;
import com.example.carpoolproject.Model.room.UserDatabase;
import com.example.carpoolproject.R;
import com.example.carpoolproject.Start.MainActivity;
import com.example.carpoolproject.carpoolProject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PassengerProfileView extends AppCompatActivity {

    TextView pName, pEmail, pPhone,pAge;

    ManageUserQuery manageUserQuery = ManageUserQuery.getInstance();

    private static final Executor executor = Executors.newSingleThreadExecutor();
    UserDatabase userDatabase = carpoolProject.getInstance().getDb();
    final UserDao userDao = userDatabase.userDao();


    public void insertPassenger(Passenger passenger, UserDao userDao) {
        executor.execute(() -> userDao.insertPassenger(passenger));
    }
    public void clearPassengers(Runnable onComplete) {
        executor.execute(() -> {
            userDao.clearPassengers();
            runOnUiThread(onComplete);
        });
    }

    protected void viewProfileInfo() {

            LiveData<Integer> passengerCountLiveData = userDao.getPassengerCount();
            passengerCountLiveData.observe(this, passengerCount -> {
                if (passengerCount != null && passengerCount > 0) {
                    LiveData<List<Passenger>> passengerLiveData = userDao.getAllPassengers(); // Assuming you have a method to get all passengers
                    passengerLiveData.observe(this, passengers -> {
                        if (passengers != null && !passengers.isEmpty()) {
                            Passenger firstPassenger = passengers.get(0);
                            updateUI(firstPassenger);
                        }
                    });
                }
            });
        }



    private void updateUI(Passenger passenger) {
        String passengerName = passenger.getName();
        String passengerPhone = passenger.getMobile();
        String passengerEmail = passenger.getEmail();
        String passengerAge = passenger.getAge();

        pName = findViewById(R.id.nameTextView);
        pPhone = findViewById(R.id.mobileValueTextView);
        pEmail = findViewById(R.id.emailValueTextView);
        pAge = findViewById(R.id.passengerAgeValueTextView);

        pName.setText(passengerName);
        pEmail.setText(passengerEmail);
        pPhone.setText(passengerPhone);
        pAge.setText(passengerAge);

    }

    private void fetchPassengerData(String passengerId) {
        manageUserQuery.getInstance().getPassengerData(passengerId,
                passenger -> {
                    insertPassenger(passenger, userDao);
                    updateUI(passenger);
                },
                exception -> {
                    // Handle failure to get passenger data
                    Log.e("PassengerProfileView", "Error getting passenger data: " + exception.getMessage());
                });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile_view);
        viewProfileInfo();

        Button viewProfileBtn = findViewById(R.id.activitiesBtn01);
        viewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent goToActivities = new Intent(PassengerProfileView.this, PassengerHistoryTracking.class);
                    startActivity(goToActivities);
                }
            });


            Button logOutBtn = findViewById(R.id.logOutBtn);
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearPassengers(() -> {


                    });
                    clearPassengers(() -> {
                        FirebaseAuth.getInstance().signOut(); // Sign out the current user

                        Intent intent = new Intent(PassengerProfileView.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        finish();
                    });
//                     else {
//                        logOutBtn.setEnabled(false);
//                        Toast.makeText(PassengerProfileView.this, "Please check your connection to log out.", Toast.LENGTH_LONG).show();
//                    }
                }

            });


            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            Button bookRide = findViewById(R.id.mainBtn01);
            bookRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToBooking = new Intent(PassengerProfileView.this, PassengerMainPage.class);
                    startActivity(goToBooking);
                }
            });

        }

    }
