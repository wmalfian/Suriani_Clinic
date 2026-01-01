package com.example.suriani_clinic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Medication> medicationList;
    private DatabaseHelper myDb;

    public MedicationAdapter(Context context, ArrayList<Medication> medicationList) {
        this.context = context;
        this.medicationList = medicationList;
        this.myDb = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // FIX 1: Changed "item_medication" to "item_medication_card" to match your file name
        View view = LayoutInflater.from(context).inflate(R.layout.item_medication_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = medicationList.get(position);

        holder.tvMedName.setText(med.getName());
        holder.tvTimeStatus.setText(med.getDateTime() + " - " + med.getDetails());

        // FIX 2: Logic to handle Visibility of Buttons vs Status Text
        if (med.getStatus().equals("Pending")) {
            holder.btnLayout.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setVisibility(View.VISIBLE); // Show status text
            holder.tvStatusDisplay.setText("Status: Pending");
            holder.tvStatusDisplay.setTextColor(Color.DKGRAY);
        } else {
            // If Taken or Missed, hide buttons, show status
            holder.btnLayout.setVisibility(View.GONE);
            holder.tvStatusDisplay.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setText("Status: " + med.getStatus());

            if (med.getStatus().equals("Taken")) {
                holder.tvStatusDisplay.setTextColor(Color.GREEN); // Or Color.parseColor("#4CAF50")
            } else {
                holder.tvStatusDisplay.setTextColor(Color.RED);   // Or Color.parseColor("#F44336")
            }
        }

        // Button Click Listeners
        holder.btnTaken.setOnClickListener(v -> {
            boolean isUpdated = myDb.updateStatus(med.getId(), "Taken");
            if (isUpdated) {
                Toast.makeText(context, "Marked as Taken", Toast.LENGTH_SHORT).show();
                med.updateStatus("Taken"); // Requires updateStatus() method in Medication.java
                notifyItemChanged(position);
            }
        });

        holder.btnMissed.setOnClickListener(v -> {
            boolean isUpdated = myDb.updateStatus(med.getId(), "Missed");
            if (isUpdated) {
                Toast.makeText(context, "Marked as Missed", Toast.LENGTH_SHORT).show();
                med.updateStatus("Missed");
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    // FIX 3: Updated ViewHolder to match your XML IDs exactly
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedName, tvTimeStatus, tvStatusDisplay;
        Button btnTaken, btnMissed;
        View btnLayout; // This refers to the LinearLayout containing the buttons

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tvMedName);
            tvTimeStatus = itemView.findViewById(R.id.tvTimeStatus);
            tvStatusDisplay = itemView.findViewById(R.id.tvStatusDisplay);
            btnTaken = itemView.findViewById(R.id.btnTaken);
            btnMissed = itemView.findViewById(R.id.btnMissed);
            btnLayout = itemView.findViewById(R.id.layoutButtons); // Matches ID in your XML
        }
    }
}