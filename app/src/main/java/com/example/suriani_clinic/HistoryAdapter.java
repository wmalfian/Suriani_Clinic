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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Medication> historyList;

    public HistoryAdapter(Context context, ArrayList<Medication> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = historyList.get(position);

        // 1. Set Name
        holder.tvName.setText(med.getName());

        // 2. Set Time (This now shows the full Date string from the database)
        // e.g., "Mon, 12 Jan • 08:00 PM"
        holder.tvTime.setText(med.getDateTime());

        // 3. Set Status text & Color
        holder.tvStatus.setText(med.getStatus());

        if (med.getStatus().equalsIgnoreCase("Taken")) {
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C")); // Green
        } else if (med.getStatus().equalsIgnoreCase("Missed")) {
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F")); // Red
        } else {
            holder.tvStatus.setTextColor(Color.DKGRAY);
        }

        // 4. Hide Delete Button (History should be permanent)
        holder.btnDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvStatus;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvHistoryName);
            tvTime = itemView.findViewById(R.id.tvHistoryTime);
            tvStatus = itemView.findViewById(R.id.tvHistoryStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}