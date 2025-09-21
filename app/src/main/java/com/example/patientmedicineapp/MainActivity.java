package com.example.patientmedicineapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        TextView headerTitleTextView = findViewById(R.id.tv_header_title);
        String patientName = getPatientName();
        if (patientName != null) {
            headerTitleTextView.setText(patientName);
        } else {
            headerTitleTextView.setText("Meds Buddy");
        }
        TextView headerPateintDetailsTextView = findViewById(R.id.tv_header_details);
        String pateintDetails  = getpatientDetails();
        if (pateintDetails != null) {
            headerPateintDetailsTextView.setText(pateintDetails);
        } else {
            headerPateintDetailsTextView.setText("Meds Details");
        }


        // Initialize UI components
        EditText etSearch = findViewById(R.id.et_search);
        //Button btnExplore = findViewById(R.id.btn_explore);

        // Set up search functionality (placeholder for now)
        etSearch.setOnClickListener(v -> {
            // TODO: Implement search functionality
        });

        // Set up explore button

        RecyclerView rvFeatures = findViewById(R.id.rv_features);
        rvFeatures.setLayoutManager(new GridLayoutManager(this, 3));
        List<FeatureAdapter.FeatureItem> features = new ArrayList<>();
        features.add(new FeatureAdapter.FeatureItem(R.drawable.pateint_details, "Patient Details"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.uploadprescription, "Upload Prescription"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.medicine_entry, "Manual Medicine Entry"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.medicine_taken, "Daily Medicine Tracker"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.reminder, "Medicine Reminder"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.add_stock, "Add Medicine Quantity"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.stock_status, "Stock Status"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.doctor_appointment, "Doctor Appointments"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.health_tracker, "Health Tracker"));
        FeatureAdapter adapter = new FeatureAdapter(features, position -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, PatientDetailsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(this, UploadPrescriptionActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(this, ManualEntryActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, DailyTrackerActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(this, MedicineReminderActivity.class));
                    break;
                case 5:
                    startActivity(new Intent(this, AddMedicineActivity.class));
                    break;
                case 6:
                    startActivity(new Intent(this, StockStatusActivity.class));
                    break;
                case 7:
                    startActivity(new Intent(this, DoctorAppointmentActivity.class));
                    break;
                case 8:
                    startActivity(new Intent(this, WeightTrackerActivity.class));
                    break;
            }
        });
        rvFeatures.setAdapter(adapter);
    }

    private String getPatientName() {
        List<Patient> patients = db.patientDao().getAllPatients(); // Assuming getAllPatients() returns List<Patient>
        List<String> patientNames = new ArrayList<>();
        for (Patient p : patients) {
            patientNames.add(p.firstName + " " + p.lastName);
        }
        if (!patientNames.isEmpty()) {
            return patientNames.get(0);
        } else {
            return "Meds Buddy";
        }
    }

    private String getpatientDetails() {
        List<Patient> patients = db.patientDao().getAllPatients(); // Assuming getAllPatients() returns List<Patient>
        List<String> patientDetails = new ArrayList<>();
        for (Patient p : patients) {
            patientDetails.add("Age: " + p.age + ", Gender: " + p.gender);
        }
        if (!patientDetails.isEmpty()) {
            return patientDetails.get(0);
        } else {
            return "Meds Buddy";
        }
    }


}
