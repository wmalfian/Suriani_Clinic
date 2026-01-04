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
import java.text.ParseException; // Import
import java.text.SimpleDateFormat; // Import
import java.util.ArrayList;
import java.util.Collections; // Import for sorting
import java.util.Comparator; // Import
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> medList;
    MedicationAdapter adapter;
    TextView tvEmptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHome);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        myDb = new DatabaseHelper(requireContext());
        medList = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    void loadData() {
        medList.clear();
        Cursor cursor = myDb.getPendingMedications();

        if (cursor != null && cursor.getCount() > 0) {
            tvEmptyState.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                medList.add(new Medication(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
            cursor.close();
        } else {
            tvEmptyState.setVisibility(View.VISIBLE);
        }

        // --- NEW: TIME SORTING LOGIC ---
        // Sorts the list so 08:00 AM is top, 09:00 PM is bottom
        Collections.sort(medList, new Comparator<Medication>() {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH); // Matches your TimePicker format

            @Override
            public int compare(Medication m1, Medication m2) {
                try {
                    Date d1 = sdf.parse(m1.getDateTime());
                    Date d2 = sdf.parse(m2.getDateTime());
                    if (d1 != null && d2 != null) {
                        return d1.compareTo(d2);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0; // If error, don't swap
            }
        });
        // -------------------------------

        adapter = new MedicationAdapter(requireContext(), medList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}