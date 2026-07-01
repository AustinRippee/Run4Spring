package com.austinrippee.run4spring.mileagetracker;

public class Lifting extends Activity {

    public Lifting(String title, int minHeartRate, String duration,
                   int maxHeartRate, int avgHeartRate, int caloriesBurned) {
        super("lifting", title, minHeartRate, duration, maxHeartRate, avgHeartRate, caloriesBurned);
    }
}