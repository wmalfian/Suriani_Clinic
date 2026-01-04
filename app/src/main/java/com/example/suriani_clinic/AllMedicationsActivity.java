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

public class AllMedicationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> fullList;
    AllMedicationsAdapter adapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medications);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerAllMeds);
        btnBack = findViewById(R.id.btnBack);
        myDb = new DatabaseHelper(this);
        fullList = new ArrayList<>();

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data every time we come back to this screen
        loadData();
    }

    private void loadData() {
        fullList.clear();
        Cursor cursor = myDb.getAllMedicationsByName();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                fullList.add(new Medication(
                        cursor.getString(0), // ID
                        cursor.getString(1), // Name
                        cursor.getString(2), // Details
                        cursor.getString(3), // Time
                        cursor.getString(4)  // Status
                ));
            }
            cursor.close();
        }

        adapter = new AllMedicationsAdapter(this, fullList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}