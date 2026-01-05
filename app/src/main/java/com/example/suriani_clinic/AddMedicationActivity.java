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

// This activity is responsible for adding a new medication or editing an existing one.
public class AddMedicationActivity extends AppCompatActivity {

    // UI Elements
    EditText etName, etDetails, etTime;
    Button btnSave;
    ImageButton btnBack;
    // Database helper to interact with the SQLite database
    DatabaseHelper myDb;

    // Flag to check if the activity is in edit mode
    boolean isEditMode = false;
    // ID of the medication being edited
    String medId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Called when the activity is first created.
        super.onCreate(savedInstanceState);
        // Set the user interface layout for this activity.
        // The layout file is defined in res/layout/activity_add_medication.xml
        setContentView(R.layout.activity_add_medication);

        // Hide the action bar if it exists, for a cleaner UI.
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Initialize the database helper, which provides methods to interact with the database.
        myDb = new DatabaseHelper(this);

        // Initialize UI elements by finding them by their ID from the layout file.
        etName = findViewById(R.id.etMedName);
        etDetails = findViewById(R.id.etDetails);
        etTime = findViewById(R.id.etTime);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // --- TIME PICKER LOGIC STARTS HERE ---
        // Makes the EditText for time not focusable, so the user can't type in it directly.
        etTime.setFocusable(false);
        // Makes the EditText clickable to open the time picker dialog.
        etTime.setClickable(true);

        // Set an OnClickListener on the time EditText to show a TimePickerDialog when clicked.
        etTime.setOnClickListener(v -> {
            // Get the current time to initialize the picker with the current hour and minute.
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            // Create a new TimePickerDialog.
            mTimePicker = new TimePickerDialog(AddMedicationActivity.this,
                    (timePicker, selectedHour, selectedMinute) -> {
                        // This block is executed when the user sets a time in the dialog.
                        String status = "AM";
                        int hour12 = selectedHour;

                        // Convert 24-hour format to 12-hour format with AM/PM.
                        if (selectedHour >= 12) {
                            status = "PM";
                            if (selectedHour > 12) hour12 = selectedHour - 12;
                        }
                        if (selectedHour == 0) hour12 = 12; // Handle midnight case (00:xx)

                        // Format the time string and set it to the EditText.
                        etTime.setText(String.format("%02d:%02d %s", hour12, selectedMinute, status));
                    }, hour, minute, false); // false = use 12-hour format in the picker.
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        // --- TIME PICKER LOGIC ENDS HERE ---

        // CHECK FOR EDIT MODE
        // Check if the intent that started this activity has extra data with the key "isEditMode".
        // This is how we know whether to add a new medication or edit an existing one.
        if (getIntent().hasExtra("isEditMode")) {
            isEditMode = true;
            // Get the medication ID from the intent.
            medId = getIntent().getStringExtra("id");

            // Populate the EditText fields with the data of the medication being edited.
            etName.setText(getIntent().getStringExtra("name"));
            etDetails.setText(getIntent().getStringExtra("details"));
            etTime.setText(getIntent().getStringExtra("time"));

            // Change the save button text to "Update Schedule" to reflect the edit mode.
            btnSave.setText("Update Schedule");
        }

        // Set an OnClickListener for the back button to finish the activity and return to the previous screen.
        btnBack.setOnClickListener(v -> finish());

        // --- SAVE BUTTON LOGIC ---
        // Set an OnClickListener for the save button.
        btnSave.setOnClickListener(view -> {
            // Get the user input from the EditText fields.
            String name = etName.getText().toString();
            String details = etDetails.getText().toString();
            String time = etTime.getText().toString();

            // Validate that the required fields (name and time) are not empty.
            if(name.isEmpty() || time.isEmpty()) {
                // Show a short message to the user to fill in all fields.
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // If in edit mode, handle updating the medication.
                if (isEditMode) {
                    // --- EDIT MODE LOGIC ---
                    // 1. Check the current status of the medication in the database to decide the update strategy.
                    String currentStatus = myDb.getStatus(medId);

                    // Check if the medication status is "Pending" (case-insensitive).
                    if (currentStatus.equalsIgnoreCase("Pending")) {
                        // CASE A: The medication is still pending (not taken or missed yet).
                        // It is safe to overwrite this record with the new details.
                        boolean isUpdated = myDb.updateMedication(medId, name, details, time);
                        if(isUpdated) {
                            Toast.makeText(this, "Schedule Updated", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity on successful update.
                        } else {
                            Toast.makeText(this, "Error Updating", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // CASE B: The medication is already in History (status is Taken or Missed).
                        // DO NOT overwrite the existing record to preserve the history.
                        // Instead, create a NEW medication record with the updated details.
                        boolean isInserted = myDb.addMedication(name, details, time);
                        if(isInserted) {
                            Toast.makeText(this, "History Preserved. New Schedule Added.", Toast.LENGTH_LONG).show();
                            finish(); // Close the activity on successful insertion.
                        } else {
                            Toast.makeText(this, "Error Adding", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // --- ADD MODE LOGIC ---
                    // CASE C: Not in edit mode, so add a brand new medication record.
                    boolean isInserted = myDb.addMedication(name, details, time);
                    if(isInserted) {
                        Toast.makeText(this, "Schedule Saved", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity on successful save.
                    } else {
                        Toast.makeText(this, "Error Saving", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
