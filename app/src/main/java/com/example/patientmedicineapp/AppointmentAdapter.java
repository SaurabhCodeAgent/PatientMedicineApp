package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmedicineapp.model.DoctorAppointment;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    public interface OnAppointmentActionListener {
        void onEditAppointment(DoctorAppointment appointment);
        void onDeleteAppointment(DoctorAppointment appointment);
    }
    
    private List<DoctorAppointment> appointments;
    private OnAppointmentActionListener listener;
    
    public AppointmentAdapter(List<DoctorAppointment> appointments, OnAppointmentActionListener listener) {
        this.appointments = appointments;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        DoctorAppointment appointment = appointments.get(position);
        holder.tvDoctorName.setText(appointment.doctorName);
        holder.tvDoctorSpecialty.setText(appointment.doctorSpecialty);
        holder.tvAppointmentDateTime.setText(appointment.appointmentDate + " at " + appointment.appointmentTime);
        holder.tvNotes.setText(appointment.notes);
        
        holder.btnEdit.setOnClickListener(v -> listener.onEditAppointment(appointment));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteAppointment(appointment));
    }
    
    @Override
    public int getItemCount() {
        return appointments.size();
    }
    
    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorName, tvDoctorSpecialty, tvAppointmentDateTime, tvNotes;
        Button btnEdit, btnDelete;
        
        AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvDoctorSpecialty = itemView.findViewById(R.id.tv_doctor_specialty);
            tvAppointmentDateTime = itemView.findViewById(R.id.tv_appointment_datetime);
            tvNotes = itemView.findViewById(R.id.tv_notes);
            btnEdit = itemView.findViewById(R.id.btn_edit_appointment);
            btnDelete = itemView.findViewById(R.id.btn_delete_appointment);
        }
    }
}
