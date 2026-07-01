package com.austinrippee.run4spring.trackconvert;

public record TrackConvertResponse(String gender, String event, String originalTime, String fromTrack, String toTrack, String convertedTime) {}