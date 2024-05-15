package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TimeRecordServiceTest {
  private TimeRecordService timeRecordServiceMockedDependencies;
  private TimeRecordService timeRecordServiceReal;
  private TemperaStationService temperaStationService;
  private SensorService sensorService;
  @Autowired private UserxService userxService;
  @Autowired private ExternalRecordRepository externalRecordRepository;
  @Autowired private InternalRecordRepository internalRecordRepository;
    @Autowired private TemperaStationRepository temperaStationRepository;
@Autowired private SensorRepository sensorRepository;
@Autowired private UserxRepository userxRepository;

  @Mock private ExternalRecordRepository externalRecordRepositoryMock;
  @Mock private InternalRecordRepository internalRecordRepositoryMock;
    @Mock private UserxRepository mockedUserxRepository;



  @BeforeEach
  void setUp() {
    timeRecordServiceMockedDependencies =
        new TimeRecordService(
                externalRecordRepositoryMock, internalRecordRepositoryMock, mockedUserxRepository);
    sensorService = new SensorService(sensorRepository);
    timeRecordServiceReal = new TimeRecordService(externalRecordRepository, internalRecordRepository, userxRepository);
    temperaStationService = new TemperaStationService(temperaStationRepository, sensorService);

  }

  @AfterEach
  void tearDown() {}

  @Test
  void findExternalRecordByIdFirstTimeRecord() {}

  @Test
  void findLastSavedTimeRecord() {}

  @Test
  @WithMockUser(
      username = "admin",
      roles = {"ADMIN"})
  void addRecordWithOlderRecordsMockedRepository() throws Exception {
    LocalDateTime startOld = LocalDateTime.now().minusHours(2);
    LocalDateTime startNew = LocalDateTime.now();
    long duration = ChronoUnit.SECONDS.between(startOld, startNew);
    Userx admin = userxService.loadUser("admin");

    TemperaStation temperaStation = new TemperaStation("temperaStation", true, admin);

    // create an older ExternalRecord
    ExternalRecord oldExternalRecord =
        new ExternalRecord(admin, startOld, duration, null, State.OUT_OF_OFFICE);
    InternalRecord oldInternalRecord = new InternalRecord(startOld);
    oldExternalRecord.addInternalRecord(oldInternalRecord);

    // method will call findLatestExternalRecordByUser which will call this method:
    when(externalRecordRepositoryMock.findFirstByUserAndEndIsNull(admin))
        .thenReturn(Optional.of(oldExternalRecord));

    // method will call finalizeOldTimeRecord which will call this method in Repository:
    when(externalRecordRepositoryMock.save(oldExternalRecord))
        .thenReturn(oldExternalRecord);
    when(internalRecordRepositoryMock.save(oldInternalRecord))
        .thenReturn(oldInternalRecord);

    // create a new ExternalRecord
    ExternalRecord newExternalRecord =
        new ExternalRecord(admin, startNew, 1L, null, State.DEEPWORK);
    InternalRecord newInternalRecord = new InternalRecord(startNew);

    // finalizeOldTimeRecord will call these methods:
    when(externalRecordRepositoryMock.save(newExternalRecord))
        .thenReturn(newExternalRecord);
    when(internalRecordRepositoryMock.save(newInternalRecord))
        .thenReturn(newInternalRecord);

    // call the method
    ExternalRecord result = timeRecordServiceMockedDependencies.addRecord(newExternalRecord);

    // check if the result is the new ExternalRecord
    assertEquals(newExternalRecord, result);
    // it should have saved both old and new ExternalRecord & InternalRecord
    verify(externalRecordRepositoryMock, times(1)).save(newExternalRecord);
    verify(internalRecordRepositoryMock, times(1)).save(newInternalRecord);
    verify(externalRecordRepositoryMock, times(1)).save(oldExternalRecord);
    verify(internalRecordRepositoryMock, times(1)).save(oldInternalRecord);
  }


  @Test
  @WithMockUser(
      username = "admin",
      roles = {"ADMIN"})
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:addRecordWithOlderRecordsRealRepositoryTest.sql")
  public void addRecordWithOlderRecordsRealRepository() throws Exception {

 LocalDateTime startNew = LocalDateTime.now();
 Userx admin = userxService.loadUser("admin");


    // create new ExternalRecord
    ExternalRecord newExternalRecord =
        new ExternalRecord(admin, startNew, 30L, null,State.DEEPWORK);

    // call the method
    ExternalRecord result = timeRecordServiceReal.addRecord(newExternalRecord);

    assertEquals(newExternalRecord.getStart(), result.getStart());
    assertEquals(newExternalRecord.getDuration(), result.getDuration());
    assertEquals(newExternalRecord.getState(), result.getState());
    assertEquals(newExternalRecord.getUser(), result.getUser());
    assertEquals(newExternalRecord.getId(), result.getId());
    assertEquals(2, externalRecordRepository.findAll().size());

    assertEquals(2, internalRecordRepository.findAll().size());
      ExternalRecordId oldExternalRecordId = new ExternalRecordId(LocalDateTime.of(2016, 1, 1, 0,0,0), admin.getUsername());
    ExternalRecord oldExternalRecord = externalRecordRepository.findById(oldExternalRecordId).orElseThrow(() -> new CouldNotFindEntityException("ExternalRecord"));

    assertEquals(1, oldExternalRecord.getInternalRecords().size());
    assertEquals(admin, oldExternalRecord.getUser());
    long difference = ChronoUnit.SECONDS.between(oldExternalRecord.getEnd(), newExternalRecord.getStart());
    assertEquals(1L, difference);
  }

  @Test
  void delete() {}


}
