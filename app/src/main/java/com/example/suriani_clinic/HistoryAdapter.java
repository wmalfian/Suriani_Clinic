package com.example.suriani_clinic;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton; // Import this
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Medication> historyList;
    private DatabaseHelper myDb; // 1. Add DB Helper

    public HistoryAdapter(Context context, ArrayList<Medication> historyList) {
        this.context = context;
        this.historyList = historyList;
        this.myDb = new DatabaseHelper(context); // 2. Initialize it
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

        holder.tvName.setText(med.getName());
        holder.tvTime.setText(med.getDateTime() + " - " + med.getDetails());
        holder.tvStatus.setText(med.getStatus());

        // Color Logic
        if (med.getStatus().equalsIgnoreCase("Taken")) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        } else if (med.getStatus().equalsIgnoreCase("Missed")) {
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"));
        } else {
            holder.tvStatus.setTextColor(Color.DKGRAY);
        }

        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(context, MedicationDetailActivity.class);
            intent.putExtra("name", med.getName());
            intent.putExtra("time", med.getDateTime());
            intent.putExtra("details", med.getDetails());
            intent.putExtra("status", med.getStatus());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return historyList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvStatus;
        ImageButton btnDelete; // 4. Declare button

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvHistoryName);
            tvTime = itemView.findViewById(R.id.tvHistoryTime);
            tvStatus = itemView.findViewById(R.id.tvHistoryStatus);
        }
    }
}