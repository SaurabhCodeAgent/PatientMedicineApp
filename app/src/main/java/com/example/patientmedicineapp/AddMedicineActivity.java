package com.example.patientmedicineapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Medicine;
import com.example.patientmedicineapp.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class AddMedicineActivity extends AppCompatActivity {
    private AppDatabase db;
    private List<Patient> patients;
    private List<Medicine> medicines = new ArrayList<>();
    private ArrayAdapter<String> patientAdapter, medicineAdapter;
    private Spinner spinnerPatients, spinnerMedicines;
    private int selectedPatientId = -1;
    private int selectedMedicineId = -1;
    private Medicine selectedMedicine = null;
    private TextView tvCurrentStock;
    private EditText etAddQuantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Medicine Quantity");
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        spinnerPatients = findViewById(R.id.spinner_patients);
        spinnerMedicines = findViewById(R.id.spinner_medicines);
        tvCurrentStock = findViewById(R.id.tv_current_stock);
        etAddQuantity = findViewById(R.id.et_add_quantity);
        Button btnAddQuantity = findViewById(R.id.btn_add_quantity);

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
                    selectedMedicine = medicines.get(position);
                    selectedMedicineId = selectedMedicine.id;
                    updateCurrentStock();
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedMedicineId = -1;
                selectedMedicine = null;
                tvCurrentStock.setText("Current Stock: ");
            }
        });

        btnAddQuantity.setOnClickListener(v -> addQuantity());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint({"GestureBackNavigation", "MissingSuperCall"})
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
            selectedMedicine = medicines.get(0);
            selectedMedicineId = selectedMedicine.id;
            updateCurrentStock();
        } else {
            selectedMedicine = null;
            selectedMedicineId = -1;
            tvCurrentStock.setText("Current Stock: ");
        }
    }

    private void updateCurrentStock() {
        if (selectedMedicine != null) {
            tvCurrentStock.setText("Current Stock: " + selectedMedicine.quantity);
        } else {
            tvCurrentStock.setText("Current Stock: ");
        }
    }

    private void addQuantity() {
        if (selectedMedicine == null) {
            Toast.makeText(this, "Select a medicine", Toast.LENGTH_SHORT).show();
            return;
        }
        String qtyStr = etAddQuantity.getText().toString().trim();
        if (TextUtils.isEmpty(qtyStr)) {
            Toast.makeText(this, "Enter quantity to add", Toast.LENGTH_SHORT).show();
            return;
        }
        int addQty = Integer.parseInt(qtyStr);
        selectedMedicine.quantity += addQty;
        db.medicineDao().update(selectedMedicine);
        updateCurrentStock();
        etAddQuantity.setText("");
        Toast.makeText(this, "Quantity updated", Toast.LENGTH_SHORT).show();
    }
}
