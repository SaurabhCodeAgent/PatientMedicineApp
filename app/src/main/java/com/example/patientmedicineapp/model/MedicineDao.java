package com.example.patientmedicineapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicineDao {
    @Insert
    void insert(Medicine medicine);

    @Update
    void update(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Query("SELECT * FROM Medicine WHERE id = :id")
    Medicine getMedicineById(int id);

    @Query("SELECT * FROM Medicine WHERE patientId = :patientId")
    List<Medicine> getMedicinesForPatient(int patientId);

    @Query("SELECT * FROM Medicine")
    List<Medicine> getAllMedicines();
}
