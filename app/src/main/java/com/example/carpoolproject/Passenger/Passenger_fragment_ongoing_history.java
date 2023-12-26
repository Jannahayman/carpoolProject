package com.example.carpoolproject.Passenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carpoolproject.R;


public class Passenger_fragment_ongoing_history extends Fragment {


    public Passenger_fragment_ongoing_history() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_passenger_ongoing_history,
                container,
                false);
        return view;
    }
}