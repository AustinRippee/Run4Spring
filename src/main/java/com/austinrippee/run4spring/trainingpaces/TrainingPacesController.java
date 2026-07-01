package com.austinrippee.run4spring.trainingpaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training")
@CrossOrigin(origins = "*")
public class TrainingPacesController {

    private final TrainingPacesService service;

    public TrainingPacesController(TrainingPacesService service) {
        this.service = service;
    }

    @PostMapping("/paces")
    public ResponseEntity<?> getPaces(@RequestBody TrainingPacesRequest request) {
        try {
            return ResponseEntity.ok(service.getPaces(request));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
