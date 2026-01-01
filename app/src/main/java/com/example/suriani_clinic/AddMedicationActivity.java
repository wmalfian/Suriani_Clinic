package com.example.suriani_clinic;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddMedicationActivity extends AppCompatActivity {

    EditText etName, etDetails, etTime;
    Button btnSave;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        myDb = new DatabaseHelper(this);

        etName = findViewById(R.id.etMedName);
        etDetails = findViewById(R.id.etDetails);
        etTime = findViewById(R.id.etTime);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String details = etDetails.getText().toString();
            String time = etTime.getText().toString();

            if(name.isEmpty() || time.isEmpty()) {
                Toast.makeText(AddMedicationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = myDb.addMedication(name, details, time);
                if(isInserted) {
                    Toast.makeText(AddMedicationActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                    finish(); // Closes this activity and goes back to Main
                } else {
                    Toast.makeText(AddMedicationActivity.this, "Error Saving", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}