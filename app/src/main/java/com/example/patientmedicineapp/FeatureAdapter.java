package com.example.patientmedicineapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder> {
    public static class FeatureItem {
        public int iconRes;
        public String label;
        public FeatureItem(int iconRes, String label) {
            this.iconRes = iconRes;
            this.label = label;
        }
    }
    public interface OnFeatureClickListener {
        void onFeatureClick(int position);
    }
    private List<FeatureItem> features;
    private OnFeatureClickListener listener;
    public FeatureAdapter(List<FeatureItem> features, OnFeatureClickListener listener) {
        this.features = features;
        this.listener = listener;
    }
    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature, parent, false);
        return new FeatureViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        FeatureItem item = features.get(position);
        holder.ivIcon.setImageResource(item.iconRes);
        holder.tvLabel.setText(item.label);
        holder.itemView.setOnClickListener(v -> listener.onFeatureClick(position));
    }
    @Override
    public int getItemCount() {
        return features.size();
    }
    static class FeatureViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvLabel;
        FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_feature_icon);
            tvLabel = itemView.findViewById(R.id.tv_feature_label);
        }
    }
}
