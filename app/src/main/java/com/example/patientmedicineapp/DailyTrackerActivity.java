package com.example.patientmedicineapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Medicine;
import com.example.patientmedicineapp.model.MedicineTracker;
import com.example.patientmedicineapp.model.Patient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyTrackerActivity extends AppCompatActivity {
    private AppDatabase db;
    private List<Patient> patients;
    private ArrayAdapter<String> patientAdapter;
    private Spinner spinnerPatients;
    private int selectedPatientId = -1;
    private List<Medicine> medicineList = new ArrayList<>();
    private List<MedicineTracker> trackerList = new ArrayList<>();
    private TrackerAdapter trackerAdapter;
    private String today;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tracker);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Daily Medicine Tracker");
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        spinnerPatients = findViewById(R.id.spinner_patients);
        RecyclerView rvTracker = findViewById(R.id.rv_tracker);
        rvTracker.setLayoutManager(new LinearLayoutManager(this));
        trackerAdapter = new TrackerAdapter(medicineList, trackerList, (medicine, checked) -> onMedicineChecked(medicine, checked));
        rvTracker.setAdapter(trackerAdapter);

        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        patients = db.patientDao().getAllPatients();
        List<String> patientNames = new ArrayList<>();
        for (Patient p : patients) patientNames.add(p.firstName + " " + p.lastName);
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientNames);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatients.setAdapter(patientAdapter);
        spinnerPatients.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedPatientId = patients.get(position).id;
                loadMedicinesAndTrackers();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedPatientId = -1;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void loadMedicinesAndTrackers() {
        medicineList.clear();
        trackerList.clear();
        if (selectedPatientId != -1) {
            medicineList.addAll(db.medicineDao().getMedicinesForPatient(selectedPatientId));
            trackerList.addAll(db.medicineTrackerDao().getTrackersForPatientAndDate(selectedPatientId, today));
        }
        trackerAdapter.notifyDataSetChanged();
    }

    private void onMedicineChecked(Medicine medicine, boolean checked) {
        MedicineTracker tracker = db.medicineTrackerDao().getTracker(selectedPatientId, medicine.id, today);
        if (tracker == null) {
            tracker = new MedicineTracker();
            tracker.patientId = selectedPatientId;
            tracker.medicineId = medicine.id;
            tracker.date = today;
            tracker.taken = checked;
            db.medicineTrackerDao().insert(tracker);
        } else {
            tracker.taken = checked;
            db.medicineTrackerDao().update(tracker);
        }
        loadMedicinesAndTrackers();
        Toast.makeText(this, medicine.name + (checked ? " marked as taken" : " marked as not taken"), Toast.LENGTH_SHORT).show();
    }
}
