package com.austinrippee.run4spring.trackconvert;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TrackConvertService {

    private static final Set<String> VALID_TRACKS = Set.of("undersized", "flat", "oversized", "banked");
    private static final Set<String> VALID_EVENTS = Set.of("200", "400", "800", "1000", "Mile", "3000", "5000", "4x400", "DMR");
    private static final Set<String> VALID_GENDERS = Set.of("M", "F");
    private static final Set<String> VALID_CONVERSIONS = Set.of(
            "undersized_to_flat", "undersized_to_banked", "oversized_to_flat",
            "flat_to_oversized", "undersized_to_oversized", "flat_to_banked", "banked_to_flat"
    );

    private final JdbcTemplate jdbc;

    public TrackConvertService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public TrackConvertResponse convert(TrackConvertRequest request) {
        String gender = request.gender().toUpperCase();
        String fromTrack = request.fromTrack().toLowerCase();
        String toTrack = request.toTrack().toLowerCase();

        if (!VALID_GENDERS.contains(gender)) {
            throw new IllegalArgumentException("Invalid gender. Use 'M' or 'F'.");
        }
        if (!VALID_EVENTS.contains(request.event())) {
            throw new IllegalArgumentException("Invalid event: " + request.event() + ". Valid: " + VALID_EVENTS);
        }
        if (!VALID_TRACKS.contains(fromTrack)) {
            throw new IllegalArgumentException("Invalid fromTrack: " + fromTrack + ". Valid: " + VALID_TRACKS);
        }
        if (!VALID_TRACKS.contains(toTrack)) {
            throw new IllegalArgumentException("Invalid toTrack: " + toTrack + ". Valid: " + VALID_TRACKS);
        }

        String conversionColumn = fromTrack + "_to_" + toTrack;
        if (!VALID_CONVERSIONS.contains(conversionColumn)) {
            throw new IllegalArgumentException("Unsupported track conversion: " + conversionColumn);
        }

        double inputSeconds = parseTimeToSeconds(request.time());
        String sql = "SELECT " + conversionColumn + " FROM track_conversions WHERE gender = ? AND event = ?";
        String multiplierStr = jdbc.queryForObject(sql, String.class, gender, request.event());

        if (multiplierStr == null) {
            throw new IllegalStateException("No conversion data found for the given inputs.");
        }

        double convertedSeconds = Double.parseDouble(multiplierStr) * inputSeconds;
        return new TrackConvertResponse(gender, request.event(), request.time(),
                fromTrack, toTrack, formatTime(convertedSeconds));
    }

    private double parseTimeToSeconds(String time) {
        String[] parts = time.split(":");
        if (parts.length == 3) {
            return Double.parseDouble(parts[0]) * 3600
                    + Double.parseDouble(parts[1]) * 60
                    + Double.parseDouble(parts[2]);
        } else if (parts.length == 2) {
            return Double.parseDouble(parts[0]) * 60
                    + Double.parseDouble(parts[1]);
        }
        return Double.parseDouble(parts[0]);
    }

    private String formatTime(double totalSeconds) {
        int minutes = (int) (totalSeconds / 60);
        double seconds = totalSeconds % 60;
        return String.format("%d:%05.2f", minutes, seconds);
    }
}