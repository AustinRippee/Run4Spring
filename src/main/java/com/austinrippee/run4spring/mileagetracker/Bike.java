package com.austinrippee.run4spring.mileagetracker;

public class Bike extends Activity {

    public Bike(String title, int minHeartRate, String duration,
                int maxHeartRate, int avgHeartRate, int caloriesBurned) {
        super("bike", title, minHeartRate, duration, maxHeartRate, avgHeartRate, caloriesBurned);
    }
}