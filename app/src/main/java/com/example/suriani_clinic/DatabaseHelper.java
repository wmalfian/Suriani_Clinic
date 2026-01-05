package com.example.suriani_clinic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// DatabaseHelper class manages all the database operations like creating tables,
// inserting, updating, deleting, and querying data. It extends SQLiteOpenHelper.
public class DatabaseHelper extends SQLiteOpenHelper {

    // The name of the database file.
    private static final String DATABASE_NAME = "MedicationLog.db";
    // The version of the database.
    // When the database schema is changed, this version must be incremented.
    private static final int DATABASE_VERSION = 2;

    // Table name for medication schedules.
    private static final String TABLE_MEDS = "medication_logs";
    // Table name for historical logs of taken or missed medications.
    private static final String TABLE_HISTORY = "history_logs"; // New Table

    // --- Column Names ---
    // Common column names used in both tables.
    private static final String COL_ID = "ID"; // Unique identifier for each row.
    private static final String COL_MED_NAME = "MED_NAME"; // Name of the medication.
    private static final String COL_DETAILS = "DETAILS"; // Additional details for the medication.
    private static final String COL_STATUS = "STATUS"; // Status (e.g., "Pending", "Taken", "Missed").

    // Column specific to the medication schedule table.
    private static final String COL_SCHEDULE_TIME = "DATE_TIME"; // The scheduled time for the medication (e.g., "08:00 AM").

    // Column specific to the history log table.
    private static final String COL_LOG_TIMESTAMP = "LOG_TIMESTAMP"; // The exact time the log was created (e.g., "Jan 5, 08:00 AM").

    // Constructor for the DatabaseHelper.
    public DatabaseHelper(Context context) {
        // Calls the superclass constructor to initialize the database.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the medication schedule table.
        String createMeds = "CREATE TABLE " + TABLE_MEDS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MED_NAME + " TEXT, " +
                COL_DETAILS + " TEXT, " +
                COL_SCHEDULE_TIME + " TEXT, " +
                COL_STATUS + " TEXT)";
        db.execSQL(createMeds);

        // SQL query to create the history log table.
        String createHistory = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MED_NAME + " TEXT, " +
                COL_DETAILS + " TEXT, " +
                COL_LOG_TIMESTAMP + " TEXT, " +
                COL_STATUS + " TEXT)";
        db.execSQL(createHistory);
    }

    // Called when the database needs to be upgraded.
    // This happens when the DATABASE_VERSION is incremented.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handles the schema upgrade from version 1 to 2.
        if (oldVersion < 2) {
            // If upgrading from an older version that didn't have the history table, create it.
            String createHistory = "CREATE TABLE " + TABLE_HISTORY + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_MED_NAME + " TEXT, " +
                    COL_DETAILS + " TEXT, " +
                    COL_LOG_TIMESTAMP + " TEXT, " +
                    COL_STATUS + " TEXT)";
            db.execSQL(createHistory);
        }
    }

    // --- METHODS FOR MEDICATION SCHEDULE (HOME SCREEN) ---

    // Adds a new medication schedule to the database.
    public boolean addMedication(String name, String details, String time) {
        // Gets the database in writable mode.
        SQLiteDatabase db = this.getWritableDatabase();
        // ContentValues is used to store a set of key-value pairs.
        ContentValues cv = new ContentValues();
        cv.put(COL_MED_NAME, name);
        cv.put(COL_DETAILS, details);
        cv.put(COL_SCHEDULE_TIME, time);
        cv.put(COL_STATUS, "Pending"); // New medications always start with "Pending" status.
        // Inserts the new row, returning -1 if an error occurred.
        return db.insert(TABLE_MEDS, null, cv) != -1;
    }

    // Updates an existing medication schedule.
    public boolean updateMedication(String id, String name, String details, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MED_NAME, name);
        cv.put(COL_DETAILS, details);
        cv.put(COL_SCHEDULE_TIME, time);
        // Updates the row where the ID matches, and returns the number of rows affected.
        return db.update(TABLE_MEDS, cv, "ID=?", new String[]{id}) > 0;
    }

    // Updates the status of a medication (e.g., to "Taken" or "Missed").
    public boolean updateStatus(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, status);
        return db.update(TABLE_MEDS, cv, "ID=?", new String[]{id}) > 0;
    }

    // Retrieves all medications that are currently "Pending".
    public Cursor getPendingMedications() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Returns a Cursor object, which points to the result set of the query.
        // The results are ordered by schedule time.
        return db.rawQuery("SELECT * FROM " + TABLE_MEDS + " WHERE STATUS='Pending' ORDER BY " + COL_SCHEDULE_TIME + " ASC", null);
    }

    // Retrieves all medications, sorted by name.
    public Cursor getAllMedicationsByName() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEDS + " ORDER BY " + COL_MED_NAME + " ASC", null);
    }

    // Gets the status of a specific medication by its ID.
    public String getStatus(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query the database for the status of the given medication ID.
        Cursor c = db.rawQuery("SELECT STATUS FROM " + TABLE_MEDS + " WHERE ID=?", new String[]{id});
        String s = "Pending"; // Default status.
        if (c.moveToFirst()) { // If a result is found...
            s = c.getString(0); // ...get the status from the first column.
        }
        c.close(); // Always close the cursor to release resources.
        return s;
    }

    // Resets the status of all medications to "Pending".
    // This is useful for starting a new day.
    public void resetDailySchedule() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, "Pending");
        // Updates all rows in the table.
        db.update(TABLE_MEDS, cv, null, null);
    }

    // Deletes a medication from the schedule table by its ID.
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Returns the number of rows deleted.
        return db.delete(TABLE_MEDS, "ID = ?", new String[] {id});
    }

    // --- NEW: METHODS FOR HISTORY LOGS ---

    // Adds a record to the history table.
    // This is called when a medication is marked as "Taken" or "Missed".
    public void addToHistory(String name, String details, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Creates a nicely formatted timestamp string (e.g., "Mon, 12 Jan • 08:00 PM").
        String timestamp = new SimpleDateFormat("EEE, dd MMM • hh:mm a", Locale.getDefault()).format(new Date());

        cv.put(COL_MED_NAME, name);
        cv.put(COL_DETAILS, details);
        cv.put(COL_LOG_TIMESTAMP, timestamp); // Saves the current date and time as the log timestamp.
        cv.put(COL_STATUS, status); // Saves whether it was "Taken" or "Missed".

        // Inserts the new history log.
        db.insert(TABLE_HISTORY, null, cv);
    }

    // Retrieves all records from the history log.
    public Cursor getAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Returns a Cursor with all history logs, ordered by ID in descending order (newest first).
        return db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + COL_ID + " DESC", null);
    }
}
