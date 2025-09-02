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
        String firstName = patient.firstName != null ? patient.firstName : "";
        String lastName = patient.lastName != null ? patient.lastName : "";
        holder.tvName.setText(firstName + " " + lastName);
        
        String gender = patient.gender != null ? patient.gender : "";
        holder.tvInfo.setText("Age: " + patient.age + ", Gender: " + gender);
        
        String countryCode = patient.countryCode != null ? patient.countryCode : "";
        String contact = patient.contact != null ? patient.contact : "";
        holder.tvContact.setText(countryCode + " " + contact);
        
        String streetAddress = patient.streetAddress != null ? patient.streetAddress : "";
        String city = patient.city != null ? patient.city : "";
        String state = patient.state != null ? patient.state : "";
        String postalCode = patient.postalCode != null ? patient.postalCode : "";
        String country = patient.country != null ? patient.country : "";
        
        String address = "";
        if (!streetAddress.isEmpty()) {
            address = streetAddress;
            if (!city.isEmpty()) address += ", " + city;
            if (!state.isEmpty()) address += ", " + state;
            if (!postalCode.isEmpty()) address += " " + postalCode;
            if (!country.isEmpty()) address += ", " + country;
        }
        holder.tvAddress.setText(address);
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
        TextView tvName, tvInfo, tvContact, tvAddress;
        PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_patient_name);
            tvInfo = itemView.findViewById(R.id.tv_patient_info);
            tvContact = itemView.findViewById(R.id.tv_patient_contact);
            tvAddress = itemView.findViewById(R.id.tv_patient_address);
        }
    }
}
