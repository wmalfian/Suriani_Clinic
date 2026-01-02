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

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper myDb;
    ArrayList<Medication> historyList;
    HistoryAdapter adapter;
    ImageButton btnBack; // Declare Back Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerHistory);
        btnBack = findViewById(R.id.btnBack); // Find the button

        myDb = new DatabaseHelper(this);
        historyList = new ArrayList<>();

        // Back Button Logic
        btnBack.setOnClickListener(v -> finish()); // Closes this page and goes back

        loadHistoryData();
    }

    private void loadHistoryData() {
        Cursor cursor = myDb.getAllHistory();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                historyList.add(new Medication(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            }
            cursor.close();
        } else {
            Toast.makeText(this, "No history found", Toast.LENGTH_SHORT).show();
        }

        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}