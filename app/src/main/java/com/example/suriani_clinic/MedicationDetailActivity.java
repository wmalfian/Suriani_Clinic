package com.example.suriani_clinic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// This activity displays the details of a single medication.
public class MedicationDetailActivity extends AppCompatActivity {

    // UI Elements
    TextView tvName, tvTime, tvStatus, tvInfo;
    ImageButton btnBack, btnEdit;
    // Variable to store the medication ID passed from the previous activity
    String medId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Called when the activity is first created.
        super.onCreate(savedInstanceState);
        // Sets the layout for this activity from activity_medication_detail.xml
        setContentView(R.layout.activity_medication_detail);

        // Hide the action bar for a cleaner UI
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // 1. Initialize Views
        // Link the variables to the views in the layout file
        tvName = findViewById(R.id.tvDetailName);
        tvTime = findViewById(R.id.tvDetailTime);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvInfo = findViewById(R.id.tvDetailInfo);
        btnBack = findViewById(R.id.btnBack);// Make sure you added this button in XML
        // Note: btnEdit is declared, but not yet initialized or used in this file.

        // 2. Receive Data (INCLUDING ID)
        // Get the data passed from the previous activity through the Intent
        medId = getIntent().getStringExtra("id"); // Catch the ID here
        String name = getIntent().getStringExtra("name");
        String time = getIntent().getStringExtra("time");
        String status = getIntent().getStringExtra("status");
        String details = getIntent().getStringExtra("details");

        // 3. Set Data to Views
        // Display the received medication data in the UI
        tvName.setText(name);
        tvTime.setText(time);
        tvStatus.setText(status);
        tvInfo.setText(details);

        // 4. Back Button
        // Set a click listener on the back button to close this activity
        btnBack.setOnClickListener(v -> finish());
    }
}
