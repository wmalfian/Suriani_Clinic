package com.example.suriani_clinic;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AddMedicationActivity extends AppCompatActivity {

    EditText etName, etDetails, etTime;
    Button btnSave;
    ImageButton btnBack;
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

        // --- TIME PICKER LOGIC STARTS HERE ---
        etTime.setFocusable(false); // Disable manual typing
        etTime.setClickable(true);  // Enable clicking

        etTime.setOnClickListener(v -> {
            // Get Current Time
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddMedicationActivity.this,
                    (timePicker, selectedHour, selectedMinute) -> {
                        String status = "AM";
                        int hour12 = selectedHour;

                        // Convert 24h to 12h format
                        if (selectedHour >= 12) {
                            status = "PM";
                            if (selectedHour > 12) hour12 = selectedHour - 12;
                        }
                        if (selectedHour == 0) hour12 = 12; // Handle midnight

                        // Format string (adds leading zero to minutes if needed)
                        etTime.setText(String.format("%02d:%02d %s", hour12, selectedMinute, status));
                    }, hour, minute, false); // false = 12 hour format (AM/PM)
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        // --- TIME PICKER LOGIC ENDS HERE ---

        // CHECK FOR EDIT MODE
        if (getIntent().hasExtra("isEditMode")) {
            isEditMode = true;
            medId = getIntent().getStringExtra("id");

            etName.setText(getIntent().getStringExtra("name"));
            etDetails.setText(getIntent().getStringExtra("details"));
            etTime.setText(getIntent().getStringExtra("time"));

            btnSave.setText("Update Schedule");
        }

        btnBack.setOnClickListener(v -> finish());

        // Inside AddMedicationActivity.java

        btnSave.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String details = etDetails.getText().toString();
            String time = etTime.getText().toString();

            if(name.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (isEditMode) {
                    // 1. Check the current status in the database
                    String currentStatus = myDb.getStatus(medId);

                    if (currentStatus.equalsIgnoreCase("Pending")) {
                        // CASE A: It is still pending (not taken yet).
                        // It is safe to overwrite this record.
                        boolean isUpdated = myDb.updateMedication(medId, name, details, time);
                        if(isUpdated) {
                            Toast.makeText(this, "Schedule Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error Updating", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // CASE B: It is History (Taken/Missed).
                        // DO NOT overwrite. Create a NEW record instead.
                        boolean isInserted = myDb.addMedication(name, details, time);
                        if(isInserted) {
                            Toast.makeText(this, "History Preserved. New Schedule Added.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error Adding", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // CASE C: Adding a brand new record (Not edit mode)
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