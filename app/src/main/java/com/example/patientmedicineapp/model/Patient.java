package com.example.patientmedicineapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Patient {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int age;

    public void setId(int id) {
        this.id = id;
    }

    public String gender;
    public String contact;
    public String address;

    public String getName() {
        return "";
    }

    public int getId() {
        return 0;
    }
}
