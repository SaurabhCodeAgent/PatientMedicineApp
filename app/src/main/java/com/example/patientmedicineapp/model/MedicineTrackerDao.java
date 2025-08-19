package com.example.patientmedicineapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicineTrackerDao {
    @Insert
    void insert(MedicineTracker tracker);

    @Update
    void update(MedicineTracker tracker);

    @Query("SELECT * FROM MedicineTracker WHERE patientId = :patientId AND date = :date")
    List<MedicineTracker> getTrackersForPatientAndDate(int patientId, String date);

    @Query("SELECT * FROM MedicineTracker WHERE patientId = :patientId AND medicineId = :medicineId AND date = :date LIMIT 1")
    MedicineTracker getTracker(int patientId, int medicineId, String date);
}
