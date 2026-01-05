package com.example.suriani_clinic;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException; // Import for handling parsing errors
import java.text.SimpleDateFormat; // Import for date formatting
import java.util.ArrayList;
import java.util.Collections; // Import for sorting collections
import java.util.Comparator; // Import for custom comparison logic
import java.util.Date;
import java.util.Locale;

// This fragment displays the list of pending medications for the day.
public class HomeFragment extends Fragment {

    // UI elements
    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> medList;
    MedicationAdapter adapter;
    TextView tvEmptyState;

    // This method is called to create the view for the fragment.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the RecyclerView and the empty state TextView
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        // Initialize the database helper and the list to hold the medications
        myDb = new DatabaseHelper(requireContext());
        medList = new ArrayList<>();

        return view;
    }

    // This method is called when the fragment becomes visible to the user.
    @Override
    public void onResume() {
        super.onResume();
        // Reload the data every time the fragment is resumed to ensure it's up-to-date
        loadData();
    }

    // This method loads the pending medications from the database and displays them in the RecyclerView.
    void loadData() {
        // Clear the list to avoid displaying old data
        medList.clear();
        // Get a cursor with all pending medications from the database
        Cursor cursor = myDb.getPendingMedications();

        // Check if the cursor is not null and contains data
        if (cursor != null && cursor.getCount() > 0) {
            // If there is data, hide the empty state message
            tvEmptyState.setVisibility(View.GONE);
            // Loop through the cursor and add each medication to the list
            while (cursor.moveToNext()) {
                medList.add(new Medication(
                        cursor.getString(0), // ID
                        cursor.getString(1), // Name
                        cursor.getString(2), // Details
                        cursor.getString(3), // Time
                        cursor.getString(4)  // Status
                ));
            }
            // Close the cursor to release its resources
            cursor.close();
        } else {
            // If there is no data, show the empty state message
            tvEmptyState.setVisibility(View.VISIBLE);
        }

        // --- NEW: TIME SORTING LOGIC ---
        // Sorts the list so that medications scheduled earlier appear first (e.g., 08:00 AM before 09:00 PM).
        Collections.sort(medList, new Comparator<Medication>() {
            // Define the date format that matches the time picker format
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

            @Override
            public int compare(Medication m1, Medication m2) {
                try {
                    // Parse the time strings into Date objects
                    Date d1 = sdf.parse(m1.getDateTime());
                    Date d2 = sdf.parse(m2.getDateTime());
                    // Compare the two dates
                    if (d1 != null && d2 != null) {
                        return d1.compareTo(d2);
                    }
                } catch (ParseException e) {
                    // Print the stack trace if there is an error parsing the date
                    e.printStackTrace();
                }
                return 0; // If there is an error, don't change the order
            }
        });
        // -------------------------------

        // Create a new adapter with the context and the sorted list of medications
        adapter = new MedicationAdapter(requireContext(), medList);
        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(adapter);
        // Set the layout manager for the RecyclerView to a linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}
