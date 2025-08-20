package com.example.patientmedicineapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvFeatures = findViewById(R.id.rv_features);
        rvFeatures.setLayoutManager(new GridLayoutManager(this, 2));
        List<FeatureAdapter.FeatureItem> features = new ArrayList<>();
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Patient Details"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Upload Prescription"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Manual Medicine Entry"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Daily Medicine Tracker"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Medicine Reminder"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Add Medicine Quantity"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Stock Status"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Upload Bill"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_launcher_foreground, "Doctor Appointments"));
        features.add(new FeatureAdapter.FeatureItem(R.drawable.ic_weight, "Health Tracker"));
        FeatureAdapter adapter = new FeatureAdapter(features, position -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, PatientDetailsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(this, UploadPrescriptionActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(this, ManualEntryActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, DailyTrackerActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(this, MedicineReminderActivity.class));
                    break;
                case 5:
                    startActivity(new Intent(this, AddMedicineActivity.class));
                    break;
                case 6:
                    startActivity(new Intent(this, StockStatusActivity.class));
                    break;
                case 7:
                    startActivity(new Intent(this, UploadBillActivity.class));
                    break;
                case 8:
                    startActivity(new Intent(this, DoctorAppointmentActivity.class));
                    break;
                case 9:
                    startActivity(new Intent(this, WeightTrackerActivity.class));
                    break;
            }
        });
        rvFeatures.setAdapter(adapter);
    }
}
