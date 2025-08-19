package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmedicineapp.model.Medicine;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {
    private List<Medicine> medicines;
    public StockAdapter(List<Medicine> medicines) {
        this.medicines = medicines;
    }
    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Medicine med = medicines.get(position);
        holder.tvName.setText(med.name);
        holder.tvDosage.setText("Dosage: " + med.dosage);
        holder.tvQuantity.setText("Quantity: " + med.quantity);
    }
    @Override
    public int getItemCount() {
        return medicines.size();
    }
    static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDosage, tvQuantity;
        StockViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_medicine_name);
            tvDosage = itemView.findViewById(R.id.tv_medicine_dosage);
            tvQuantity = itemView.findViewById(R.id.tv_medicine_quantity);
        }
    }
}
