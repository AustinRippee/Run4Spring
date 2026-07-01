package com.austinrippee.run4spring.trainingpaces;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TrainingPacesService {

    private static final Set<String> VALID_DISTANCES = Set.of(
            "800", "1500", "Mile", "3000", "5000", "10000", "HalfMarathon", "Marathon"
    );

    private final JdbcTemplate jdbc;

    public TrainingPacesService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public TrainingPacesResponse getPaces(TrainingPacesRequest request) {
        if (!VALID_DISTANCES.contains(request.distance())) {
            throw new IllegalArgumentException(
                    "Invalid distance. Valid options: " + VALID_DISTANCES);
        }

        double totalSeconds = parseTimeToSeconds(request.time());
        int vdot = findVdot(request.distance(), totalSeconds);

        if (vdot == -1) {
            throw new IllegalStateException("No matching VDOT found for the given distance and time.");
        }

        Map<String, String> paces = getPacesForVdot(vdot);
        return new TrainingPacesResponse(vdot, paces);
    }

    private int findVdot(String distance, double totalSeconds) {
        String sql = "SELECT vdot, \"" + distance + "\" FROM vdot_estimate ORDER BY \"" + distance + "\" ASC";
        int lastFasterVdot = -1;

        List<Map<String, Object>> rows = jdbc.queryForList(sql);
        for (Map<String, Object> row : rows) {
            Object timeObj = row.get(distance);
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
        String[] parts = time.split(":");
        if (parts.length == 3) {
            return Integer.parseInt(parts[0]) * 3600
                    + Integer.parseInt(parts[1]) * 60
                    + Double.parseDouble(parts[2]);
        } else if (parts.length == 2) {
            return Integer.parseInt(parts[0]) * 60
                    + Double.parseDouble(parts[1]);
        }
        return Double.parseDouble(parts[0]);
    }
}