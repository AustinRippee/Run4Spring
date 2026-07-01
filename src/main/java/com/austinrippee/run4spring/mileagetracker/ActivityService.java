package com.austinrippee.run4spring.mileagetracker;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ActivityService {

    private final List<Activity> activities = new ArrayList<>();

    public List<Activity> getAll() {
        return Collections.unmodifiableList(activities);
    }

    public Activity add(ActivityRequest request) {
        Activity activity = createActivity(request);
        activities.add(activity);
        return activity;
    }

    private Activity createActivity(ActivityRequest request) {
        return switch (request.type().toLowerCase()) {
            case "run" -> new Run(
                    request.distance() != null ? request.distance() : 0.0,
                    request.avgPace() != null ? request.avgPace() : "",
                    request.title(), request.duration(),
                    request.minHeartRate(), request.maxHeartRate(),
                    request.avgHeartRate(), request.caloriesBurned()
            );
            case "swim" -> new Swim(request.title(), request.minHeartRate(),
                    request.duration(), request.maxHeartRate(),
                    request.avgHeartRate(), request.caloriesBurned());
            case "bike" -> new Bike(request.title(), request.minHeartRate(),
                    request.duration(), request.maxHeartRate(),
                    request.avgHeartRate(), request.caloriesBurned());
            case "walk" -> new Walk(request.title(), request.minHeartRate(),
                    request.duration(), request.maxHeartRate(),
                    request.avgHeartRate(), request.caloriesBurned());
            case "lifting" -> new Lifting(request.title(), request.minHeartRate(),
                    request.duration(), request.maxHeartRate(),
                    request.avgHeartRate(), request.caloriesBurned());
            default -> throw new IllegalArgumentException("Unknown activity type: " + request.type()
                    + ". Valid: run, swim, bike, walk, lifting");
        };
    }
}