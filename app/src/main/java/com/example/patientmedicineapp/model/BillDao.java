package com.example.patientmedicineapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillDao {
    @Insert
    void insert(Bill bill);

    @Update
    void update(Bill bill);

    @Delete
    void delete(Bill bill);

    @Query("SELECT * FROM Bill WHERE id = :id")
    Bill getBillById(int id);

    @Query("SELECT * FROM Bill WHERE patientId = :patientId")
    List<Bill> getBillsForPatient(int patientId);

    @Query("SELECT * FROM Bill")
    List<Bill> getAllBills();
}
