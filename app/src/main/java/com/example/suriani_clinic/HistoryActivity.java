package com.example.suriani_clinic;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton; // Import this
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// This activity displays the medication history.
public class HistoryActivity extends AppCompatActivity {

    // UI elements
    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> historyList;
    HistoryAdapter adapter;
    ImageButton btnBack; // Declare Back Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Initialize the RecyclerView and the back button from the layout
        recyclerView = findViewById(R.id.recyclerHistory);
        btnBack = findViewById(R.id.btnBack); // Find the button

        // Initialize the database helper and the list to hold the history
        myDb = new DatabaseHelper(this);
        historyList = new ArrayList<>();

        // Back Button Logic: Finish the activity when the back button is clicked
        btnBack.setOnClickListener(v -> finish()); // Closes this page and goes back

        // Load the history data into the RecyclerView
        loadHistoryData();
    }

    // This method retrieves the medication history from the database and displays it.
    private void loadHistoryData() {
        // Get a cursor with all the history data from the database
        Cursor cursor = myDb.getAllHistory();

        // Check if the cursor is not null and contains data
        if (cursor != null && cursor.getCount() > 0) {
            // Iterate through the cursor to get each history entry
            while (cursor.moveToNext()) {
                // Create a new Medication object with the data from the cursor and add it to the list
                historyList.add(new Medication(
                        cursor.getString(0), // ID
                        cursor.getString(1), // Name
                        cursor.getString(2), // DateTime
                        cursor.getString(3), // Details
                        cursor.getString(4)  // Status
                ));
            }
            // Close the cursor to free up resources
            cursor.close();
        } else {
            // If there is no history, show a toast message
            Toast.makeText(this, "No history found", Toast.LENGTH_SHORT).show();
        }

        // Create a new HistoryAdapter with the current context and the history list
        adapter = new HistoryAdapter(this, historyList);
        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(adapter);
        // Set the layout manager for the RecyclerView to a linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
