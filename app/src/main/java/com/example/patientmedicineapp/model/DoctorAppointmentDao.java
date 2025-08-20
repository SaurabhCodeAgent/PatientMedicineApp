package com.example.patientmedicineapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DoctorAppointmentDao {
    @Insert
    void insert(DoctorAppointment appointment);

    @Update
    void update(DoctorAppointment appointment);

    @Delete
    void delete(DoctorAppointment appointment);

    @Query("SELECT * FROM DoctorAppointment WHERE id = :id")
    DoctorAppointment getAppointmentById(int id);

    @Query("SELECT * FROM DoctorAppointment WHERE patientId = :patientId")
    List<DoctorAppointment> getAppointmentsForPatient(int patientId);

    @Query("SELECT * FROM DoctorAppointment ORDER BY appointmentDate ASC, appointmentTime ASC")
    List<DoctorAppointment> getAllAppointments();

    @Query("SELECT * FROM DoctorAppointment WHERE appointmentDate >= :today ORDER BY appointmentDate ASC, appointmentTime ASC")
    List<DoctorAppointment> getUpcomingAppointments(String today);
}
