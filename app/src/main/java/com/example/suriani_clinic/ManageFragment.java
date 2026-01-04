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

public class ManageFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> fullList;
    AllMedicationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        recyclerView = view.findViewById(R.id.recyclerAllMedsFragment);
        myDb = new DatabaseHelper(requireContext());
        fullList = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        fullList.clear();
        Cursor cursor = myDb.getAllMedicationsByName();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                fullList.add(new Medication(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
            cursor.close();
        }

        adapter = new AllMedicationsAdapter(requireContext(), fullList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}