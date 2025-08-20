package com.example.patientmedicineapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Patient;

import java.util.List;

public class PatientDetailsActivity extends AppCompatActivity {
    private AppDatabase db;
    private PatientAdapter adapter;
    private List<Patient> patientList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Patient Details");
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        RecyclerView rvPatients = findViewById(R.id.rv_patients);
        rvPatients.setLayoutManager(new LinearLayoutManager(this));
        patientList = db.patientDao().getAllPatients();
        adapter = new PatientAdapter(patientList, new PatientAdapter.OnPatientClickListener() {
            @Override
            public void onPatientClick(Patient patient) {
                showAddEditDialog(patient);
            }
        });
        rvPatients.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btn_add_patient);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditDialog(null);
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showAddEditDialog(@Nullable Patient patient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_patient, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText etName = view.findViewById(R.id.et_name);
        EditText etAge = view.findViewById(R.id.et_age);
        EditText etGender = view.findViewById(R.id.et_gender);
        EditText etContact = view.findViewById(R.id.et_contact);
        EditText etAddress = view.findViewById(R.id.et_address);
        Button btnSave = view.findViewById(R.id.btn_save_patient);

        if (patient != null) {
            etName.setText(patient.name);
            etAge.setText(String.valueOf(patient.age));
            etGender.setText(patient.gender);
            etContact.setText(patient.contact);
            etAddress.setText(patient.address);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String ageStr = etAge.getText().toString().trim();
                String gender = etGender.getText().toString().trim();
                String contact = etContact.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(gender)) {
                    Toast.makeText(PatientDetailsActivity.this, "Name, Age, Gender required", Toast.LENGTH_SHORT).show();
                    return;
                }
                int age = Integer.parseInt(ageStr);
                if (patient == null) {
                    Patient newPatient = new Patient();
                    newPatient.name = name;
                    newPatient.age = age;
                    newPatient.gender = gender;
                    newPatient.contact = contact;
                    newPatient.address = address;
                    db.patientDao().insert(newPatient);
                } else {
                    patient.name = name;
                    patient.age = age;
                    patient.gender = gender;
                    patient.contact = contact;
                    patient.address = address;
                    db.patientDao().update(patient);
                }
                refreshList();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void refreshList() {
        patientList.clear();
        patientList.addAll(db.patientDao().getAllPatients());
        adapter.notifyDataSetChanged();
    }
}
