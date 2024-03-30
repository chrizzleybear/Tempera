package at.qe.skeleton.rest.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.repositories.SuperiorTimeRecordRepository;
import at.qe.skeleton.rest.dtos.SuperiorTimeRecordDto;
import at.qe.skeleton.rest.mappers.SuperiorTimeRecordMapper;
import at.qe.skeleton.services.TimeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//code was written with workshop 5 code as template
@RestController
@RequestMapping("/api/timerecord")
public class TimeRecordController {
    private TimeRecordService timeRecordService;
    private SuperiorTimeRecordMapper timeRecordMapper;

    public TimeRecordController(TimeRecordService timeRecordService, SuperiorTimeRecordMapper timeRecordMapper) {
        this.timeRecordService = timeRecordService;
        this.timeRecordMapper = timeRecordMapper;
    }

    @GetMapping("/{id}")
    private ResponseEntity<SuperiorTimeRecordDto> getTimeRecord(@PathVariable Long id){
        Optional<SuperiorTimeRecord> entity = timeRecordService.findSuperiorTimeRecordById(id);
        return entity.map(superiorTimeRecord -> ResponseEntity.ok(timeRecordMapper.mapToDto(superiorTimeRecord)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    private ResponseEntity<SuperiorTimeRecordDto> postTimeRecord(@RequestBody SuperiorTimeRecordDto timeRecordDto) {
        try {
        SuperiorTimeRecord entity = timeRecordService.save(timeRecordMapper.mapFromDto(timeRecordDto));
        return ResponseEntity.status(201).body(timeRecordMapper.mapToDto(entity));
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //todo: implement put & delete.
}
