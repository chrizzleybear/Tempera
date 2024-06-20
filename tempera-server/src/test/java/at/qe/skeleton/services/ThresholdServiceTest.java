package at.qe.skeleton.services;

import at.qe.skeleton.model.Modification;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.ThresholdTip;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.repositories.TemperaStationRepository;
import at.qe.skeleton.repositories.ThresholdRepository;
import at.qe.skeleton.repositories.ThresholdTipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThresholdServiceTest {

    @Mock private ThresholdRepository thresholdRepository;
    @Mock private TemperaStationRepository temperaStationRepository;
    @Mock private ThresholdTipRepository thresholdTipRepository;
    @Mock private AuditLogService auditLogService;

    private ThresholdService thresholdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //doNothing().when(log.info(any(String.class)));
        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        this.thresholdService =
                new ThresholdService(
                        thresholdRepository,
                        temperaStationRepository,
                        thresholdTipRepository,
                        auditLogService
                );
    }

    @Test
    void testCreateThreshold() {
        Threshold threshold = new Threshold(
                SensorType.TEMPERATURE, ThresholdType.UPPERBOUND_INFO, 25.0,
                new Modification("reason"), new ThresholdTip("tip"));
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(threshold);

        Threshold createdThreshold = thresholdService.createThreshold(
                SensorType.TEMPERATURE, ThresholdType.UPPERBOUND_INFO, 25.0,
                "reason", "tip");

        assertNotNull(createdThreshold);
        assertEquals(SensorType.TEMPERATURE, createdThreshold.getSensorType());
        assertEquals(ThresholdType.UPPERBOUND_INFO, createdThreshold.getThresholdType());
        assertEquals(25.0, createdThreshold.getValue());
        assertEquals("reason", createdThreshold.getModification().getReason());
        assertEquals("tip", createdThreshold.getTip().getTip());
    }

    @Test
    void testGetThresholdsByTemperaId() {
        Set<Threshold> thresholds = new HashSet<>();
        thresholds.add(new Threshold());
        when(temperaStationRepository.getThresholdsByTemperaId(anyString())).thenReturn(thresholds);

        Set<Threshold> retrievedThresholds = thresholdService.getThresholdsByTemperaId("temperaId");

        assertNotNull(retrievedThresholds);
        assertFalse(retrievedThresholds.isEmpty());
    }

    @Test
    void testSaveThreshold() {
        Threshold threshold = new Threshold();
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(threshold);

        Threshold savedThreshold = thresholdService.saveThreshold(threshold);

        assertNotNull(savedThreshold);
        verify(thresholdRepository, times(1)).save(threshold);
    }

    @Test
    void testUpdateThreshold() {
        Threshold oldThreshold = new Threshold();
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(oldThreshold);

        Threshold updatedThreshold = thresholdService.updateThreshold(
                oldThreshold, SensorType.HUMIDITY, ThresholdType.LOWERBOUND_INFO,
                15.0, "reason", "tip");

        assertNotNull(updatedThreshold);
        assertEquals(SensorType.HUMIDITY, updatedThreshold.getSensorType());
        assertEquals(ThresholdType.LOWERBOUND_INFO, updatedThreshold.getThresholdType());
        assertEquals(15.0, updatedThreshold.getValue());
        assertEquals("reason", updatedThreshold.getModification().getReason());
        assertEquals("tip", updatedThreshold.getTip().getTip());
    }

    @Test
    void testDeleteThreshold() {
        Threshold threshold = new Threshold();
        when(thresholdRepository.findById(anyLong())).thenReturn(Optional.of(threshold));

        thresholdService.deleteThreshold(1L);

        verify(thresholdRepository, times(1)).delete(threshold);
    }

    @Test
    void testUpdateThresholdTip() {
        ThresholdTip tip = new ThresholdTip("tip");
        when(thresholdTipRepository.findById(tip.getId())).thenReturn(Optional.of(tip));
        when(thresholdTipRepository.save(any(ThresholdTip.class))).thenReturn(tip);

        ThresholdTip updatedTip = thresholdService.updateThresholdTip(tip);

        assertNotNull(updatedTip);
        assertEquals("tip", updatedTip.getTip());
        verify(auditLogService, times(1)).logEvent(any(), any(), any());
    }
}


