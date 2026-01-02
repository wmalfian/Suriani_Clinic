package com.example.suriani_clinic;

import android.content.Context;
import android.content.Intent; // Added Import
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
        // Matches your file name "item_medication_card.xml"
        View view = LayoutInflater.from(context).inflate(R.layout.item_medication_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = medicationList.get(position);

        holder.tvMedName.setText(med.getName());
        holder.tvTimeStatus.setText(med.getDateTime() + " - " + med.getDetails());

        // ==================================================================
        // NEW: Click Listener for the whole card (Opens Detail Activity)
        // ==================================================================
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicationDetailActivity.class);

            //for edit
            intent.putExtra("id", med.getId());

            // Pass data to the new activity
            intent.putExtra("name", med.getName());
            intent.putExtra("time", med.getDateTime());
            intent.putExtra("details", med.getDetails());
            intent.putExtra("status", med.getStatus());

            context.startActivity(intent);
        });

        // Logic to handle Visibility of Buttons vs Status Text
        if (med.getStatus().equals("Pending")) {
            holder.btnLayout.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setText("Status: Pending");
            holder.tvStatusDisplay.setTextColor(Color.DKGRAY);
        } else {
            // If Taken or Missed, hide buttons, show status
            holder.btnLayout.setVisibility(View.GONE);
            holder.tvStatusDisplay.setVisibility(View.VISIBLE);
            holder.tvStatusDisplay.setText("Status: " + med.getStatus());

            if (med.getStatus().equals("Taken")) {
                holder.tvStatusDisplay.setTextColor(Color.GREEN);
            } else {
                holder.tvStatusDisplay.setTextColor(Color.RED);
            }
        }

        // Button Click Listeners
        holder.btnTaken.setOnClickListener(v -> {
            boolean isUpdated = myDb.updateStatus(med.getId(), "Taken");
            if (isUpdated) {
                Toast.makeText(context, "Marked as Taken", Toast.LENGTH_SHORT).show();
                med.updateStatus("Taken");
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