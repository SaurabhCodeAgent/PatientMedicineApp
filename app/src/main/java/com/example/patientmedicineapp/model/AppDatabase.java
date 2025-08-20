package com.example.patientmedicineapp.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.patientmedicineapp.model.MedicineTracker;
import com.example.patientmedicineapp.model.MedicineTrackerDao;
import com.example.patientmedicineapp.model.DoctorAppointment;
import com.example.patientmedicineapp.model.DoctorAppointmentDao;
import com.example.patientmedicineapp.model.WeightRecord;
import com.example.patientmedicineapp.model.WeightRecordDao;

@Database(entities = {Patient.class, Medicine.class, Bill.class, MedicineTracker.class, DoctorAppointment.class, WeightRecord.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PatientDao patientDao();
    public abstract MedicineDao medicineDao();
    public abstract BillDao billDao();
    public abstract MedicineTrackerDao medicineTrackerDao();
    public abstract DoctorAppointmentDao doctorAppointmentDao();
    public abstract WeightRecordDao weightRecordDao();
}
