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

        try {
            // Initialize database
            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db")
                    .fallbackToDestructiveMigration() // This will wipe and rebuild the database if the schema changes
                    .allowMainThreadQueries()
                    .build();

            // Initialize the RecyclerView
            RecyclerView rvPatients = findViewById(R.id.rv_patients);
            rvPatients.setLayoutManager(new LinearLayoutManager(this));
            
            // Load patients
            patientList = db.patientDao().getAllPatients();
            adapter = new PatientAdapter(patientList, patient -> showAddEditDialog(patient));
            rvPatients.setAdapter(adapter);

            // Setup Add button
            Button btnAdd = findViewById(R.id.btn_add_patient);
            btnAdd.setOnClickListener(v -> showAddEditDialog(null));
            
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
            return;
        }
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

        EditText etFirstName = view.findViewById(R.id.et_first_name);
        EditText etLastName = view.findViewById(R.id.et_last_name);
        EditText etAge = view.findViewById(R.id.et_age);
        EditText etGender = view.findViewById(R.id.et_gender);
        EditText etCountryCode = view.findViewById(R.id.et_country_code);
        EditText etContact = view.findViewById(R.id.et_contact);
        EditText etStreetAddress = view.findViewById(R.id.et_street_address);
        EditText etCity = view.findViewById(R.id.et_city);
        EditText etState = view.findViewById(R.id.et_state);
        EditText etPostalCode = view.findViewById(R.id.et_postal_code);
        EditText etCountry = view.findViewById(R.id.et_country);
        Button btnSave = view.findViewById(R.id.btn_save_patient);

        if (patient != null) {
            etFirstName.setText(patient.firstName);
            etLastName.setText(patient.lastName);
            etAge.setText(String.valueOf(patient.age));
            etGender.setText(patient.gender);
            etCountryCode.setText(patient.countryCode);
            etContact.setText(patient.contact);
            etStreetAddress.setText(patient.streetAddress);
            etCity.setText(patient.city);
            etState.setText(patient.state);
            etPostalCode.setText(patient.postalCode);
            etCountry.setText(patient.country);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String ageStr = etAge.getText().toString().trim();
                String gender = etGender.getText().toString().trim();
                String countryCode = etCountryCode.getText().toString().trim();
                String contact = etContact.getText().toString().trim();
                String streetAddress = etStreetAddress.getText().toString().trim();
                String city = etCity.getText().toString().trim();
                String state = etState.getText().toString().trim();
                String postalCode = etPostalCode.getText().toString().trim();
                String country = etCountry.getText().toString().trim();
                
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(gender)) {
                    Toast.makeText(PatientDetailsActivity.this, "First Name, Last Name, Age, Gender required", Toast.LENGTH_SHORT).show();
                    return;
                }
                int age = Integer.parseInt(ageStr);
                if (patient == null) {
                    Patient newPatient = new Patient();
                    newPatient.firstName = firstName;
                    newPatient.lastName = lastName;
                    newPatient.age = age;
                    newPatient.gender = gender;
                    newPatient.countryCode = countryCode;
                    newPatient.contact = contact;
                    newPatient.streetAddress = streetAddress;
                    newPatient.city = city;
                    newPatient.state = state;
                    newPatient.postalCode = postalCode;
                    newPatient.country = country;
                    db.patientDao().insert(newPatient);
                } else {
                    patient.firstName = firstName;
                    patient.lastName = lastName;
                    patient.age = age;
                    patient.gender = gender;
                    patient.countryCode = countryCode;
                    patient.contact = contact;
                    patient.streetAddress = streetAddress;
                    patient.city = city;
                    patient.state = state;
                    patient.postalCode = postalCode;
                    patient.country = country;
                    db.patientDao().update(patient);
                }
                refreshList();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void refreshList() {
        try {
            patientList.clear();
            patientList.addAll(db.patientDao().getAllPatients());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error refreshing list: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
