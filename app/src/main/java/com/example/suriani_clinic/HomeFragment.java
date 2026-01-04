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
import java.util.ArrayList;

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

        // Use requireContext() in Fragments
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

        adapter = new MedicationAdapter(requireContext(), medList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}