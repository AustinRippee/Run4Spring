package com.austinrippee.run4spring.trainingpaces;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TrainingPacesService {

    private static final Map<String, String> DISTANCE_TO_COLUMN;
    static {
        DISTANCE_TO_COLUMN = new HashMap<>();
        DISTANCE_TO_COLUMN.put("800", "800");
        DISTANCE_TO_COLUMN.put("1500", "1500");
        DISTANCE_TO_COLUMN.put("Mile", "mile");
        DISTANCE_TO_COLUMN.put("3000", "3000");
        DISTANCE_TO_COLUMN.put("5000", "5000");
        DISTANCE_TO_COLUMN.put("10000", "10000");
        DISTANCE_TO_COLUMN.put("HalfMarathon", "halfmara");
        DISTANCE_TO_COLUMN.put("Marathon", "marathon");
    }

    private static final Set<String> VALID_DISTANCES = DISTANCE_TO_COLUMN.keySet();

    private static final Set<String> VALID_THRESHOLD_UNITS = Set.of(
            "threshold_400", "threshold_km", "threshold_mi"
    );

    private final JdbcTemplate jdbc;

    public TrainingPacesService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public TrainingPacesResponse getPaces(TrainingPacesRequest request) {
        if ("vdot".equals(request.inputType())) {
            int inputVdot;
            try {
                inputVdot = Integer.parseInt(request.vdotInput());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("VDOT score must be a whole number.");
            }
            Map<String, String> paces = getPacesForVdot(inputVdot);
            if (paces.isEmpty()) {
                throw new IllegalStateException("No training paces found for VDOT " + inputVdot + ".");
            }
            return new TrainingPacesResponse(inputVdot, paces);
        }

        double totalSeconds = parseTimeToSeconds(request.time());
        int vdot;

        if ("threshold".equals(request.inputType())) {
            if (!VALID_THRESHOLD_UNITS.contains(request.thresholdUnit())) {
                throw new IllegalArgumentException(
                        "Invalid threshold unit. Valid options: threshold_400, threshold_km, threshold_mi");
            }
            vdot = findVdotFromThreshold(request.thresholdUnit(), totalSeconds);
        } else {
            if (!VALID_DISTANCES.contains(request.distance())) {
                throw new IllegalArgumentException(
                        "Invalid distance. Valid options: " + VALID_DISTANCES);
            }
            vdot = findVdot(request.distance(), totalSeconds);
        }

        if (vdot == -1) {
            throw new IllegalStateException("No matching VDOT found for the given input.");
        }

        Map<String, String> paces = getPacesForVdot(vdot);
        return new TrainingPacesResponse(vdot, paces);
    }

    private int findVdot(String distance, double totalSeconds) {
        String column = DISTANCE_TO_COLUMN.get(distance);
        String sql = "SELECT vdot, \"" + column + "\" FROM vdot_estimate ORDER BY vdot DESC";
        int lastFasterVdot = -1;

        List<Map<String, Object>> rows = jdbc.queryForList(sql);
        for (Map<String, Object> row : rows) {
            Object timeObj = row.get(column);
            if (timeObj == null) continue;
            double rowSeconds = parseTimeToSeconds(timeObj.toString());
            if (rowSeconds < totalSeconds) {
                Object vdotObj = row.get("vdot");
                if (vdotObj != null) {
                    lastFasterVdot = ((Number) vdotObj).intValue();
                }
            } else {
                break;
            }
        }
        return lastFasterVdot;
    }

    private int findVdotFromThreshold(String column, double totalSeconds) {
        String sql = "SELECT vdot, \"" + column + "\" FROM vdot_paces";
        int bestVdot = -1;
        double bestDiff = Double.MAX_VALUE;

        List<Map<String, Object>> rows = jdbc.queryForList(sql);
        for (Map<String, Object> row : rows) {
            Object paceObj = row.get(column);
            if (paceObj == null) continue;
            double diff = Math.abs(parseTimeToSeconds(paceObj.toString()) - totalSeconds);
            if (diff < bestDiff) {
                bestDiff = diff;
                Object vdotObj = row.get("vdot");
                if (vdotObj != null) bestVdot = ((Number) vdotObj).intValue();
            }
        }
        return bestVdot;
    }

    private Map<String, String> getPacesForVdot(int vdot) {
        return jdbc.query("SELECT * FROM vdot_paces WHERE vdot = ?", rs -> {
            if (rs.next()) {
                ResultSetMetaData meta = rs.getMetaData();
                Map<String, String> paces = new LinkedHashMap<>();
                for (int col = 2; col <= meta.getColumnCount(); col++) {
                    paces.put(meta.getColumnName(col), rs.getString(col));
                }
                return paces;
            }
            return new LinkedHashMap<>();
        }, vdot);
    }

    public double parseTimeToSeconds(String time) {
        if (time == null || time.isEmpty()) return Double.MAX_VALUE;

        boolean threePart = time.matches("^\\d{1,2}:\\d{2}:\\d{2}(\\.\\d{1,3})?$");
        boolean twoPart = time.matches("^\\d{1,2}:\\d{2}(\\.\\d{1,3})?$");

        if (!threePart && !twoPart) {
            throw new IllegalArgumentException(
                    "Invalid time format. Use MM:SS[.SSS] or H:MM:SS[.SSS] (e.g. 3:45.20 or 1:02:30)");
        }

        String[] parts = time.split(":");
        if (parts.length == 3) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            double secs = Double.parseDouble(parts[2]);
            if (minutes > 59)
                throw new IllegalArgumentException("Minutes must be between 00 and 59");
            if (secs >= 60.0)
                throw new IllegalArgumentException("Seconds must be between 00 and 59.999");
            return hours * 3600.0 + minutes * 60.0 + secs;
        } else {
            int minutes = Integer.parseInt(parts[0]);
            double secs = Double.parseDouble(parts[1]);
            if (secs >= 60.0)
                throw new IllegalArgumentException("Seconds must be between 00 and 59.999");
            return minutes * 60.0 + secs;
        }
    }
}