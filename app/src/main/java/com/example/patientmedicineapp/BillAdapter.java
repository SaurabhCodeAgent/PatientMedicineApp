package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmedicineapp.model.Bill;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private List<Bill> bills;
    public BillAdapter(List<Bill> bills) {
        this.bills = bills;
    }
    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.tvFileName.setText(bill.filePath.substring(bill.filePath.lastIndexOf("/") + 1));
        holder.tvFileDate.setText(bill.date);
    }
    @Override
    public int getItemCount() {
        return bills.size();
    }
    static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName, tvFileDate;
        BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
            tvFileDate = itemView.findViewById(R.id.tv_file_date);
        }
    }
}
