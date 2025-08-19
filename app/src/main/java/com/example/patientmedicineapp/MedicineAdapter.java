package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmedicineapp.model.Medicine;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private List<Medicine> medicines;
    public MedicineAdapter(List<Medicine> medicines) {
        this.medicines = medicines;
    }
    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine med = medicines.get(position);
        holder.tvName.setText(med.name);
        holder.tvInfo.setText("Dosage: " + med.dosage + ", Qty: " + med.quantity + ", Time: " + med.time);
    }
    @Override
    public int getItemCount() {
        return medicines.size();
    }
    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvInfo;
        MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_medicine_name);
            tvInfo = itemView.findViewById(R.id.tv_medicine_info);
        }
    }
}
