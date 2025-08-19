package com.example.patientmedicineapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Medicine;
import com.example.patientmedicineapp.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class StockStatusActivity extends AppCompatActivity {
    private AppDatabase db;
    private List<Patient> patients;
    private List<Medicine> medicines = new ArrayList<>();
    private ArrayAdapter<String> patientAdapter;
    private Spinner spinnerPatients;
    private int selectedPatientId = -1;
    private StockAdapter stockAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_status);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").allowMainThreadQueries().build();
        spinnerPatients = findViewById(R.id.spinner_patients);
        RecyclerView rvStock = findViewById(R.id.rv_stock);
        rvStock.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter(medicines);
        rvStock.setAdapter(stockAdapter);

        patients = db.patientDao().getAllPatients();
        List<String> patientNames = new ArrayList<>();
        for (Patient p : patients) patientNames.add(p.name);
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientNames);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatients.setAdapter(patientAdapter);
        spinnerPatients.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedPatientId = patients.get(position).id;
                loadMedicines();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedPatientId = -1;
            }
        });
    }

    private void loadMedicines() {
        medicines.clear();
        if (selectedPatientId != -1) {
            medicines.addAll(db.medicineDao().getMedicinesForPatient(selectedPatientId));
        }
        stockAdapter.notifyDataSetChanged();
    }
}
