package com.austinrippee.run4spring.trackconvert;

public record TrackConvertRequest(String gender, String event, String time, String fromTrack, String toTrack) {}