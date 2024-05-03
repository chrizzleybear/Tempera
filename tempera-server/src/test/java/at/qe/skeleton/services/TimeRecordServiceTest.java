package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.SubordinateTimeRecord;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.repositories.*;
import org.hibernate.annotations.processing.SQL;
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
  @Autowired private SuperiorTimeRecordRepository superiorTimeRecordRepository;
  @Autowired private SubordinateTimeRecordRepository subordinateTimeRecordRepository;
    @Autowired private TemperaStationRepository temperaStationRepository;
@Autowired private SensorRepository sensorRepository;
@Autowired private UserxRepository userxRepository;

  @Mock private SuperiorTimeRecordRepository superiorTimeRecordRepositoryMock;
  @Mock private SubordinateTimeRecordRepository subordinateTimeRecordRepositoryMock;
    @Mock private UserxRepository mockedUserxRepository;



  @BeforeEach
  void setUp() {
    timeRecordServiceMockedDependencies =
        new TimeRecordService(
            superiorTimeRecordRepositoryMock, subordinateTimeRecordRepositoryMock, mockedUserxRepository);
    sensorService = new SensorService(sensorRepository);
    timeRecordServiceReal = new TimeRecordService(superiorTimeRecordRepository, subordinateTimeRecordRepository, userxRepository);
    temperaStationService = new TemperaStationService(temperaStationRepository, sensorService);

  }

  @AfterEach
  void tearDown() {}

  @Test
  void findSuperiorTimeRecordByIdFirstTimeRecord() {}

  @Test
  void findLastSavedTimeRecord() {}

  @Test
  @WithMockUser(
      username = "admin",
      roles = {"ADMIN"})
  void addRecordWithOlderRecordsMockedRepository() throws Exception {
    LocalDateTime startOld = LocalDateTime.now().minusHours(2);
    LocalDateTime startNew = LocalDateTime.now();
    Userx admin = userxService.loadUser("admin");

    TemperaStation temperaStation = new TemperaStation("temperaStation", true, admin);

    // create an older SuperiorTimeRecord
    SuperiorTimeRecord oldSuperiorTimeRecord =
        new SuperiorTimeRecord(temperaStation, startOld, null, State.OUT_OF_OFFICE);
    SubordinateTimeRecord oldSubordinateTimeRecord = new SubordinateTimeRecord(startOld);
    oldSuperiorTimeRecord.addSubordinateTimeRecord(oldSubordinateTimeRecord);

    // method will call findLastSavedTimeRecordByUser which will call this method:
    when(userxRepository.findLastSuperiorTimeRecordByUser(admin))
        .thenReturn(Optional.of(oldSuperiorTimeRecord));

    // method will call finalizeOldTimeRecord which will call this method in Repository:
    when(superiorTimeRecordRepositoryMock.save(oldSuperiorTimeRecord))
        .thenReturn(oldSuperiorTimeRecord);
    when(subordinateTimeRecordRepositoryMock.save(oldSubordinateTimeRecord))
        .thenReturn(oldSubordinateTimeRecord);

    // create a new SuperiorTimeRecord
    SuperiorTimeRecord newSuperiorTimeRecord =
        new SuperiorTimeRecord(temperaStation, startNew, null, State.DEEPWORK);
    SubordinateTimeRecord newSubordinateTimeRecord = new SubordinateTimeRecord(startNew);

    // finalizeOldTimeRecord will call these methods:
    when(superiorTimeRecordRepositoryMock.save(newSuperiorTimeRecord))
        .thenReturn(newSuperiorTimeRecord);
    when(subordinateTimeRecordRepositoryMock.save(newSubordinateTimeRecord))
        .thenReturn(newSubordinateTimeRecord);

    // call the method
    SuperiorTimeRecord result = timeRecordServiceMockedDependencies.addRecord(newSuperiorTimeRecord);

    // check if the result is the new SuperiorTimeRecord
    assertEquals(newSuperiorTimeRecord, result);
    // it should have saved both old and new SuperiorTimeRecord & SubordinateTimeRecord
    verify(superiorTimeRecordRepositoryMock, times(1)).save(newSuperiorTimeRecord);
    verify(subordinateTimeRecordRepositoryMock, times(1)).save(newSubordinateTimeRecord);
    verify(superiorTimeRecordRepositoryMock, times(1)).save(oldSuperiorTimeRecord);
    verify(subordinateTimeRecordRepositoryMock, times(1)).save(oldSubordinateTimeRecord);
  }
  @Test
  @WithMockUser(
      username = "admin",
      roles = {"ADMIN"})
    public void transactionTest() throws Exception{
    Userx user = userxRepository.findByUsername("admin").orElseThrow(Exception::new);
    assertEquals(0, user.getSuperiorTimeRecords().size());
    TemperaStation temperaStation = temperaStationRepository.findById("tempera_station_1").get();
    LocalDateTime startOld = LocalDateTime.now().minusHours(2);

    SuperiorTimeRecord oldSuperiorTimeRecord =
            new SuperiorTimeRecord(temperaStation, startOld, null, State.OUT_OF_OFFICE);


    }
  @Test
  @WithMockUser(
      username = "admin",
      roles = {"ADMIN"})
  public void addRecordWithOlderRecordsRealRepository() throws CouldNotFindEntityException {
//    superiorTimeRecordRepository.findAll().forEach(superiorTimeRecordRepository::delete);
//    subordinateTimeRecordRepository.findAll().forEach(subordinateTimeRecordRepository::delete);
//    temperaStationRepository.findAll().forEach(temperaStationRepository::delete);
//    assertEquals(0, superiorTimeRecordRepository.findAll().size());
//    assertEquals(0, subordinateTimeRecordRepository.findAll().size());

//    LocalDateTime startOld = LocalDateTime.now().minusHours(2);
 LocalDateTime startNew = LocalDateTime.now();
//    Userx admin = userxService.loadUser("admin");
//  //  TemperaStation temperaStation = new TemperaStation("tempera_station_1", true, admin);
////    temperaStationService.save(temperaStation);
//
//    // create and save older SuperiorTimeRecord
  TemperaStation temperaStation = temperaStationRepository.findById("tempera_station_1").get();
//    SuperiorTimeRecord oldSuperiorTimeRecord =
//        new SuperiorTimeRecord(temperaStation, startOld, null, State.OUT_OF_OFFICE);
//    SubordinateTimeRecord oldSubordinateTimeRecord = new SubordinateTimeRecord(startOld);
//    oldSuperiorTimeRecord.addSubordinateTimeRecord(oldSubordinateTimeRecord);
//    subordinateTimeRecordRepository.save(oldSubordinateTimeRecord);
//    superiorTimeRecordRepository.save(oldSuperiorTimeRecord);
//    assertEquals(1, superiorTimeRecordRepository.findAll().size());
//    assertEquals(1, subordinateTimeRecordRepository.findAll().size());
//    admin.addSuperiorTimeRecord(oldSuperiorTimeRecord);
//    userxRepository.save(admin);

    // create new SuperiorTimeRecord
    SuperiorTimeRecord newSuperiorTimeRecord =
        new SuperiorTimeRecord(temperaStation, startNew, 30L, State.DEEPWORK);

    // call the method
    SuperiorTimeRecord result = timeRecordServiceReal.addRecord(newSuperiorTimeRecord);

    assertEquals(newSuperiorTimeRecord, result);
    assertEquals(2, superiorTimeRecordRepository.findAll().size());
  }

  @Test
  void delete() {}
  // todo: add tests for TimeRecordService

}
