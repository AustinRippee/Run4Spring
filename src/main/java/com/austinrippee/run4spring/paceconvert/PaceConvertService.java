package com.austinrippee.run4spring.paceconvert;

import org.springframework.stereotype.Service;

@Service
public class PaceConvertService {

    public PaceConvertResponse convert(PaceConvertRequest request) {
        double totalSeconds = parseTimeToSeconds(request.time());
        double score = computeScore(request.distance(), totalSeconds);
        String convertedTime = computeConvertedTime(request.convertTo(), score);
        return new PaceConvertResponse(request.distance(), request.time(), request.convertTo(), convertedTime);
    }

    private double parseTimeToSeconds(String time) {
        if (time.contains(":")) {
            String[] parts = time.split(":");
            if (parts.length == 3) {
                return Double.parseDouble(parts[0]) * 3600
                        + Double.parseDouble(parts[1]) * 60
                        + Double.parseDouble(parts[2]);
            } else {
                return Double.parseDouble(parts[0]) * 60
                        + Double.parseDouble(parts[1]);
            }
        }
        return Double.parseDouble(time);
    }

    private double computeScore(String distance, double t) {
        return switch (distance.toLowerCase().trim()) {
            case "100m", "100" -> 290.52712 * (100.0 / t) - 1953.2266;
            case "150m", "150" -> 265.3031224 * (150.0 / t) - 1720.7734;
            case "200m", "200" -> 267.75893 * (200.0 / t) - 1703.6447;
            case "300m", "300" -> 251.7769577 * (300.0 / t) - 1414.90071;
            case "400m", "400" -> 262.37121 * (400.0 / t) - 1402.7708;
            case "600m", "600" -> 285.7637 * (600.0 / t) - 1371.563558;
            case "800m", "800" -> 302.9089 * (800.0 / t) - 1377.5673;
            case "1000m", "1000", "1,000", "1,000m", "1k", "1km" -> 313.6503268 * (1000.0 / t) - 1374.25166;
            case "1500m", "1500", "1,500", "1,500m", "1.5k", "1.5km" -> 320.6038 * (1500.0 / t) - 1314.0045;
            case "1600m", "1600", "1,600", "1,600m", "1.6k", "1.6km" -> 321.7731201 * (1600.0 / t) - 1306.325127;
            case "1 mile", "1mile", "mile", "one mile", "1609", "1609m", "1mi" ->
                    321.7731201 * (1609.34 / t) - 1306.285127;
            case "2000m", "2000", "2,000", "2,000m", "2k" -> 328.2988442 * (2000.0 / t) - 1303.430804;
            case "3000m", "3000", "3,000", "3,000m", "3k" -> 331.264214 * (3000.0 / t) - 1240.294895;
            case "3200m", "3200", "3,200", "3,200m" -> 333.4505158 * (3200.0 / t) - 1241.705275;
            case "2 mile", "2 miles", "2mile", "2miles", "two miles", "two mile", "3218", "3218m", "2mi" ->
                    333.4505158 * (2 * 1609.34 / t) - 1241.705275;
            case "5000m", "5,000m", "5000", "5,000", "5k", "5km" -> 342.8535 * (5000.0 / t) - 1234.1959;
            case "6000m", "6,000m", "6000", "6,000", "6k", "6km" -> 344.0777994 * (6000.0 / t) - 1217.284313;
            case "8000m", "8,000m", "8000", "8,000", "8k", "8km" -> 348.6258257 * (8000.0 / t) - 1192.426848;
            case "10000m", "10,000m", "10000", "10,000", "10k", "10km" -> 349.8535 * (10000.0 / t) - 1171.2847;
            case "10 miles", "10mi" -> 360.6890152 * (10 * 1609.34 / t) - 1164.451907;
            case "halfm", "half marathon", "hm", "half", "13.1", "13.1mi", "13.1 miles" ->
                    366.3739581 * ((42194.99 / 2) / t) - 1168.783894;
            case "marathon", "m", "full marathon", "full", "mara", "26.2", "26.2mi", "26.2 miles" ->
                    384.5408 * (42194.99 / t) - 1161.8021;
            default -> throw new IllegalArgumentException("Invalid race distance: " + distance);
        };
    }

    private String computeConvertedTime(String target, double score) {
        double totalSecs = switch (target.toLowerCase().trim()) {
            case "100m", "100" -> 100.0 / (0.003439 * score + 6.72526);
            case "150m", "150" -> 150.0 / (0.003768768 * score + 6.486427968);
            case "200m", "200" -> 200.0 / (0.003734 * score + 6.36315);
            case "300m", "300" -> 300.0 / (0.003970935 * score + 5.620258201);
            case "400m", "400" -> 400.0 / (0.0038105 * score + 5.34719);
            case "600m", "600" -> 600.0 / (0.003499 * score + 4.80022);
            case "800m", "800" -> 800.0 / (0.003300 * score + 4.54844);
            case "1000m", "1000", "1,000", "1,000m", "1k", "1km" ->
                    1000.0 / (0.00318746 * score + 4.382052887);
            case "1500m", "1500", "1,500", "1,500m", "1.5k", "1.5km" ->
                    1500.0 / (0.003117 * score + 4.09988);
            case "1600m", "1600", "1,600", "1,600m", "1.6k", "1.6km" ->
                    1600.0 / (0.0031062 * score + 4.060811045);
            case "1 mile", "1mile", "mile", "one mile", "1609", "1609m", "1mi" ->
                    1609.34 / (0.0031062 * score + 4.060811045);
            case "2000m", "2000", "2,000", "2,000m", "2k" ->
                    2000.0 / (0.0030444 * score + 3.97139601);
            case "3000m", "3000", "3,000", "3,000m", "3k" ->
                    3000.0 / (0.0030147 * score + 3.74703401);
            case "3200m", "3200", "3,200", "3,200m" ->
                    3200.0 / (0.0029949 * score + 3.726724);
            case "2 mile", "2 miles", "2mile", "2miles", "two miles", "two mile", "3218", "3218m", "2mi" ->
                    (2 * 1609.34) / (0.0029949 * score + 3.726724);
            case "5000m", "5,000m", "5000", "5,000", "5k", "5km" ->
                    5000.0 / (0.0029129 * score + 3.602496);
            case "6000m", "6,000m", "6000", "6,000", "6k", "6km" ->
                    6000.0 / (0.0029028 * score + 3.5403469);
            case "8000m", "8,000m", "8000", "8,000", "8k", "8km" ->
                    8000.0 / (0.0028672 * score + 3.4212486);
            case "10000m", "10,000m", "10000", "10,000", "10k", "10km" ->
                    10000.0 / (0.002857 * score + 3.348169);
            case "10 miles", "10mi" ->
                    16093.4 / (0.0027716 * score + 3.2290278);
            case "halfm", "half marathon", "hm", "half", "13.1", "13.1mi", "13.1 miles" ->
                    (42194.99 / 2) / (0.0026921 * score + 3.098524);
            case "marathon", "m", "full marathon", "full", "mara", "26.2", "26.2mi", "26.2 miles" ->
                    42194.99 / (0.0026921 * score + 3.098524);
            default -> throw new IllegalArgumentException("Invalid target distance: " + target);
        };
        return formatTime(totalSecs);
    }

    private String formatTime(double totalSeconds) {
        if (totalSeconds >= 3600) {
            int hours = (int) (totalSeconds / 3600);
            int minutes = (int) ((totalSeconds % 3600) / 60);
            double seconds = totalSeconds % 60;
            return String.format("%d:%02d:%05.2f", hours, minutes, seconds);
        }
        int minutes = (int) (totalSeconds / 60);
        double seconds = totalSeconds % 60;
        return String.format("%d:%05.2f", minutes, seconds);
    }
}