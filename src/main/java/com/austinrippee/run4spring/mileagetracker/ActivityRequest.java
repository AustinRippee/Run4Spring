package com.austinrippee.run4spring.mileagetracker;

public record ActivityRequest(
        String type,
        String title,
        String duration,
        int minHeartRate,
        int maxHeartRate,
        int avgHeartRate,
        int caloriesBurned,
        Double distance,
        String avgPace
) {}