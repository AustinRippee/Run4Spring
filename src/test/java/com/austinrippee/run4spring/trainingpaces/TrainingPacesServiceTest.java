package com.austinrippee.run4spring.trainingpaces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingPacesServiceTest {

    private final TrainingPacesService service = new TrainingPacesService(null);

    @Test
    void threeMinutesTo180Seconds() {
        assertEquals(180.0, service.parseTimeToSeconds("3:00"));
    }

    @Test
    void fiftyNineSecondsTo59Seconds() {
        assertEquals(59.0, service.parseTimeToSeconds("0:59"));
    }

    @Test
    void oneHourTo3600Seconds() {
        assertEquals(3600.0, service.parseTimeToSeconds("1:00:00"));
    }
}
