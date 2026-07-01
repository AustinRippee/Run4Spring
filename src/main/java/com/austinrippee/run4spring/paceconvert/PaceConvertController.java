package com.austinrippee.run4spring.paceconvert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pace")
@CrossOrigin(origins = "*")
public class PaceConvertController {

    private final PaceConvertService service;

    public PaceConvertController(PaceConvertService service) {
        this.service = service;
    }

    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody PaceConvertRequest request) {
        try {
            return ResponseEntity.ok(service.convert(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
