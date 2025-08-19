package com.example.patientmedicineapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MedicineTracker {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int patientId;
    public int medicineId;
    public String date; // yyyy-MM-dd
    public boolean taken;
}
