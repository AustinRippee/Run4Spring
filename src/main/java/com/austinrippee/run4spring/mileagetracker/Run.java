package com.austinrippee.run4spring.mileagetracker;

public class Run extends Activity {

    private final double distance;
    private final String avgPace;

    public Run(double distance, String avgPace, String title, String duration,
               int minHeartRate, int maxHeartRate, int avgHeartRate, int calories) {
        super("run", title, minHeartRate, duration, maxHeartRate, avgHeartRate, calories);
        this.distance = distance;
        this.avgPace = avgPace;
    }

    public double getDistance() { return distance; }
    public String getAvgPace() { return avgPace; }
}