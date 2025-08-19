package com.example.patientmedicineapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Medicine {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String dosage;
    public int quantity;
    public int patientId;
    public String time; // e.g., "08:00,13:00,20:00"
}
