package com.example.patientmedicineapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PatientDao {
    @Insert
    void insert(Patient patient);

    @Update
    void update(Patient patient);

    @Delete
    void delete(Patient patient);

    @Query("SELECT * FROM Patient WHERE id = :id")
    Patient getPatientById(int id);

    @Query("SELECT * FROM Patient")
    List<Patient> getAllPatients();
}
