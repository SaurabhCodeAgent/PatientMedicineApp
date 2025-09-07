package com.example.patientmedicineapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Patient {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String firstName;
    public String lastName;
    public int age;

    public void setId(int id) {
        this.id = id;
    }

    public String gender;
    public String countryCode;
    public String contact;
    public String streetAddress;
    public String city;
    public String state;
    public String postalCode;
    public String country;

    public String getName() {
        String name = "";
        if (firstName != null && !firstName.trim().isEmpty()) {
            name = firstName.trim();
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (!name.isEmpty()) {
                name += " ";
            }
            name += lastName.trim();
        }
        return name.isEmpty() ? "Unknown Patient" : name;
    }

    public int getId() {
        return id;
    }
}
