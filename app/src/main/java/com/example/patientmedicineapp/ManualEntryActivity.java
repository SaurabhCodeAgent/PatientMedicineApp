package com.example.patientmedicineapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class ManualEntryActivity extends AppCompatActivity {
    private AppDatabase db;
    private List<Patient> patients;
    private ArrayAdapter<String> patientAdapter;
    private Spinner spinnerPatients;
    private int selectedPatientId = -1;
    private List<Medicine> medicineList = new ArrayList<>();
    private MedicineAdapter medicineAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manual Medicine Entry");
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        spinnerPatients = findViewById(R.id.spinner_patients);
        EditText etName = findViewById(R.id.et_medicine_name);
        EditText etDosage = findViewById(R.id.et_dosage);
        EditText etQuantity = findViewById(R.id.et_quantity);
        EditText etTime = findViewById(R.id.et_time);
        Button btnSave = findViewById(R.id.btn_save_medicine);
        RecyclerView rvMedicines = findViewById(R.id.rv_medicines);
        rvMedicines.setLayoutManager(new LinearLayoutManager(this));
        medicineAdapter = new MedicineAdapter(medicineList);
        rvMedicines.setAdapter(medicineAdapter);

        patients = db.patientDao().getAllPatients();
        List<String> patientNames = new ArrayList<>();
        for (Patient p : patients) patientNames.add(p.name);
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientNames);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatients.setAdapter(patientAdapter);
        spinnerPatients.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedPatientId = patients.get(position).id;
                loadMedicines();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedPatientId = -1;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPatientId == -1) {
                    Toast.makeText(ManualEntryActivity.this, "Select a patient first", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = etName.getText().toString().trim();
                String dosage = etDosage.getText().toString().trim();
                String quantityStr = etQuantity.getText().toString().trim();
                String time = etTime.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dosage) || TextUtils.isEmpty(quantityStr) || TextUtils.isEmpty(time)) {
                    Toast.makeText(ManualEntryActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                    return;
                }
                int quantity = Integer.parseInt(quantityStr);
                Medicine med = new Medicine();
                med.name = name;
                med.dosage = dosage;
                med.quantity = quantity;
                med.time = time;
                med.patientId = selectedPatientId;
                db.medicineDao().insert(med);
                etName.setText("");
                etDosage.setText("");
                etQuantity.setText("");
                etTime.setText("");
                loadMedicines();
                Toast.makeText(ManualEntryActivity.this, "Medicine saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMedicines() {
        medicineList.clear();
        if (selectedPatientId != -1) {
            medicineList.addAll(db.medicineDao().getMedicinesForPatient(selectedPatientId));
        }
        medicineAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
