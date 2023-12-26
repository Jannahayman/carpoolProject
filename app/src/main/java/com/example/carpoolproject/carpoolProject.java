package com.example.carpoolproject;

import android.app.Application;

import androidx.room.Room;

import com.example.carpoolproject.Model.room.UserDatabase;


public class carpoolProject extends Application {

    private static carpoolProject instance;

    private static UserDatabase db;

    public static carpoolProject getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = Room.databaseBuilder(
                getApplicationContext(),
                UserDatabase.class,
                "user_database"
        ).build();
        db.getOpenHelper().getWritableDatabase();

    }

    public UserDatabase getDb(){
        return db;
    }
}
