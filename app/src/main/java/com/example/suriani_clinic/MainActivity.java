package com.example.suriani_clinic;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton btnAddNew;
    ImageButton btnHistory;
    DatabaseHelper myDb;
    ArrayList<Medication> medList;
    MedicationAdapter adapter;
    TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure this XML has a RecyclerView with id "recyclerView"

        recyclerView = findViewById(R.id.recyclerView);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnHistory = findViewById(R.id.btnViewHistory);// Add a button in activity_main.xml to go to Add Activity
        myDb = new DatabaseHelper(this);
        medList = new ArrayList<>();
        tvEmptyState = findViewById(R.id.tvEmptyState);

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMedicationActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning from "Add Activity"
        loadData();
    }

    void loadData() {
        medList.clear();

        // OLD CODE: Cursor cursor = myDb.getAllHistory();
        // NEW CODE: Fetch only items that haven't been taken yet
        Cursor cursor = myDb.getPendingMedications();

        if (cursor != null && cursor.getCount() > 0) {
            tvEmptyState.setVisibility(View.GONE);//hide message if we have meds
            while (cursor.moveToNext()) {
                // ... existing loop code ...
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String details = cursor.getString(2);
                String time = cursor.getString(3);
                String status = cursor.getString(4);

                medList.add(new Medication(id, name, details, time, status));
            }
            cursor.close();
        }

        // ... adapter setup ...
        adapter = new MedicationAdapter(this, medList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}