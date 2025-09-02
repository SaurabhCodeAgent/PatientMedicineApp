package com.example.patientmedicineapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.patientmedicineapp.model.AppDatabase;
import com.example.patientmedicineapp.model.DoctorAppointment;
import com.example.patientmedicineapp.model.Patient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorAppointmentActivity extends AppCompatActivity {
    private AppDatabase db;
    private List<Patient> patients;
    private ArrayAdapter<String> patientAdapter;
    private Spinner spinnerPatients;
    private int selectedPatientId = -1;
    private List<DoctorAppointment> appointmentList = new ArrayList<>();
    private AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Doctor Appointments");
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "patient_medicine_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        spinnerPatients = findViewById(R.id.spinner_patients);
        Button btnAddAppointment = findViewById(R.id.btn_add_appointment);
        RecyclerView rvAppointments = findViewById(R.id.rv_appointments);
        
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        appointmentAdapter = new AppointmentAdapter(appointmentList, new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onEditAppointment(DoctorAppointment appointment) {
                showAddEditDialog(appointment);
            }

            @Override
            public void onDeleteAppointment(DoctorAppointment appointment) {
                showDeleteConfirmation(appointment);
            }
        });
        rvAppointments.setAdapter(appointmentAdapter);

        patients = db.patientDao().getAllPatients();
        List<String> patientNames = new ArrayList<>();
        for (Patient p : patients) patientNames.add(p.firstName + " " + p.lastName);
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientNames);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatients.setAdapter(patientAdapter);
        spinnerPatients.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedPatientId = patients.get(position).id;
                loadAppointments();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedPatientId = -1;
            }
        });

        btnAddAppointment.setOnClickListener(v -> {
            if (selectedPatientId == -1) {
                Toast.makeText(this, "Select a patient first", Toast.LENGTH_SHORT).show();
                return;
            }
            showAddEditDialog(null);
        });
    }

    private void showAddEditDialog(@Nullable DoctorAppointment appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_appointment, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText etDoctorName = view.findViewById(R.id.et_doctor_name);
        EditText etDoctorSpecialty = view.findViewById(R.id.et_doctor_specialty);
        EditText etAppointmentDate = view.findViewById(R.id.et_appointment_date);
        EditText etAppointmentTime = view.findViewById(R.id.et_appointment_time);
        EditText etNotes = view.findViewById(R.id.et_notes);
        CheckBox cbReminder = view.findViewById(R.id.cb_reminder);
        Button btnSave = view.findViewById(R.id.btn_save_appointment);

        if (appointment != null) {
            etDoctorName.setText(appointment.doctorName);
            etDoctorSpecialty.setText(appointment.doctorSpecialty);
            etAppointmentDate.setText(appointment.appointmentDate);
            etAppointmentTime.setText(appointment.appointmentTime);
            etNotes.setText(appointment.notes);
            cbReminder.setChecked(appointment.reminderSet);
        }

        btnSave.setOnClickListener(v -> {
            String doctorName = etDoctorName.getText().toString().trim();
            String doctorSpecialty = etDoctorSpecialty.getText().toString().trim();
            String appointmentDate = etAppointmentDate.getText().toString().trim();
            String appointmentTime = etAppointmentTime.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();
            boolean reminderSet = cbReminder.isChecked();

            if (TextUtils.isEmpty(doctorName) || TextUtils.isEmpty(appointmentDate) || TextUtils.isEmpty(appointmentTime)) {
                Toast.makeText(this, "Doctor name, date and time are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (appointment == null) {
                DoctorAppointment newAppointment = new DoctorAppointment();
                newAppointment.patientId = selectedPatientId;
                newAppointment.doctorName = doctorName;
                newAppointment.doctorSpecialty = doctorSpecialty;
                newAppointment.appointmentDate = appointmentDate;
                newAppointment.appointmentTime = appointmentTime;
                newAppointment.notes = notes;
                newAppointment.reminderSet = reminderSet;
                db.doctorAppointmentDao().insert(newAppointment);
                
                if (reminderSet) {
                    scheduleAppointmentReminder(newAppointment);
                }
            } else {
                appointment.doctorName = doctorName;
                appointment.doctorSpecialty = doctorSpecialty;
                appointment.appointmentDate = appointmentDate;
                appointment.appointmentTime = appointmentTime;
                appointment.notes = notes;
                appointment.reminderSet = reminderSet;
                db.doctorAppointmentDao().update(appointment);
                
                if (reminderSet) {
                    scheduleAppointmentReminder(appointment);
                }
            }
            
            loadAppointments();
            dialog.dismiss();
            Toast.makeText(this, "Appointment saved", Toast.LENGTH_SHORT).show();
        });
        
        dialog.show();
    }

    private void showDeleteConfirmation(DoctorAppointment appointment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Appointment")
                .setMessage("Are you sure you want to delete this appointment?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.doctorAppointmentDao().delete(appointment);
                    loadAppointments();
                    Toast.makeText(this, "Appointment deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadAppointments() {
        appointmentList.clear();
        if (selectedPatientId != -1) {
            appointmentList.addAll(db.doctorAppointmentDao().getAppointmentsForPatient(selectedPatientId));
        }
        appointmentAdapter.notifyDataSetChanged();
    }

    private void scheduleAppointmentReminder(DoctorAppointment appointment) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date appointmentDateTime = dateFormat.parse(appointment.appointmentDate + " " + appointment.appointmentTime);
            
            if (appointmentDateTime != null) {
                // Set reminder 1 hour before appointment
                Calendar reminderTime = Calendar.getInstance();
                reminderTime.setTime(appointmentDateTime);
                reminderTime.add(Calendar.HOUR, -1);
                
                if (reminderTime.getTimeInMillis() > System.currentTimeMillis()) {
                    Intent intent = new Intent(this, AppointmentReminderReceiver.class);
                    intent.putExtra("appointment_id", appointment.id);
                    intent.putExtra("patient_name", getPatientName(appointment.patientId));
                    intent.putExtra("doctor_name", appointment.doctorName);
                    
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, appointment.id, intent, 
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), pendingIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPatientName(int patientId) {
        for (Patient p : patients) {
            if (p.id == patientId) return p.firstName + " " + p.lastName;
        }
        return "Unknown Patient";
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
