package com.austinrippee.run4spring.trainingpaces;

import java.util.Map;

public record TrainingPacesResponse(int vdot, Map<String, String> paces) {}