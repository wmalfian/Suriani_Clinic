package com.example.suriani_clinic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton; // 1. IMPORT THIS
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddMedicationActivity extends AppCompatActivity {

    EditText etName, etDetails, etTime;
    Button btnSave;
    ImageButton btnBack; // 2. DECLARE THE BUTTON
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        myDb = new DatabaseHelper(this);

        etName = findViewById(R.id.etMedName);
        etDetails = findViewById(R.id.etDetails);
        etTime = findViewById(R.id.etTime);
        btnSave = findViewById(R.id.btnSave);

        // 3. FIND THE BUTTON AND ADD THE "GO BACK" LOGIC
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // This command closes the page

        btnSave.setOnClickListener(view -> {
            // ... your existing save logic ...
            String name = etName.getText().toString();
            String details = etDetails.getText().toString();
            String time = etTime.getText().toString();

            if(name.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = myDb.addMedication(name, details, time);
                if(isInserted) {
                    Toast.makeText(this, "Schedule Saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error Saving", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}