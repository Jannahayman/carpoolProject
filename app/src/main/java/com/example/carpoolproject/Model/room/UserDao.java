package com.example.carpoolproject.Model.room;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.carpoolproject.Driver.Driver;
import com.example.carpoolproject.Passenger.Passenger;
import com.example.carpoolproject.carpoolProject;

import java.util.List;

@Dao
public interface UserDao {

    static UserDao getInstance(Context context) {
        UserDatabase database = carpoolProject.getInstance().getDb();
        return database.userDao();
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPassenger(Passenger passenger);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDriver(Driver driver);

    @Query("DELETE FROM passenger_table")
    void clearPassengers();

    @Query("DELETE FROM driver_table")
    void clearDrivers();

    @Query("SELECT * FROM passenger_table")
    LiveData<List<Passenger>> getPassenger();

    @Query("SELECT * FROM driver_table")
    LiveData<List<Driver>> getDriver();

    @Query("SELECT * FROM driver_table WHERE driverId = :driverId")
    LiveData<List<Driver>> getDriverById(String driverId);

    @Query("SELECT * FROM passenger_table WHERE passengerId = :passengerId")
    LiveData<List<Passenger>> getPassengerById(String passengerId);
    @Query("SELECT driverId FROM driver_table LIMIT 1")
    LiveData<String> getDriverId();
    @Query("SELECT passengerId FROM passenger_table LIMIT 1")
    LiveData<String> getPassengerId();

    @Query("SELECT COUNT(*) FROM passenger_table")
    LiveData<Integer> getPassengerCount();
    @Query("SELECT COUNT(*) FROM driver_table")
    LiveData<Integer> getDriverCount();
    @Query("SELECT * FROM passenger_table")
    LiveData<List<Passenger>> getAllPassengers();
    @Query("SELECT * FROM driver_table")
    LiveData<List<Driver>> getAllDrivers();


}
