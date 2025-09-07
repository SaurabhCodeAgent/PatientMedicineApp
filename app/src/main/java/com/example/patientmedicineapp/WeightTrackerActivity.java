package com.example.patientmedicineapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Patient;
import com.example.patientmedicineapp.model.WeightRecord;
import com.example.patientmedicineapp.model.WeightRecordDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeightTrackerActivity extends AppCompatActivity implements WeightRecordAdapter.OnWeightRecordActionListener {
    private AppDatabase db;
    private Spinner spinnerPatients;
    private EditText etWeight, etDate, etNotes, etSystolicBP, etDiastolicBP, etSugarLevel;
    private Button btnAddWeight;
    private RecyclerView rvWeightRecords;
    private WeightRecordAdapter adapter;
    private List<WeightRecord> weightRecords;
    private List<Patient> patients;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);

        try {
            // Setup action bar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Health Tracker");
            }

            // Initialize database
            db = Room.databaseBuilder(getApplicationContext(), 
                AppDatabase.class, 
                "patient_medicine_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

            // Initialize views
            initViews();
            setupDatePicker();
            
            // Load data after views are initialized
            loadPatients();
            loadWeightRecords();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing Health Tracker: " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }

        // Setup click listeners
        btnAddWeight.setOnClickListener(v -> addWeightRecord());
    }

    private void initViews() {
        spinnerPatients = findViewById(R.id.spinner_patients);
        etWeight = findViewById(R.id.et_weight);
        etDate = findViewById(R.id.et_date);
        etNotes = findViewById(R.id.et_notes);
        etSystolicBP = findViewById(R.id.et_systolic_bp);
        etDiastolicBP = findViewById(R.id.et_diastolic_bp);
        etSugarLevel = findViewById(R.id.et_sugar_level);
        btnAddWeight = findViewById(R.id.btn_add_weight);
        rvWeightRecords = findViewById(R.id.rv_weight_records);

        // Setup RecyclerView
        weightRecords = new ArrayList<>();
        adapter = new WeightRecordAdapter(weightRecords, this);
        rvWeightRecords.setLayoutManager(new LinearLayoutManager(this));
        rvWeightRecords.setAdapter(adapter);

        // Setup refresh button
        Button btnRefreshPatients = findViewById(R.id.btn_refresh_patients);
        btnRefreshPatients.setOnClickListener(v -> {
            loadPatients();
            Toast.makeText(this, "Refreshing patient list...", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupDatePicker() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        
        // Set today's date
        etDate.setText(dateFormat.format(calendar.getTime()));

        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    etDate.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void loadPatients() {
        try {
            // First check if database is accessible
            if (db == null) {
                Toast.makeText(this, "Database not initialized", Toast.LENGTH_LONG).show();
                return;
            }

            patients = db.patientDao().getAllPatients();
            List<String> patientNames = new ArrayList<>();
            
            if (patients != null && !patients.isEmpty()) {
                for (Patient patient : patients) {
                    if (patient != null && patient.getName() != null && !patient.getName().trim().isEmpty()) {
                        patientNames.add(patient.getName().trim());
                    }
                }
                
                if (!patientNames.isEmpty()) {
                    ArrayAdapter<String> patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientNames);
                    patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPatients.setAdapter(patientAdapter);
                    
                    // Set default selection to first patient
                    spinnerPatients.setSelection(0);
                    
                    Toast.makeText(this, "Loaded " + patientNames.size() + " patients", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No valid patient names found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No patients found in database. Please add patients first.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading patients: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void loadWeightRecords() {
        weightRecords.clear();
        weightRecords.addAll(db.weightRecordDao().getAllWeightRecords());
        adapter.notifyDataSetChanged();
    }

    private void addWeightRecord() {
        if (spinnerPatients.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a patient first", Toast.LENGTH_LONG).show();
            return;
        }

        String weightStr = etWeight.getText().toString().trim();
        if (weightStr.isEmpty()) {
            Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            if (weight <= 0) {
                Toast.makeText(this, "Weight must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            String patientName = spinnerPatients.getSelectedItem().toString();
            String date = etDate.getText().toString();
            String notes = etNotes.getText().toString().trim();

            // Get blood pressure values
            int systolicBP = 0;
            int diastolicBP = 0;
            String systolicStr = etSystolicBP.getText().toString().trim();
            String diastolicStr = etDiastolicBP.getText().toString().trim();
            
            if (!systolicStr.isEmpty()) {
                try {
                    systolicBP = Integer.parseInt(systolicStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid systolic blood pressure", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            
            if (!diastolicStr.isEmpty()) {
                try {
                    diastolicBP = Integer.parseInt(diastolicStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid diastolic blood pressure", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Get sugar level
            double sugarLevel = 0.0;
            String sugarStr = etSugarLevel.getText().toString().trim();
            if (!sugarStr.isEmpty()) {
                try {
                    sugarLevel = Double.parseDouble(sugarStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid blood sugar level", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Find patient ID
            int patientId = -1;
            for (Patient patient : patients) {
                if (patient.getName().equals(patientName)) {
                    patientId = patient.getId();
                    break;
                }
            }

            if (patientId == -1) {
                Toast.makeText(this, "Patient not found. Please refresh patient list.", Toast.LENGTH_LONG).show();
                return;
            }

            WeightRecord weightRecord = new WeightRecord(patientId, patientName, weight, systolicBP, diastolicBP, sugarLevel, date, notes);
            db.weightRecordDao().insert(weightRecord);

            Toast.makeText(this, "Health record added successfully for " + patientName, Toast.LENGTH_SHORT).show();
            
            // Clear inputs
            etWeight.setText("");
            etSystolicBP.setText("");
            etDiastolicBP.setText("");
            etSugarLevel.setText("");
            etNotes.setText("");
            calendar = Calendar.getInstance();
            etDate.setText(dateFormat.format(calendar.getTime()));
            
            // Reload records
            loadWeightRecords();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid weight", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEdit(WeightRecord weightRecord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_weight, null);
        
        EditText etEditWeight = dialogView.findViewById(R.id.et_edit_weight);
        EditText etEditDate = dialogView.findViewById(R.id.et_edit_date);
        EditText etEditNotes = dialogView.findViewById(R.id.et_edit_notes);
        EditText etEditSystolicBP = dialogView.findViewById(R.id.et_edit_systolic_bp);
        EditText etEditDiastolicBP = dialogView.findViewById(R.id.et_edit_diastolic_bp);
        EditText etEditSugarLevel = dialogView.findViewById(R.id.et_edit_sugar_level);
        
        etEditWeight.setText(String.valueOf(weightRecord.getWeight()));
        etEditDate.setText(weightRecord.getDate());
        etEditNotes.setText(weightRecord.getNotes());
        
        if (weightRecord.getSystolicBP() > 0) {
            etEditSystolicBP.setText(String.valueOf(weightRecord.getSystolicBP()));
        }
        if (weightRecord.getDiastolicBP() > 0) {
            etEditDiastolicBP.setText(String.valueOf(weightRecord.getDiastolicBP()));
        }
        if (weightRecord.getSugarLevel() > 0) {
            etEditSugarLevel.setText(String.valueOf(weightRecord.getSugarLevel()));
        }

        // Setup date picker for edit dialog
        Calendar editCalendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdf.parse(weightRecord.getDate());
            if (date != null) {
                editCalendar.setTime(date);
            }
        } catch (Exception e) {
            // Use current date if parsing fails
        }

        etEditDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    editCalendar.set(Calendar.YEAR, year);
                    editCalendar.set(Calendar.MONTH, month);
                    editCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    etEditDate.setText(dateFormat.format(editCalendar.getTime()));
                },
                editCalendar.get(Calendar.YEAR),
                editCalendar.get(Calendar.MONTH),
                editCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        builder.setView(dialogView)
                .setTitle("Edit Health Record")
                .setPositiveButton("Update", (dialog, which) -> {
                    try {
                        double newWeight = Double.parseDouble(etEditWeight.getText().toString().trim());
                        if (newWeight <= 0) {
                            Toast.makeText(this, "Weight must be greater than 0", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get blood pressure values
                        int newSystolicBP = 0;
                        int newDiastolicBP = 0;
                        String systolicStr = etEditSystolicBP.getText().toString().trim();
                        String diastolicStr = etEditDiastolicBP.getText().toString().trim();
                        
                        if (!systolicStr.isEmpty()) {
                            newSystolicBP = Integer.parseInt(systolicStr);
                        }
                        if (!diastolicStr.isEmpty()) {
                            newDiastolicBP = Integer.parseInt(diastolicStr);
                        }

                        // Get sugar level
                        double newSugarLevel = 0.0;
                        String sugarStr = etEditSugarLevel.getText().toString().trim();
                        if (!sugarStr.isEmpty()) {
                            newSugarLevel = Double.parseDouble(sugarStr);
                        }

                        weightRecord.setWeight(newWeight);
                        weightRecord.setSystolicBP(newSystolicBP);
                        weightRecord.setDiastolicBP(newDiastolicBP);
                        weightRecord.setSugarLevel(newSugarLevel);
                        weightRecord.setDate(etEditDate.getText().toString());
                        weightRecord.setNotes(etEditNotes.getText().toString().trim());
                        weightRecord.setTimestamp(System.currentTimeMillis());

                        db.weightRecordDao().update(weightRecord);
                        Toast.makeText(this, "Health record updated successfully", Toast.LENGTH_SHORT).show();
                        loadWeightRecords();

                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDelete(WeightRecord weightRecord) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Health Record")
                .setMessage("Are you sure you want to delete this health record?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.weightRecordDao().delete(weightRecord);
                    Toast.makeText(this, "Health record deleted successfully", Toast.LENGTH_SHORT).show();
                    loadWeightRecords();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
