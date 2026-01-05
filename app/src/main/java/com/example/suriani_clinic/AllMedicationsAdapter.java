package com.example.suriani_clinic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Adapter for the RecyclerView in AllMedicationsActivity, which shows all unique medications.
public class AllMedicationsAdapter extends RecyclerView.Adapter<AllMedicationsAdapter.ViewHolder> {

    // The application context.
    private Context context;
    // The list of all medications.
    private ArrayList<Medication> fullList;
    // A helper class to manage database interactions.
    private DatabaseHelper myDb;

    // Constructor for the adapter.
    public AllMedicationsAdapter(Context context, ArrayList<Medication> fullList) {
        this.context = context;
        this.fullList = fullList;
        // Initialize the database helper.
        this.myDb = new DatabaseHelper(context);
    }

    // This method is called when the RecyclerView needs a new ViewHolder.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item in the list.
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_row, parent, false);
        return new ViewHolder(view);
    }

    // This method is called to bind the data to the views of a ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the medication object for the current position.
        Medication med = fullList.get(position);

        // Set the medication name and details in the corresponding TextViews.
        holder.tvName.setText(med.getName());
        holder.tvTime.setText(med.getDateTime() + " - " + med.getDetails());

        // Set the status text to "EDIT RECORD" to indicate that the item can be edited.
        holder.tvStatus.setText("EDIT RECORD");
        holder.tvStatus.setTextColor(Color.parseColor("#2196F3")); // Set text color to blue.

        // 1. CLICK TO EDIT: Set a click listener on the item view to open the AddMedicationActivity in edit mode.
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddMedicationActivity.class);
            // Pass the medication data to the activity as extras.
            intent.putExtra("isEditMode", true);
            intent.putExtra("id", med.getId());
            intent.putExtra("name", med.getName());
            intent.putExtra("details", med.getDetails());
            intent.putExtra("time", med.getDateTime());
            // Start the activity.
            context.startActivity(intent);
        });

        // 2. CLICK TO DELETE: Set a click listener on the delete button.
        holder.btnDelete.setVisibility(View.VISIBLE); // Ensure the delete button is visible.
        holder.btnDelete.setOnClickListener(v -> {
            // Show an AlertDialog to confirm the deletion.
            new AlertDialog.Builder(context)
                    .setTitle("Delete Medication")
                    .setMessage("Are you sure you want to delete this record?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Delete the record from the database.
                        Integer deletedRows = myDb.deleteData(med.getId());
                        if (deletedRows > 0) {
                            // If deletion is successful, remove the item from the list and update the RecyclerView.
                            fullList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, fullList.size());
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            // If deletion fails, show an error message.
                            Toast.makeText(context, "Error deleting", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null) // Do nothing on cancel.
                    .show();
        });
    }

    // This method returns the total number of items in the list.
    @Override
    public int getItemCount() { return fullList.size(); }

    // This class holds the views for a single item in the RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder {
        // The TextViews for the medication name, time, and status.
        TextView tvName, tvTime, tvStatus;
        // The button to delete the medication.
        ImageButton btnDelete;

        // The constructor for the ViewHolder.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the views by their IDs.
            tvName = itemView.findViewById(R.id.tvHistoryName);
            tvTime = itemView.findViewById(R.id.tvHistoryTime);
            tvStatus = itemView.findViewById(R.id.tvHistoryStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
