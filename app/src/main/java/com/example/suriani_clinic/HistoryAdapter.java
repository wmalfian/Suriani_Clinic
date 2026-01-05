package com.example.suriani_clinic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Adapter to bind the medication history data to the RecyclerView
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    // The context of the calling activity
    private Context context;
    // The list of medication history items
    private ArrayList<Medication> historyList;

    // Constructor to initialize the adapter with the context and history list
    public HistoryAdapter(Context context, ArrayList<Medication> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    // Called when the RecyclerView needs a new ViewHolder to represent an item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single history item
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_row, parent, false);
        return new ViewHolder(view);
    }

    // Called by the RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the medication item at the current position
        Medication med = historyList.get(position);

        // 1. Set Name
        // Set the medication name in the corresponding TextView
        holder.tvName.setText(med.getName());

        // 2. Set Time (This now shows the full Date string from the database)
        // e.g., "Mon, 12 Jan • 08:00 PM"
        // Set the medication date and time in the corresponding TextView
        holder.tvTime.setText(med.getDateTime());

        // 3. Set Status text & Color
        // Set the medication status in the corresponding TextView
        holder.tvStatus.setText(med.getStatus());

        // Change the color of the status text based on its value
        if (med.getStatus().equalsIgnoreCase("Taken")) {
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C")); // Green
        } else if (med.getStatus().equalsIgnoreCase("Missed")) {
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F")); // Red
        } else {
            holder.tvStatus.setTextColor(Color.DKGRAY); // Default color
        }

        // 4. Hide Delete Button (History should be permanent)
        // The delete button is not needed in the history view, so it's made invisible
        holder.btnDelete.setVisibility(View.GONE);
    }

    // Returns the total number of items in the history list
    @Override
    public int getItemCount() {
        return historyList.size();
    }

    // ViewHolder class to hold the views for each history item
    public class ViewHolder extends RecyclerView.ViewHolder {
        // TextViews to display the medication name, time, and status
        TextView tvName, tvTime, tvStatus;
        // The delete button (which is hidden)
        ImageButton btnDelete;

        // Constructor to initialize the views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the views by their IDs from the layout
            tvName = itemView.findViewById(R.id.tvHistoryName);
            tvTime = itemView.findViewById(R.id.tvHistoryTime);
            tvStatus = itemView.findViewById(R.id.tvHistoryStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
