package com.example.suriani_clinic;

import android.database.Cursor;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // OPTIONAL: Hides the default top bar so your custom XML header looks better
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerHistory);
        myDb = new DatabaseHelper(this);
        historyList = new ArrayList<>();

        loadHistoryData();
    }

    private void loadHistoryData() {
        Cursor cursor = myDb.getAllHistory();

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No history found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                // Fetch data from database columns by index
                // 0=ID, 1=NAME, 2=DETAILS, 3=TIME, 4=STATUS
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String details = cursor.getString(2);
                String time = cursor.getString(3);
                String status = cursor.getString(4);

                historyList.add(new Medication(id, name, details, time, status));
            }
            cursor.close(); // GOOD PRACTICE: Always close the cursor to save memory
        }

        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}