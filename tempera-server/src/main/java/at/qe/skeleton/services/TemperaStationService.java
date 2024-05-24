package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.Unit;
import at.qe.skeleton.repositories.TemperaStationRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@Scope("application")
public class TemperaStationService {

  private final Logger logger = Logger.getLogger("TemperaStationServiceLogger");

  private final TemperaStationRepository temperaStationRepository;
  private final SensorService sensorService;

  public TemperaStationService(
      TemperaStationRepository temperaStationRepository, SensorService sensorService) {
    this.temperaStationRepository = temperaStationRepository;
    this.sensorService = sensorService;
  }

  /**
   * Creates and saves a new TemperaStation with the passed on ID. It also creates 4 new sensors for
   * this TemperaStation and assigns them to it, saving them in the db as well. This method should
   * be preferable used when creating a new TemperaStation instead of directly using the
   * constructor.
   *
   * @param id of the new TemperaStation, has to be unique
   * @param enabled whether the TemperaStation is enabled or not
   * @param user the user that is the owner of the TemperaStation (may be null if not yet assigned)
   * @return the newly created TemperaStation
   */
  public TemperaStation createTemperaStation(String id, boolean enabled, Userx user) {
    logger.info("creating new Temperastation with id %s".formatted(id));
    TemperaStation temperaStation = new TemperaStation(id, enabled, user, false);
    save(temperaStation);

    Sensor temperatureSensor = new Sensor(SensorType.HUMIDITY, Unit.PERCENT, temperaStation);
    sensorService.saveSensor(temperatureSensor);

    Sensor irradianceSensor = new Sensor(SensorType.IRRADIANCE, Unit.LUX, temperaStation);
    sensorService.saveSensor(irradianceSensor);

    Sensor humiditySensor = new Sensor(SensorType.TEMPERATURE, Unit.CELSIUS, temperaStation);
    sensorService.saveSensor(humiditySensor);

    Sensor nmvocSensor = new Sensor(SensorType.NMVOC, Unit.OHM, temperaStation);
    sensorService.saveSensor(nmvocSensor);

    return temperaStation;
  }

  public List<TemperaStation> getAllTemperaStations() {
    return temperaStationRepository.findAll();
  }

  public TemperaStation findById(String id) throws CouldNotFindEntityException {
    logger.info("trying to find TemperaStation with Id: %s".formatted(id));
    return temperaStationRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("TemperaStation %s".formatted(id)));
  }

  public Optional<TemperaStation> findByUser(Userx user) {
    logger.info("trying to find Temperastation of User %s".formatted(user));
    return temperaStationRepository.findFirstByUser(user);
  }

  public Optional<TemperaStation> findByUsername(String username) {
    return temperaStationRepository.findFirstByUser_Username(username);
  }

  public TemperaStation save(TemperaStation temperaStation) {
    logger.info("saving temperaStation %s".formatted(temperaStation.toString()));
    return temperaStationRepository.save(temperaStation);

  }

/**
 *  Deletes a TemperaStation and removes it from its corresponding AccessPoint.
 *  Be careful as this will also delete all corresponding sensors and with them all the
 *  Measurements associated with that sensor and Temperastation.
 */
  public void delete(TemperaStation temperaStation) {
    logger.info("deleting temperaStation %s".formatted(temperaStation.toString()));
    AccessPoint accessPoint = temperaStation.getAccessPoint();
    accessPoint.getTemperaStations().remove(temperaStation);
    temperaStationRepository.delete(temperaStation);
  }

  /**
   * Checks whether the tempera Station with the passed on ID is enabled or not.
   *
   * @param id of the tempera Station in question
   * @throws CouldNotFindEntityException if there is no TemperaStation with that id in the DB.
   */
  public boolean isEnabled(String id) throws CouldNotFindEntityException {
    TemperaStation station = findById(id);
    return station.isEnabled();
  }
}
