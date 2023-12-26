package com.example.carpoolproject.Model.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.carpoolproject.Driver.Driver;
import com.example.carpoolproject.Passenger.Passenger;

@Database(entities = {Passenger.class, Driver.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}

