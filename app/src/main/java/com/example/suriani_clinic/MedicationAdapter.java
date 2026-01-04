package com.example.suriani_clinic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Medication> list;
    private DatabaseHelper myDb;

    public MedicationAdapter(Context context, ArrayList<Medication> list) {
        this.context = context;
        this.list = list;
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
        Medication med = list.get(position);

        holder.tvName.setText(med.getName());
        holder.tvTime.setText(med.getDateTime() + " - " + med.getDetails());

        // Hide the status display initially (since these are all Pending)
        holder.tvStatusDisplay.setVisibility(View.GONE);
        holder.layoutButtons.setVisibility(View.VISIBLE);

        // --- BUTTON LOGIC ---

        // 1. TAKEN BUTTON
        holder.btnTaken.setOnClickListener(v -> {
            // A. Update the Schedule (Hide from Home)
            myDb.updateStatus(med.getId(), "Taken");

            // B. Add to Permanent History Log (New Table)
            myDb.addToHistory(med.getName(), med.getDetails(), "Taken");

            // C. Remove from this list instantly
            removeItem(position);
            Toast.makeText(context, "Marked as Taken", Toast.LENGTH_SHORT).show();
        });

        // 2. MISSED BUTTON
        holder.btnMissed.setOnClickListener(v -> {
            // A. Update the Schedule (Hide from Home)
            myDb.updateStatus(med.getId(), "Missed");

            // B. Add to Permanent History Log (New Table)
            myDb.addToHistory(med.getName(), med.getDetails(), "Missed");

            // C. Remove from this list instantly
            removeItem(position);
            Toast.makeText(context, "Marked as Missed", Toast.LENGTH_SHORT).show();
        });
    }

    // Helper to safely remove item
    private void removeItem(int position) {
        if (position >= 0 && position < list.size()) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvStatusDisplay;
        Button btnTaken, btnMissed;
        LinearLayout layoutButtons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMedName);
            tvTime = itemView.findViewById(R.id.tvTimeStatus);
            tvStatusDisplay = itemView.findViewById(R.id.tvStatusDisplay);
            btnTaken = itemView.findViewById(R.id.btnTaken);
            btnMissed = itemView.findViewById(R.id.btnMissed);
            layoutButtons = itemView.findViewById(R.id.layoutButtons);
        }
    }
}