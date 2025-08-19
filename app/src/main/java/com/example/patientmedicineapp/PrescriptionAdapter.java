package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmedicineapp.model.Bill;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {
    private List<Bill> prescriptions;
    public PrescriptionAdapter(List<Bill> prescriptions) {
        this.prescriptions = prescriptions;
    }
    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription, parent, false);
        return new PrescriptionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        Bill bill = prescriptions.get(position);
        holder.tvFileName.setText(bill.filePath.substring(bill.filePath.lastIndexOf("/") + 1));
        holder.tvFileDate.setText(bill.date);
    }
    @Override
    public int getItemCount() {
        return prescriptions.size();
    }
    static class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName, tvFileDate;
        PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
            tvFileDate = itemView.findViewById(R.id.tv_file_date);
        }
    }
}
