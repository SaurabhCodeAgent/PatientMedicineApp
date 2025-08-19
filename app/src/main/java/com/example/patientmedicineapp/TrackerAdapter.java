package com.example.patientmedicineapp;

import android.widget.CompoundButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmedicineapp.model.Medicine;
import com.example.patientmedicineapp.model.MedicineTracker;
import java.util.List;

public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.TrackerViewHolder> {
    public interface OnCheckedChangeListener {
        void onCheckedChanged(Medicine medicine, boolean checked);
    }
    private List<Medicine> medicines;
    private List<MedicineTracker> trackers;
    private OnCheckedChangeListener listener;
    public TrackerAdapter(List<Medicine> medicines, List<MedicineTracker> trackers, OnCheckedChangeListener listener) {
        this.medicines = medicines;
        this.trackers = trackers;
        this.listener = listener;
    }
    @NonNull
    @Override
    public TrackerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracker_medicine, parent, false);
        return new TrackerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TrackerViewHolder holder, int position) {
        Medicine med = medicines.get(position);
        holder.tvName.setText(med.name);
        boolean taken = false;
        for (MedicineTracker t : trackers) {
            if (t.medicineId == med.id) {
                taken = t.taken;
                break;
            }
        }
        holder.checkbox.setChecked(taken);
        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            listener.onCheckedChanged(med, isChecked);
        });
    }
    @Override
    public int getItemCount() {
        return medicines.size();
    }
    static class TrackerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox checkbox;
        TrackerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_medicine_name);
            checkbox = itemView.findViewById(R.id.checkbox_taken);
        }
    }
}
