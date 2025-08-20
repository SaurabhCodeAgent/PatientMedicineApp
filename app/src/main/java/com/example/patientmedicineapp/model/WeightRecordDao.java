package com.example.patientmedicineapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeightRecordDao {
    @Insert
    void insert(WeightRecord weightRecord);

    @Update
    void update(WeightRecord weightRecord);

    @Delete
    void delete(WeightRecord weightRecord);

    @Query("SELECT * FROM health_records ORDER BY timestamp DESC")
    List<WeightRecord> getAllWeightRecords();

    @Query("SELECT * FROM health_records WHERE patientId = :patientId ORDER BY timestamp DESC")
    List<WeightRecord> getWeightRecordsByPatient(int patientId);

    @Query("SELECT * FROM health_records WHERE patientName LIKE '%' || :patientName || '%' ORDER BY timestamp DESC")
    List<WeightRecord> getWeightRecordsByPatientName(String patientName);

    @Query("SELECT * FROM health_records WHERE date = :date ORDER BY timestamp DESC")
    List<WeightRecord> getWeightRecordsByDate(String date);

    @Query("SELECT * FROM health_records WHERE id = :id")
    WeightRecord getWeightRecordById(int id);
}
