package com.austinrippee.run4spring.mileagetracker;

public class Activity {

    private final String type;
    private final String title;
    private final String duration;
    private final int minHeartRate;
    private final int maxHeartRate;
    private final int avgHeartRate;
    private final int caloriesBurned;

    protected Activity(String type, String title, int minHeartRate, String duration,
                       int maxHeartRate, int avgHeartRate, int calories) {
        this.type = type;
        this.title = title;
        this.minHeartRate = minHeartRate;
        this.duration = duration;
        this.maxHeartRate = maxHeartRate;
        this.avgHeartRate = avgHeartRate;
        this.caloriesBurned = calories;
    }

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getDuration() { return duration; }
    public int getMinHeartRate() { return minHeartRate; }
    public int getMaxHeartRate() { return maxHeartRate; }
    public int getAvgHeartRate() { return avgHeartRate; }
    public int getCaloriesBurned() { return caloriesBurned; }
}