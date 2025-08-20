package com.example.patientmedicineapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "health_records")
public class WeightRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int patientId;
    private String patientName;
    private double weight;
    private int systolicBP;    // Systolic Blood Pressure
    private int diastolicBP;   // Diastolic Blood Pressure
    private double sugarLevel; // Blood Sugar Level
    private String date;
    private String notes;
    private long timestamp;

    public WeightRecord() {}

    @Ignore
    public WeightRecord(int patientId, String patientName, double weight, String date, String notes) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.weight = weight;
        this.systolicBP = 0;
        this.diastolicBP = 0;
        this.sugarLevel = 0.0;
        this.date = date;
        this.notes = notes;
        this.timestamp = System.currentTimeMillis();
    }

    public WeightRecord(int patientId, String patientName, double weight, int systolicBP, int diastolicBP, double sugarLevel, String date, String notes) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.weight = weight;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
        this.sugarLevel = sugarLevel;
        this.date = date;
        this.notes = notes;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public int getSystolicBP() { return systolicBP; }
    public void setSystolicBP(int systolicBP) { this.systolicBP = systolicBP; }
    
    public int getDiastolicBP() { return diastolicBP; }
    public void setDiastolicBP(int diastolicBP) { this.diastolicBP = diastolicBP; }
    
    public double getSugarLevel() { return sugarLevel; }
    public void setSugarLevel(double sugarLevel) { this.sugarLevel = sugarLevel; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
