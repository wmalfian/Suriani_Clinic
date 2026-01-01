package com.example.suriani_clinic;

public class Medication {
    private String id;
    private String name;
    private String details;
    private String dateTime;
    private String status;

    // Constructor
    public Medication(String id, String name, String details, String dateTime, String status) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.dateTime = dateTime;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDetails() { return details; }
    public String getDateTime() { return dateTime; }
    public String getStatus() { return status; }

    // --- ADD THIS METHOD TO FIX THE ERROR ---
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}