package com.example.suriani_clinic;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// This fragment is for managing all medications, allowing users to view, edit, and delete them.
public class ManageFragment extends Fragment {

    // UI Elements and data handling objects
    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> fullList;
    AllMedicationsAdapter adapter;

    // This method is called to create the view for the fragment.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        // Initialize the RecyclerView, DatabaseHelper, and the list of medications.
        recyclerView = view.findViewById(R.id.recyclerAllMedsFragment);
        myDb = new DatabaseHelper(requireContext());
        fullList = new ArrayList<>();

        return view;
    }

    // This method is called when the fragment becomes visible to the user.
    @Override
    public void onResume() {
        super.onResume();
        // Load or reload the data every time the fragment is resumed to ensure it is up-to-date.
        loadData();
    }

    // This method loads all medications from the database and populates the RecyclerView.
    private void loadData() {
        // Clear the list to prevent displaying old or duplicate data.
        fullList.clear();
        // Get a cursor containing all medications, ordered by name, from the database.
        Cursor cursor = myDb.getAllMedicationsByName();

        // Check if the cursor is not null and has at least one row of data.
        if (cursor != null && cursor.getCount() > 0) {
            // Loop through the cursor to retrieve each medication's data.
            while (cursor.moveToNext()) {
                // Create a new Medication object and add it to the list.
                fullList.add(new Medication(
                        cursor.getString(0), // ID
                        cursor.getString(1), // Name
                        cursor.getString(2), // Details
                        cursor.getString(3), // Time
                        cursor.getString(4)  // Status
                ));
            }
            // Close the cursor to free up resources.
            cursor.close();
        }

        // Create a new AllMedicationsAdapter with the context and the list of medications.
        adapter = new AllMedicationsAdapter(requireContext(), fullList);
        // Set the adapter for the RecyclerView.
        recyclerView.setAdapter(adapter);
        // Set the layout manager for the RecyclerView to a linear layout.
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}
