package com.example.suriani_clinic;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView; // Import TextView to change header
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddMedicationActivity extends AppCompatActivity {

    EditText etName, etDetails, etTime;
    Button btnSave;
    ImageButton btnBack;
    TextView tvHeaderTitle; // To change "New Record" to "Edit Record"
    DatabaseHelper myDb;

    boolean isEditMode = false;
    String medId;

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
        btnBack = findViewById(R.id.btnBack);

        // You might need to give your header TextView an ID in XML (e.g., tvHeaderTitle)
        // tvHeaderTitle = findViewById(R.id.tvHeaderTitle);

        // CHECK FOR EDIT MODE
        if (getIntent().hasExtra("isEditMode")) {
            isEditMode = true;
            medId = getIntent().getStringExtra("id");

            // Pre-fill the data
            etName.setText(getIntent().getStringExtra("name"));
            etDetails.setText(getIntent().getStringExtra("details"));
            etTime.setText(getIntent().getStringExtra("time"));

            // Change UI to look like "Edit" mode
            btnSave.setText("Update Schedule");
            // tvHeaderTitle.setText("Edit Record");
        }

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String details = etDetails.getText().toString();
            String time = etTime.getText().toString();

            if(name.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (isEditMode) {
                    // UPDATE EXISTING
                    boolean isUpdated = myDb.updateMedication(medId, name, details, time);
                    if(isUpdated) {
                        Toast.makeText(this, "Record Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // SAVE NEW
                    boolean isInserted = myDb.addMedication(name, details, time);
                    if(isInserted) {
                        Toast.makeText(this, "Schedule Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error Saving", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}