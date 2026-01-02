package com.example.suriani_clinic;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MedicationDetailActivity extends AppCompatActivity {

    TextView tvName, tvTime, tvStatus, tvInfo;
    ImageButton btnBack;

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

        // 2. Receive Data from Intent
        String name = getIntent().getStringExtra("name");
        String time = getIntent().getStringExtra("time");
        String status = getIntent().getStringExtra("status");
        String details = getIntent().getStringExtra("details");

        // 3. Set Data to Views
        tvName.setText(name);
        tvTime.setText(time);
        tvStatus.setText(status);
        tvInfo.setText(details);

        // 4. Back Button Logic
        btnBack.setOnClickListener(v -> finish());
    }
}