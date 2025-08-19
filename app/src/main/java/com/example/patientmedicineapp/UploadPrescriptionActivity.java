package com.example.patientmedicineapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.Bill;
import com.example.patientmedicineapp.model.Patient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UploadPrescriptionActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1001;
    private AppDatabase db;
    private List<Patient> patients;
    private ArrayAdapter<String> patientAdapter;
    private Spinner spinnerPatients;
    private int selectedPatientId = -1;
    private List<Bill> prescriptionList = new ArrayList<>();
    private PrescriptionAdapter prescriptionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").allowMainThreadQueries().build();
        spinnerPatients = findViewById(R.id.spinner_patients);
        Button btnUpload = findViewById(R.id.btn_upload_prescription);
        RecyclerView rvPrescriptions = findViewById(R.id.rv_prescriptions);
        rvPrescriptions.setLayoutManager(new LinearLayoutManager(this));
        prescriptionAdapter = new PrescriptionAdapter(prescriptionList);
        rvPrescriptions.setAdapter(prescriptionAdapter);

        patients = db.patientDao().getAllPatients();
        List<String> patientNames = new ArrayList<>();
        for (Patient p : patients) patientNames.add(p.name);
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientNames);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatients.setAdapter(patientAdapter);
        spinnerPatients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPatientId = patients.get(position).id;
                loadPrescriptions();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPatientId = -1;
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPatientId == -1) {
                    Toast.makeText(UploadPrescriptionActivity.this, "Select a patient first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf,image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select Prescription"), PICK_FILE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String fileName = getFileName(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                File file = new File(getFilesDir(), fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                Bill bill = new Bill();
                bill.patientId = selectedPatientId;
                bill.filePath = file.getAbsolutePath();
                bill.date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                db.billDao().insert(bill);
                loadPrescriptions();
                Toast.makeText(this, "Prescription uploaded", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPrescriptions() {
        prescriptionList.clear();
        if (selectedPatientId != -1) {
            prescriptionList.addAll(db.billDao().getBillsForPatient(selectedPatientId));
        }
        prescriptionAdapter.notifyDataSetChanged();
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
