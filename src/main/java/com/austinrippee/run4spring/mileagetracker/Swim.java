package com.austinrippee.run4spring.mileagetracker;

public class Swim extends Activity {

    public Swim(String title, int minHeartRate, String duration,
                int maxHeartRate, int avgHeartRate, int caloriesBurned) {
        super("swim", title, minHeartRate, duration, maxHeartRate, avgHeartRate, caloriesBurned);
    }
}