package com.example.ordo;

public class ActivityItem {
    private String name;
    private String startTime;
    private String endTime;

    // Constructor
    public ActivityItem(String name, String startTime, String endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}