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

// This adapter is responsible for binding the medication data to the RecyclerView on the home screen.
public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    // The application context.
    private Context context;
    // The list of medications to be displayed.
    private ArrayList<Medication> list;
    // A helper class to manage database interactions.
    private DatabaseHelper myDb;

    // The constructor for the adapter.
    public MedicationAdapter(Context context, ArrayList<Medication> list) {
        this.context = context;
        this.list = list;
        // Initialize the database helper.
        this.myDb = new DatabaseHelper(context);
    }

    // This method is called when the RecyclerView needs a new ViewHolder.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single medication card item.
        View view = LayoutInflater.from(context).inflate(R.layout.item_medication_card, parent, false);
        return new ViewHolder(view);
    }

    // This method is called to bind the data to the views of a ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the medication object for the current position.
        Medication med = list.get(position);

        // Set the medication name and details in the corresponding TextViews.
        holder.tvName.setText(med.getName());
        holder.tvTime.setText(med.getDateTime() + " - " + med.getDetails());

        // Since the items on the home screen are always pending, hide the status display initially.
        holder.tvStatusDisplay.setVisibility(View.GONE);
        // Show the buttons for taking or missing the medication.
        holder.layoutButtons.setVisibility(View.VISIBLE);

        // --- BUTTON LOGIC ---

        // 1. TAKEN BUTTON: Set a click listener for the "Taken" button.
        holder.btnTaken.setOnClickListener(v -> {
            // A. Update the status of the medication to "Taken" in the schedule table. This will hide it from the home screen.
            myDb.updateStatus(med.getId(), "Taken");

            // B. Add a new record to the permanent history log.
            myDb.addToHistory(med.getName(), med.getDetails(), "Taken");

            // C. Remove the item from the list instantly to update the UI.
            removeItem(position);
            Toast.makeText(context, "Marked as Taken", Toast.LENGTH_SHORT).show();
        });

        // 2. MISSED BUTTON: Set a click listener for the "Missed" button.
        holder.btnMissed.setOnClickListener(v -> {
            // A. Update the status of the medication to "Missed" in the schedule table. This will hide it from the home screen.
            myDb.updateStatus(med.getId(), "Missed");

            // B. Add a new record to the permanent history log.
            myDb.addToHistory(med.getName(), med.getDetails(), "Missed");

            // C. Remove the item from the list instantly to update the UI.
            removeItem(position);
            Toast.makeText(context, "Marked as Missed", Toast.LENGTH_SHORT).show();
        });
    }

    // A helper method to safely remove an item from the list and notify the adapter.
    private void removeItem(int position) {
        if (position >= 0 && position < list.size()) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        }
    }

    // This method returns the total number of items in the list.
    @Override
    public int getItemCount() {
        return list.size();
    }

    // This class holds the views for a single item in the RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder {
        // The TextViews for the medication name, time, and status display.
        TextView tvName, tvTime, tvStatusDisplay;
        // The buttons for marking the medication as "Taken" or "Missed".
        Button btnTaken, btnMissed;
        // The layout containing the action buttons.
        LinearLayout layoutButtons;

        // The constructor for the ViewHolder.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the views by their IDs.
            tvName = itemView.findViewById(R.id.tvMedName);
            tvTime = itemView.findViewById(R.id.tvTimeStatus);
            tvStatusDisplay = itemView.findViewById(R.id.tvStatusDisplay);
            btnTaken = itemView.findViewById(R.id.btnTaken);
            btnMissed = itemView.findViewById(R.id.btnMissed);
            layoutButtons = itemView.findViewById(R.id.layoutButtons);
        }
    }
}
