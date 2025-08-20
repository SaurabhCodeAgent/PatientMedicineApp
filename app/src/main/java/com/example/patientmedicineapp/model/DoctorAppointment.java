package com.example.patientmedicineapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DoctorAppointment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int patientId;
    public String doctorName;
    public String doctorSpecialty;
    public String appointmentDate; // yyyy-MM-dd
    public String appointmentTime; // HH:mm
    public String notes;
    public boolean reminderSet;
}
