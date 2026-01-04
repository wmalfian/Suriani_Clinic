package com.example.suriani_clinic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MedicationLog.db";
    // 1. INCREMENT VERSION to force update
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_MEDS = "medication_logs";
    private static final String TABLE_HISTORY = "history_logs"; // New Table

    // Common Columns
    private static final String COL_ID = "ID";
    private static final String COL_MED_NAME = "MED_NAME";
    private static final String COL_DETAILS = "DETAILS";
    private static final String COL_STATUS = "STATUS";

    // Meds Table Column
    private static final String COL_SCHEDULE_TIME = "DATE_TIME"; // e.g., "08:00 AM"

    // History Table Column
    private static final String COL_LOG_TIMESTAMP = "LOG_TIMESTAMP"; // e.g., "Jan 5, 08:00 AM"

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Schedule Table
        String createMeds = "CREATE TABLE " + TABLE_MEDS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MED_NAME + " TEXT, " +
                COL_DETAILS + " TEXT, " +
                COL_SCHEDULE_TIME + " TEXT, " +
                COL_STATUS + " TEXT)";
        db.execSQL(createMeds);

        // Create History Table
        String createHistory = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MED_NAME + " TEXT, " +
                COL_DETAILS + " TEXT, " +
                COL_LOG_TIMESTAMP + " TEXT, " +
                COL_STATUS + " TEXT)";
        db.execSQL(createHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If updating from V1 to V2, create the missing table
        if (oldVersion < 2) {
            String createHistory = "CREATE TABLE " + TABLE_HISTORY + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_MED_NAME + " TEXT, " +
                    COL_DETAILS + " TEXT, " +
                    COL_LOG_TIMESTAMP + " TEXT, " +
                    COL_STATUS + " TEXT)";
            db.execSQL(createHistory);
        }
    }

    // --- METHODS FOR SCHEDULE (HOME) ---

    public boolean addMedication(String name, String details, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MED_NAME, name);
        cv.put(COL_DETAILS, details);
        cv.put(COL_SCHEDULE_TIME, time);
        cv.put(COL_STATUS, "Pending");
        return db.insert(TABLE_MEDS, null, cv) != -1;
    }

    public boolean updateMedication(String id, String name, String details, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MED_NAME, name);
        cv.put(COL_DETAILS, details);
        cv.put(COL_SCHEDULE_TIME, time);
        return db.update(TABLE_MEDS, cv, "ID=?", new String[]{id}) > 0;
    }

    public boolean updateStatus(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, status);
        return db.update(TABLE_MEDS, cv, "ID=?", new String[]{id}) > 0;
    }

    public Cursor getPendingMedications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEDS + " WHERE STATUS='Pending' ORDER BY " + COL_SCHEDULE_TIME + " ASC", null);
    }

    public Cursor getAllMedicationsByName() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MEDS + " ORDER BY " + COL_MED_NAME + " ASC", null);
    }

    public String getStatus(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT STATUS FROM " + TABLE_MEDS + " WHERE ID=?", new String[]{id});
        String s = "Pending";
        if (c.moveToFirst()) s = c.getString(0);
        c.close();
        return s;
    }

    public void resetDailySchedule() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, "Pending");
        db.update(TABLE_MEDS, cv, null, null);
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MEDS, "ID = ?", new String[] {id});
    }

    // --- NEW: METHODS FOR HISTORY LOGS ---

    // 1. Add to History (Call this when Taken/Missed is clicked)
    public void addToHistory(String name, String details, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Generate nicely formatted date: "Mon, 12 Jan • 08:00 PM"
        String timestamp = new SimpleDateFormat("EEE, dd MMM • hh:mm a", Locale.getDefault()).format(new Date());

        cv.put(COL_MED_NAME, name);
        cv.put(COL_DETAILS, details);
        cv.put(COL_LOG_TIMESTAMP, timestamp); // Saves the current date & time
        cv.put(COL_STATUS, status);

        db.insert(TABLE_HISTORY, null, cv);
    }

    // 2. Get All History (Now reads from TABLE_HISTORY)
    public Cursor getAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Show newest logs first
        return db.rawQuery("SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + COL_ID + " DESC", null);
    }
}