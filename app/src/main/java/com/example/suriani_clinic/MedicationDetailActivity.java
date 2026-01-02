package com.example.suriani_clinic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MedicationDetailActivity extends AppCompatActivity {

    TextView tvName, tvTime, tvStatus, tvInfo;
    ImageButton btnBack, btnEdit;
    String medId; // Variable to store the ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_detail);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // 1. Initialize Views
        tvName = findViewById(R.id.tvDetailName);
        tvTime = findViewById(R.id.tvDetailTime);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvInfo = findViewById(R.id.tvDetailInfo);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit); // Make sure you added this button in XML

        // 2. Receive Data (INCLUDING ID)
        medId = getIntent().getStringExtra("id"); // Catch the ID here
        String name = getIntent().getStringExtra("name");
        String time = getIntent().getStringExtra("time");
        String status = getIntent().getStringExtra("status");
        String details = getIntent().getStringExtra("details");

        // 3. Set Data to Views
        tvName.setText(name);
        tvTime.setText(time);
        tvStatus.setText(status);
        tvInfo.setText(details);

        // 4. Back Button
        btnBack.setOnClickListener(v -> finish());

        // 5. EDIT BUTTON LOGIC
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(MedicationDetailActivity.this, AddMedicationActivity.class);

            // Pass the "Edit Mode" signal and the data back to the input form
            intent.putExtra("isEditMode", true);
            intent.putExtra("id", medId); // SEND THE ID
            intent.putExtra("name", name);
            intent.putExtra("details", details);
            intent.putExtra("time", time);

            startActivity(intent);
            finish(); // Close this page so it refreshes when we come back
        });
    }
}