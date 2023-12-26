package com.example.carpoolproject.Passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carpoolproject.R;

public class PassengerPayment extends AppCompatActivity {

    TextView paymentMethod, pickUpLocation,dropOffLocation;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pickUpLocation=findViewById(R.id.textViewPickupPointValue);
        dropOffLocation=findViewById(R.id.textViewDestinationValue);
        paymentMethod=findViewById(R.id.paymentMethodValue);

        Intent getInfo =getIntent();

        String p1=getInfo.getStringExtra("pickup");
        String p2=getInfo.getStringExtra("dropoff");

        pickUpLocation.setText(p1);
        dropOffLocation.setText(p2);
        paymentMethod.setText(getInfo.getStringExtra("paymentType"));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button loginBtn = findViewById(R.id.doneBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////add authentication -----

                Intent intent = new Intent(PassengerPayment.this, PassengerMainPage.class);
                startActivity(intent);
            }
        });
    }
}