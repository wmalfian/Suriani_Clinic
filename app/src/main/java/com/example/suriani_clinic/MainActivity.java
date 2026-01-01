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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAddNew;
    DatabaseHelper myDb;
    ArrayList<Medication> medList;
    MedicationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure this XML has a RecyclerView with id "recyclerView"

        recyclerView = findViewById(R.id.recyclerView);
        btnAddNew = findViewById(R.id.btnAddNew); // Add a button in activity_main.xml to go to Add Activity
        myDb = new DatabaseHelper(this);
        medList = new ArrayList<>();

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMedicationActivity.class);
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
        Cursor cursor = myDb.getAllHistory(); // Or getPendingMedications() if you want to filter

        if (cursor.getCount() == 0) {
            // Show empty state
        } else {
            while (cursor.moveToNext()) {
                // Ensure column names match DatabaseHelper exactly
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String details = cursor.getString(2);
                String time = cursor.getString(3);
                String status = cursor.getString(4);

                medList.add(new Medication(id, name, details, time, status));
            }
        }

        adapter = new MedicationAdapter(this, medList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}