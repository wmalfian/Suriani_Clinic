package com.example.suriani_clinic;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// This activity displays a list of all unique medications.
public class AllMedicationsActivity extends AppCompatActivity {

    // UI Elements
    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> fullList;
    AllMedicationsAdapter adapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medications);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerAllMeds);
        btnBack = findViewById(R.id.btnBack);
        myDb = new DatabaseHelper(this);
        fullList = new ArrayList<>();

        // Set a click listener on the back button to finish the activity
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data every time we come back to this screen to reflect any changes
        loadData();
    }

    // This method loads the data from the database and populates the RecyclerView
    private void loadData() {
        // Clear the list to prevent duplicates on subsequent loads
        fullList.clear();
        // Get a cursor containing all medications, grouped by name
        Cursor cursor = myDb.getAllMedicationsByName();

        // Check if the cursor is not null and has at least one row
        if (cursor != null && cursor.getCount() > 0) {
            // Loop through the cursor and add each medication to the list
            while (cursor.moveToNext()) {
                fullList.add(new Medication(
                        cursor.getString(0), // ID
                        cursor.getString(1), // Name
                        cursor.getString(2), // Details
                        cursor.getString(3), // Time
                        cursor.getString(4)  // Status
                ));
            }
            // Close the cursor to release its resources
            cursor.close();
        }

        // Initialize the adapter with the context and the list of medications
        adapter = new AllMedicationsAdapter(this, fullList);
        // Set the adapter on the RecyclerView
        recyclerView.setAdapter(adapter);
        // Set the layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
