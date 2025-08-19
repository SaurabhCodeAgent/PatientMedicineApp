package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmedicineapp.model.Patient;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    public interface OnPatientClickListener {
        void onPatientClick(Patient patient);
    }
    private List<Patient> patients;
    private OnPatientClickListener listener;

    public PatientAdapter(List<Patient> patients, OnPatientClickListener listener) {
        this.patients = patients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.tvName.setText(patient.name);
        holder.tvInfo.setText("Age: " + patient.age + ", Gender: " + patient.gender + ", Contact: " + patient.contact);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPatientClick(patient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvInfo;
        PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_patient_name);
            tvInfo = itemView.findViewById(R.id.tv_patient_info);
        }
    }
}
