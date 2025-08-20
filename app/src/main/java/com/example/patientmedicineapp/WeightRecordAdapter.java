package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientmedicineapp.model.WeightRecord;

import java.util.List;

public class WeightRecordAdapter extends RecyclerView.Adapter<WeightRecordAdapter.WeightRecordViewHolder> {
    private List<WeightRecord> weightRecords;
    private OnWeightRecordActionListener listener;

    public interface OnWeightRecordActionListener {
        void onEdit(WeightRecord weightRecord);
        void onDelete(WeightRecord weightRecord);
    }

    public WeightRecordAdapter(List<WeightRecord> weightRecords, OnWeightRecordActionListener listener) {
        this.weightRecords = weightRecords;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeightRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight_record, parent, false);
        return new WeightRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightRecordViewHolder holder, int position) {
        WeightRecord weightRecord = weightRecords.get(position);
        holder.bind(weightRecord);
    }

    @Override
    public int getItemCount() {
        return weightRecords.size();
    }

    class WeightRecordViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPatientName, tvWeight, tvDate, tvNotes, tvBloodPressure, tvSugarLevel;
        private Button btnEdit, btnDelete;

        public WeightRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvWeight = itemView.findViewById(R.id.tv_weight);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvNotes = itemView.findViewById(R.id.tv_notes);
            tvBloodPressure = itemView.findViewById(R.id.tv_blood_pressure);
            tvSugarLevel = itemView.findViewById(R.id.tv_sugar_level);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(WeightRecord weightRecord) {
            tvPatientName.setText(weightRecord.getPatientName());
            tvWeight.setText(String.format("%.1f kg", weightRecord.getWeight()));
            tvDate.setText(weightRecord.getDate());
            
            // Display blood pressure
            if (weightRecord.getSystolicBP() > 0 && weightRecord.getDiastolicBP() > 0) {
                tvBloodPressure.setText(String.format("%d/%d", weightRecord.getSystolicBP(), weightRecord.getDiastolicBP()));
            } else {
                tvBloodPressure.setText("--/--");
            }
            
            // Display sugar level
            if (weightRecord.getSugarLevel() > 0) {
                tvSugarLevel.setText(String.format("%.0f mg/dL", weightRecord.getSugarLevel()));
            } else {
                tvSugarLevel.setText("-- mg/dL");
            }
            
            if (weightRecord.getNotes() != null && !weightRecord.getNotes().isEmpty()) {
                tvNotes.setText(weightRecord.getNotes());
                tvNotes.setVisibility(View.VISIBLE);
            } else {
                tvNotes.setVisibility(View.GONE);
            }

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(weightRecord);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(weightRecord);
                }
            });
        }
    }
}
