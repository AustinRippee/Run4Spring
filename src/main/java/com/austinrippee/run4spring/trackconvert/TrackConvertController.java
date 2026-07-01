package com.austinrippee.run4spring.trackconvert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/track")
@CrossOrigin(origins = "*")
public class TrackConvertController {

    private final TrackConvertService service;

    public TrackConvertController(TrackConvertService service) {
        this.service = service;
    }

    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody TrackConvertRequest request) {
        try {
            return ResponseEntity.ok(service.convert(request));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
