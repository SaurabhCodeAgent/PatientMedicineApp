package com.example.patientmedicineapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bill {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int patientId;
    public String filePath;
    public String date;
}
