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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    private TimeRecordService timeRecordService;


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

    // create a new ExternalRecord
    ExternalRecord newExternalRecord =
        new ExternalRecord(admin, startNew, 1L, null, State.DEEPWORK);
    InternalRecord newInternalRecord = new InternalRecord(startNew);

    // finalizeOldTimeRecord will call these methods:
    when(externalRecordRepositoryMock.save(newExternalRecord))
        .thenReturn(newExternalRecord);


    // call the method
    ExternalRecord result = timeRecordServiceMockedDependencies.addRecord(newExternalRecord);

    // check if the result is the new ExternalRecord
    assertEquals(newExternalRecord, result);
    // it should have saved both old and new ExternalRecord & InternalRecord
    verify(externalRecordRepositoryMock, times(1)).save(newExternalRecord);
    verify(externalRecordRepositoryMock, times(1)).save(oldExternalRecord);
  }


  @Test
  @Transactional
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:addRecordWithOlderRecordsRealRepositoryTest.sql")
  @WithMockUser(
          username = "admin",
          roles = {"ADMIN"})
  public void addRecordWithOlderRecordsRealRepository() throws Exception {

 LocalDateTime startNew = LocalDateTime.of(2020, 10, 10, 12, 30, 45, 100000000);
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
    long difference = ChronoUnit.MILLIS.between(oldExternalRecord.getEnd(), newExternalRecord.getStart());
    assertEquals(10L, difference);
  }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:addRecordWithOlderRecordsRealRepositoryTest.sql")
    @WithMockUser(username = "admin", roles = {"EMPLOYEE"})
    public void externalRecordWithATightSchedule() throws Exception{

      Userx admin = userxService.loadUser("admin");
        ExternalRecord oldRecord = timeRecordServiceReal.findLatestExternalRecordByUser(admin).get();
        assertNotNull(oldRecord);

        LocalDateTime oldStart = oldRecord.getStart();
    System.out.println(oldStart);
        // now we will send one that starts just a 10/th of a second later, thats the closest leo will send.
        LocalDateTime newStart = oldStart.plus(100, ChronoUnit.MILLIS);
        ExternalRecord newRecord = new ExternalRecord(admin, newStart, 0L, null, State.OUT_OF_OFFICE);
        assertDoesNotThrow(() -> timeRecordServiceReal.addRecord(newRecord));
        Optional<ExternalRecord> recordAfterAddingOptional = timeRecordServiceReal.findLatestExternalRecordByUser(admin);
        assertEquals(newRecord.getStart(), recordAfterAddingOptional.get().getStart());

        Optional<ExternalRecord> oldRecrdAfterAddingOptional = timeRecordServiceReal.findExternalRecordByStartAndUser(oldStart, admin);
        assertEquals(newStart.minus(10, ChronoUnit.MILLIS), oldRecrdAfterAddingOptional.get().getEnd());
  }

    @Test
  void delete() {}


}
