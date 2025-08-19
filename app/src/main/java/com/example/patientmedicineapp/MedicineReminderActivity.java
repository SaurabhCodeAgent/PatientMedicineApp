package com.example.patientmedicineapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Medicine;
import com.example.patientmedicineapp.model.Patient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicineReminderActivity extends AppCompatActivity {
    private AppDatabase db;
    private List<Patient> patients;
    private List<Medicine> medicines = new ArrayList<>();
    private ArrayAdapter<String> patientAdapter, medicineAdapter;
    private Spinner spinnerPatients, spinnerMedicines;
    private int selectedPatientId = -1;
    private int selectedMedicineId = -1;
    private String selectedMedicineName = "";
    private TimePicker timePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").allowMainThreadQueries().build();
        spinnerPatients = findViewById(R.id.spinner_patients);
        spinnerMedicines = findViewById(R.id.spinner_medicines);
        timePicker = findViewById(R.id.time_picker);
        Button btnSetReminder = findViewById(R.id.btn_set_reminder);

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

        spinnerMedicines.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position >= 0 && position < medicines.size()) {
                    selectedMedicineId = medicines.get(position).id;
                    selectedMedicineName = medicines.get(position).name;
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedMedicineId = -1;
                selectedMedicineName = "";
            }
        });

        btnSetReminder.setOnClickListener(v -> setReminder());
    }

    private void loadMedicines() {
        medicines.clear();
        if (selectedPatientId != -1) {
            medicines.addAll(db.medicineDao().getMedicinesForPatient(selectedPatientId));
        }
        List<String> medicineNames = new ArrayList<>();
        for (Medicine m : medicines) medicineNames.add(m.name);
        medicineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicineNames);
        medicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicines.setAdapter(medicineAdapter);
        if (!medicines.isEmpty()) {
            selectedMedicineId = medicines.get(0).id;
            selectedMedicineName = medicines.get(0).name;
        }
    }

    private void setReminder() {
        if (selectedPatientId == -1 || selectedMedicineId == -1) {
            Toast.makeText(this, "Select patient and medicine", Toast.LENGTH_SHORT).show();
            return;
        }
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long triggerTime = calendar.getTimeInMillis();
        if (triggerTime < System.currentTimeMillis()) {
            triggerTime += 24 * 60 * 60 * 1000; // next day
        }
        Intent intent = new Intent(this, MedicineReminderReceiver.class);
        intent.putExtra("medicine_name", selectedMedicineName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, selectedMedicineId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        Toast.makeText(this, "Reminder set for " + selectedMedicineName, Toast.LENGTH_SHORT).show();
    }
}
