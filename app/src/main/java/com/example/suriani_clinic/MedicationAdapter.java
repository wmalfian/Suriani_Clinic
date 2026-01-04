package com.example.suriani_clinic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_medication_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = medicationList.get(position);

        holder.tvMedName.setText(med.getName());
        holder.tvTimeStatus.setText(med.getDateTime() + " - " + med.getDetails());

        // Card Click Listener (Open Details)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicationDetailActivity.class);
            intent.putExtra("id", med.getId());
            intent.putExtra("name", med.getName());
            intent.putExtra("time", med.getDateTime());
            intent.putExtra("details", med.getDetails());
            intent.putExtra("status", med.getStatus());
            context.startActivity(intent);
        });

        // Visibility Logic
        if (med.getStatus().equals("Pending")) {
            holder.btnLayout.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setVisibility(View.GONE); // Hide status text if buttons are there
        } else {
            holder.btnLayout.setVisibility(View.GONE);
            holder.tvStatusDisplay.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setText("Status: " + med.getStatus());

            if (med.getStatus().equals("Taken")) {
                holder.tvStatusDisplay.setTextColor(Color.GREEN);
            } else {
                holder.tvStatusDisplay.setTextColor(Color.RED);
            }
        }

        // --- BUTTON: TAKEN ---
        holder.btnTaken.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Medication")
                    .setMessage("Have you taken " + med.getName() + "?")
                    .setPositiveButton("Yes, Taken", (dialog, which) -> {
                        boolean isUpdated = myDb.updateStatus(med.getId(), "Taken");
                        if (isUpdated) {
                            Toast.makeText(context, "Great job!", Toast.LENGTH_SHORT).show();
                            // Remove from the list immediately
                            medicationList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, medicationList.size());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // --- BUTTON: MISSED ---
        holder.btnMissed.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Mark as Missed")
                    .setMessage("Are you sure you missed " + med.getName() + "?")
                    .setPositiveButton("Yes, Missed", (dialog, which) -> {
                        boolean isUpdated = myDb.updateStatus(med.getId(), "Missed");
                        if (isUpdated) {
                            Toast.makeText(context, "Marked as missed", Toast.LENGTH_SHORT).show();
                            // Remove from the list immediately
                            medicationList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, medicationList.size());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedName, tvTimeStatus, tvStatusDisplay;
        Button btnTaken, btnMissed;
        View btnLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tvMedName);
            tvTimeStatus = itemView.findViewById(R.id.tvTimeStatus);
            tvStatusDisplay = itemView.findViewById(R.id.tvStatusDisplay);
            btnTaken = itemView.findViewById(R.id.btnTaken);
            btnMissed = itemView.findViewById(R.id.btnMissed);
            btnLayout = itemView.findViewById(R.id.layoutButtons);
        }
    }
}