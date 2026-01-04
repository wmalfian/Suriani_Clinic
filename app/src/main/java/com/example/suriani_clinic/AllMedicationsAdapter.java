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

public class AllMedicationsAdapter extends RecyclerView.Adapter<AllMedicationsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Medication> fullList;
    private DatabaseHelper myDb;

    public AllMedicationsAdapter(Context context, ArrayList<Medication> fullList) {
        this.context = context;
        this.fullList = fullList;
        this.myDb = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medication med = fullList.get(position);

        holder.tvName.setText(med.getName());
        holder.tvTime.setText(med.getDateTime() + " - " + med.getDetails());

        holder.tvStatus.setText("EDIT RECORD");
        holder.tvStatus.setTextColor(Color.parseColor("#2196F3"));

        // 1. CLICK TO EDIT
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddMedicationActivity.class);
            intent.putExtra("isEditMode", true);
            intent.putExtra("id", med.getId());
            intent.putExtra("name", med.getName());
            intent.putExtra("details", med.getDetails());
            intent.putExtra("time", med.getDateTime());
            context.startActivity(intent);
        });

        // 2. CLICK TO DELETE
        holder.btnDelete.setVisibility(View.VISIBLE); // Ensure it's visible here
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Medication")
                    .setMessage("Are you sure you want to delete this record?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Delete from DB
                        Integer deletedRows = myDb.deleteData(med.getId());
                        if (deletedRows > 0) {
                            // Remove from List and update UI
                            fullList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, fullList.size());
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error deleting", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() { return fullList.size(); }

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