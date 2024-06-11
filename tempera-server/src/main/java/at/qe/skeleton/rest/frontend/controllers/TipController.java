package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.ThresholdTip;
import at.qe.skeleton.services.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/tip", produces = "application/json")
public class TipController {

    @Autowired
    private ThresholdService thresholdService;

    @GetMapping("/all")
    public ResponseEntity<List<ThresholdTip>> getAllThresholds() {
        List<ThresholdTip> thresholds = thresholdService.getAllThresholdTips();
        return ResponseEntity.ok(thresholds);
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateTip(@RequestBody ThresholdTip threshold) {
        try {
            thresholdService.updateThresholdTip(threshold);
            return ResponseEntity.ok("Threshold updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
