package com.example.suriani_clinic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "MedicationLog.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_NAME = "medication_logs";

    // Column Names
    private static final String COL_ID = "ID";
    private static final String COL_MED_NAME = "MED_NAME";
    private static final String COL_DETAILS = "DETAILS";     // e.g. dosage, instructions
    private static final String COL_DATE_TIME = "DATE_TIME"; // stored as String
    private static final String COL_STATUS = "STATUS";       // "Pending", "Taken", "Missed"

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 1. Create the Table (Run once when app is installed)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MED_NAME + " TEXT, " +
                COL_DETAILS + " TEXT, " +
                COL_DATE_TIME + " TEXT, " +
                COL_STATUS + " TEXT)";
        db.execSQL(createTable);
    }

    // Upgrade logic (Drop old table if version changes)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ====================================================================
    // MEMBER 1: ALFIAN'S TASK (Adding Data)
    // ====================================================================

    // Method to insert a new medication record
    public boolean addMedication(String name, String details, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_MED_NAME, name);
        contentValues.put(COL_DETAILS, details);
        contentValues.put(COL_DATE_TIME, dateTime);
        contentValues.put(COL_STATUS, "Pending"); // Default status is Pending

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Returns true if insert was successful
    }

    // ====================================================================
    // MEMBER 2: AMIR'S TASK (Updating Status)
    // ====================================================================

    // Method to update status to "Taken" or "Missed"
    public boolean updateStatus(String id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STATUS, newStatus);

        // Update the row where ID matches the one passed in
        int rowsAffected = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return rowsAffected > 0;
    }

    // Helper method to get only "Pending" items for today
    public Cursor getPendingMedications() {
        SQLiteDatabase db = this.getReadableDatabase();
        // CHANGED: ORDER BY DATE_TIME ASC (Ascending order: 08:00 AM -> 09:00 PM)
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE STATUS = 'Pending' ORDER BY DATE_TIME ASC", null);
    }

    // ====================================================================
    // MEMBER 3: SHAHRUL'S TASK (Retrieving History)
    // ====================================================================

    // Method to get ALL data for the History list
    public Cursor getAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Orders by latest added first (ID descending)
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC", null);
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    // ====================================================================
    // NEW: UPDATE FUNCTION (Editing Details)
    // ====================================================================
    public boolean updateMedication(String id, String name, String details, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MED_NAME, name);
        contentValues.put(COL_DETAILS, details);
        contentValues.put(COL_DATE_TIME, time);

        // We only update the details, not the status
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return result > 0;
    }
}