package com.example.suriani_clinic;

// This class represents a Medication object with its properties.
public class Medication {
    // The unique identifier for the medication.
    private String id;
    // The name of the medication.
    private String name;
    // Additional details about the medication.
    private String details;
    // The date and time the medication is scheduled to be taken.
    private String dateTime;
    // The status of the medication (e.g., "Pending", "Taken", "Missed").
    private String status;

    // The constructor for the Medication class.
    public Medication(String id, String name, String details, String dateTime, String status) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.dateTime = dateTime;
        this.status = status;
    }

    // Getter method to retrieve the medication's ID.
    public String getId() { return id; }
    // Getter method to retrieve the medication's name.
    public String getName() { return name; }
    // Getter method to retrieve the medication's details.
    public String getDetails() { return details; }
    // Getter method to retrieve the medication's scheduled date and time.
    public String getDateTime() { return dateTime; }
    // Getter method to retrieve the medication's status.
    public String getStatus() { return status; }

    // --- ADD THIS METHOD TO FIX THE ERROR ---
    // This method updates the status of the medication.
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
