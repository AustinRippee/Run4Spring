package com.austinrippee.run4spring.mileagetracker;

public class Walk extends Activity {

    public Walk(String title, int minHeartRate, String duration,
                int maxHeartRate, int avgHeartRate, int caloriesBurned) {
        super("walk", title, minHeartRate, duration, maxHeartRate, avgHeartRate, caloriesBurned);
    }
}