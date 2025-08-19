package com.example.patientmedicineapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPatientDetails = findViewById(R.id.btn_patient_details);
        Button btnUploadPrescription = findViewById(R.id.btn_upload_prescription);
        Button btnManualEntry = findViewById(R.id.btn_manual_entry);
        Button btnDailyTracker = findViewById(R.id.btn_daily_tracker);
        Button btnReminder = findViewById(R.id.btn_reminder);
        Button btnAddMedicine = findViewById(R.id.btn_add_medicine);
        Button btnStockStatus = findViewById(R.id.btn_stock_status);
        Button btnUploadBill = findViewById(R.id.btn_upload_bill);

        btnPatientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PatientDetailsActivity.class));
            }
        });
        btnUploadPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadPrescriptionActivity.class));
            }
        });
        btnManualEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManualEntryActivity.class));
            }
        });
        btnDailyTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DailyTrackerActivity.class));
            }
        });
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MedicineReminderActivity.class));
            }
        });
        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddMedicineActivity.class));
            }
        });
        btnStockStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StockStatusActivity.class));
            }
        });
        btnUploadBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadBillActivity.class));
            }
        });
    }
}
