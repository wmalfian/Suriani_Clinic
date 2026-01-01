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

    // Constructor
    public MedicationAdapter(Context context, ArrayList<Medication> medicationList) {
        this.context = context;
        this.medicationList = medicationList;
        this.myDb = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = medicationList.get(position);

        holder.tvMedName.setText(med.getName());
        holder.tvTimeStatus.setText(med.getDateTime() + " - " + med.getDetails());

        // Logic to show/hide buttons based on Status
        if (med.getStatus().equals("Pending")) {
            holder.btnLayout.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setText("Status: Pending");
            holder.tvStatusDisplay.setTextColor(Color.DKGRAY);
        } else {
            // If already taken or missed, hide buttons and show status text
            holder.btnLayout.setVisibility(View.GONE);
            holder.tvStatusDisplay.setText("Status: " + med.getStatus());

            if(med.getStatus().equals("Taken")) {
                holder.tvStatusDisplay.setTextColor(Color.GREEN);
            } else {
                holder.tvStatusDisplay.setTextColor(Color.RED);
            }
        }

        // AMIR'S TASK: Button Click Listeners
        holder.btnTaken.setOnClickListener(v -> {
            boolean isUpdated = myDb.updateStatus(med.getId(), "Taken");
            if (isUpdated) {
                Toast.makeText(context, "Marked as Taken", Toast.LENGTH_SHORT).show();
                // Update the object in the list and refresh the view
                // In a real app, you might reload the data from DB here
                medicationList.get(position).updateStatus("Taken"); // You might need to add a setter in Medication.java
                notifyItemChanged(position);
            }
        });

        holder.btnMissed.setOnClickListener(v -> {
            boolean isUpdated = myDb.updateStatus(med.getId(), "Missed");
            if (isUpdated) {
                Toast.makeText(context, "Marked as Missed", Toast.LENGTH_SHORT).show();
                medicationList.get(position).updateStatus("Missed");
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedName, tvTimeStatus, tvStatusDisplay;
        Button btnTaken, btnMissed;
        View btnLayout; // To hide buttons later

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tvMedName);
            tvTimeStatus = itemView.findViewById(R.id.tvTimeStatus);
            // You might need to add a dedicated status TextView in your item_medication.xml
            // For now, I'm reusing the existing one or assuming you add one.
            tvStatusDisplay = itemView.findViewById(R.id.tvTimeStatus);

            btnTaken = itemView.findViewById(R.id.btnTaken);
            btnMissed = itemView.findViewById(R.id.btnMissed);

            // This is the parent layout of the buttons, to hide them both at once
            btnLayout = itemView.findViewById(R.id.btnTaken).getParent() instanceof View ? (View) itemView.findViewById(R.id.btnTaken).getParent() : null;
        }
    }
}