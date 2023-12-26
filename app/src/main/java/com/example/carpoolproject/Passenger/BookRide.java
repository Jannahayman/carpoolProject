package com.example.carpoolproject.Passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolproject.Model.firebase.ManageRideQuery;
import com.example.carpoolproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.Timestamp;

public class BookRide extends AppCompatActivity {
    ManageRideQuery manageRideQuery = ManageRideQuery.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    TextView pickUpLocation, dropOffLocation, driverName, driverNumber, carModel, carPlate, carColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_book_ride);
        pickUpLocation = findViewById(R.id.textViewPickupPointValue);
        dropOffLocation = findViewById(R.id.textViewDestinationValue);
        driverName = findViewById(R.id.textViewDriverName);
        driverNumber = findViewById(R.id.textViewDriverPhone);
        carColor = findViewById(R.id.textViewCarColor);
        carModel = findViewById(R.id.textViewCarModel);
        carPlate = findViewById(R.id.textViewCarPlate);

        RadioButton cash = findViewById(R.id.radioButtonCash);
        cash.setChecked(true);

        Intent getInfo = getIntent();
        String p1 = getInfo.getStringExtra("pickup");
        String p2 = getInfo.getStringExtra("dropoff");
        String driverId = getInfo.getStringExtra("driverID");

        String name = getInfo.getStringExtra("driverName");
        String phoneNum = getInfo.getStringExtra("driverPhone");
        String carModelValue = getInfo.getStringExtra("carModel");
        String carPlateValue = getInfo.getStringExtra("carPlate");
        String carColorValue = getInfo.getStringExtra("carColor");

        String time = getInfo.getStringExtra("time");
        String status = getInfo.getStringExtra("status");
        long timestampSeconds = getIntent().getLongExtra("timestamp", 0); // Default value is 0
        Timestamp receivedTimestamp = new Timestamp(timestampSeconds, 0);

        double rate = getInfo.getDoubleExtra("rate", 1.2);

        pickUpLocation.setText(p1);
        dropOffLocation.setText(p2);

        driverName.setText(name);
        driverNumber.setText(phoneNum);
        carColor.setText(carColorValue);
        carModel.setText(carModelValue);
        carPlate.setText(carPlateValue);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button bookRide = findViewById(R.id.bookSpotBtn01);
        bookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passengerId = mAuth.getUid();

                manageRideQuery.getRideID(p1, p2, time, driverId, status, receivedTimestamp, rate,
                        rideId -> {
                            FirebaseFirestore.getInstance().collection("rides").document(rideId)
                                    .get()
                                    .addOnSuccessListener(document -> {
                                        if (document.exists()) {
                                            int availableSeats = document.get("available_seats") != null ? document.getLong("available_seats").intValue() : 0;

                                             if (availableSeats >= 1) {
                                                manageRideQuery.addRequestToDatabase(passengerId, rideId, driverId,
                                                        registration -> {
                                                            if ("Already Requested".equals(registration)) {
                                                                Toast.makeText(BookRide.this, "Request already sent", Toast.LENGTH_SHORT).show();
                                                                Intent gotoMain = new Intent(BookRide.this, PassengerMainPage.class);
                                                                startActivity(gotoMain);
                                                            }
                                                            else{
                                                                if(availableSeats==1)
                                                                {

                                                                    manageRideQuery.makeRideUnavailable(rideId,aVoid->{},exception -> {});

                                                                }
                                                            FirebaseFirestore.getInstance().collection("rides").document(rideId)
                                                                    .update("available_seats", FieldValue.increment(-1));
                                                            Intent goToPayment = new Intent(BookRide.this, PassengerPayment.class);

                                                            goToPayment.putExtra("paymentType", getPaymentMethod(cash));
                                                            goToPayment.putExtra("pickup", p1);
                                                            goToPayment.putExtra("dropoff", p2);

                                                            startActivity(goToPayment);
                                                            Log.d("BookRide.bookSpotBtn", "Request Added Successfully");}


                                                        },
                                                        exception ->
                                                                Log.e("BookRide.bookSpotBtn", "Error adding request to database: " + exception.getMessage()));
                                            } else {
                                                // Toast that ride requests are full and return to PassengerMainPage
                                                Toast.makeText(BookRide.this, "Ride requests are full", Toast.LENGTH_SHORT).show();
                                                Intent gotoMain = new Intent(BookRide.this, PassengerMainPage.class);
                                                startActivity(gotoMain);
                                            }
                                        } else {
                                            Log.e("BookRide.bookSpotBtn", "Ride document does not exist");
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Log.e("BookRide.bookSpotBtn", "Error getting ride document: " + e.getMessage()));
                        },
                        exception ->
                                Log.e("BookRide.getRideID", "Error getting RideID: " + exception.getMessage())
                );
            }

            private String getPaymentMethod(RadioButton cash) {
                if (cash.isChecked()) {
                    return "Cash";
                } else {
                    return "Visa";
                }
            }
        });
    }
}
