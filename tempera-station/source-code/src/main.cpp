
// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Libraries for the BME sensor module
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"

// Include further headers
#include "../include/classesStructs.h"
#include "../include/parameters.h"
#include "../include/functions.h"
#include "../include/BLEfunctions.h"
#include "../include/BLEClassesStructs.h"



// ############### BLE SETUP ###############

// Set up the device information service
BLEService deviceInformationService("180A");
BLEStringCharacteristic manufacturerNameCharacteristic("2A29", BLERead, 64);
BLEStringCharacteristic serialNumberCharacteristic("2A25", BLERead, 64);

// Set up the elapsed time service for time tracking
BLEService elapsedTimeService("183F");
BLECharacteristic currentElapsedTimeCharacteristic("2BF2", BLERead | BLEIndicate, sizeof(elapsedTimeCharacteristicStructure));

// Set up the environmental sensing service for room climate measurements
BLEService environmentalSensingService("181A");
roomClimateStructure roomClimateData = {0, 0, 0, 0}; // explicit initialization is required for sizeof operation below
BLECharacteristic temperatureCharacteristic("2A6E", BLERead, sizeof(roomClimateData.temperature));
BLECharacteristic irradianceCharacteristic("2A77", BLERead, sizeof(roomClimateData.irradiance));
BLECharacteristic humidityCharacteristic("2A6F", BLERead, sizeof(roomClimateData.humidity));
// nmvocCharacteristic does not adhere to the BLE specifications, see definition of nmvocCharacteristicStructure  
BLECharacteristic nmvocCharacteristic("2BD3", BLERead, sizeof(roomClimateData.nmvoc));



// ############### SETUP CODE ###############

// Create objects and set variables
unsigned long lastTimeUpdate = millis();
unsigned long lastRoomClimateUpdate = millis();
LED led;
timedSession session;
elapsedTimeCharacteristicStructure currentElapsedTime = {0, 0, 0, 0, 1, 0};



void setup() {
  // Setup for the built-in LED
  pinMode(LED_BUILTIN, OUTPUT);

  // Setup for the rgb-led pins
  pinMode(LED_R, OUTPUT);
  pinMode(LED_G, OUTPUT);
  pinMode(LED_B, OUTPUT);

  // Setup for the button pins
  pinMode(DW, INPUT_PULLUP);
  pinMode(MT, INPUT_PULLUP);
  pinMode(OO, INPUT_PULLUP);
  pinMode(PT, INPUT_PULLUP);

  // Set serial output data rate in bits/s
  Serial.begin(SERIAL_DATA_RATE);
  if (INFO) Serial.println("Tempera > [INFO] Serial started");

  // Set to Out-Of-Office when device is started
  session.workMode = 4;
  session.startTime = millis();
  session.lastSessionDuration = 0;
  led.setColor(findButtonColor(session.workMode));
  led.turnOn();

  // BLE: Setup for BLE connectivity and device infos
  if (ERROR) {
    if (!BLE.begin()) {
      Serial.println("Tempera > [ERROR] Starting BLE failed!");
      while(1);
    }
    Serial.println("Tempera > [INFO] BLE startet.");
  }
  BLE.setEventHandler(BLEConnected, blePeripheralConnectHandler);
  BLE.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);
  BLE.setLocalName(DEVICE_NAME);
  BLE.setDeviceName(DEVICE_NAME);
  BLE.setAdvertisedService(deviceInformationService);

  // BLE: Setup for user description and serial number
  BLEDescriptor deviceInfoDescriptor("2901", "Sensor station with group number");
  manufacturerNameCharacteristic.addDescriptor(deviceInfoDescriptor);
  manufacturerNameCharacteristic.writeValue("G4T1");
  manufacturerNameCharacteristic.setEventHandler(BLERead, readManufacturerName);
  deviceInformationService.addCharacteristic(manufacturerNameCharacteristic);

  BLEDescriptor serialInfoDescriptor("2901", "Unique serial number");
  serialNumberCharacteristic.addDescriptor(serialInfoDescriptor);
  serialNumberCharacteristic.writeValue(DEVICE_ID);
  serialNumberCharacteristic.setEventHandler(BLERead, readSerialNumber);
  deviceInformationService.addCharacteristic(serialNumberCharacteristic);

  BLE.addService(deviceInformationService);

  // BLE: Setup for elapsed time characteristic
  BLEDescriptor elapsedTimeDescriptor("2901", "Elapsed Time service used for time tracking.");
  currentElapsedTimeCharacteristic.addDescriptor(elapsedTimeDescriptor);
  currentElapsedTimeCharacteristic.setEventHandler(BLERead, readElapsedTime);
  elapsedTimeService.addCharacteristic(currentElapsedTimeCharacteristic);
  BLE.addService(elapsedTimeService);

  // BLE: Setup for room climate data
  // to-do: maybe set unique functions for the event handlers
  BLEDescriptor temperatureCharacteristicDescriptor("2901", "Last measured temperature in deg. C.");
  temperatureCharacteristic.addDescriptor(temperatureCharacteristicDescriptor);
  temperatureCharacteristic.setEventHandler(BLERead, readAnyRoomClimateData);

  BLEDescriptor irradianceCharacteristicDescriptor("2901", "Last measured irradiance in W/m^2.");
  irradianceCharacteristic.addDescriptor(irradianceCharacteristicDescriptor);
  irradianceCharacteristic.setEventHandler(BLERead, readAnyRoomClimateData);

  BLEDescriptor humidityCharacteristicDescriptor("2901", "Last measured relative humidity in %.");
  humidityCharacteristic.addDescriptor(humidityCharacteristicDescriptor);
  humidityCharacteristic.setEventHandler(BLERead, readAnyRoomClimateData);

  BLEDescriptor nmvocCharacteristicDescriptor("2901", "Last measured non-methane-volatile-organic-compound-concentration in g/m^3.");
  nmvocCharacteristic.addDescriptor(nmvocCharacteristicDescriptor);
  nmvocCharacteristic.setEventHandler(BLERead, readAnyRoomClimateData);
  
  environmentalSensingService.addCharacteristic(temperatureCharacteristic);
  environmentalSensingService.addCharacteristic(irradianceCharacteristic);
  environmentalSensingService.addCharacteristic(humidityCharacteristic);
  environmentalSensingService.addCharacteristic(nmvocCharacteristic);
  BLE.addService(environmentalSensingService);

  // BLE: Advertise services
  BLE.advertise();
}



// ############### RUNTIME CODE ###############

void loop() {
  /* 
  * PERIODIC UPDATE OF THE ROOM CLIMATE DATA:
  * Updates the current room climate data struct.
  * (This works in a pull configuration.)
  */  
  if (lastRoomClimateUpdate + UPDATE_INTERVAL_RC < millis()) {


    // to-do: update the roomclimatedata with new values from the sensors
    // some dummy operations for now:
    roomClimateData.temperature += 5;
    roomClimateData.irradiance += 5;
    roomClimateData.humidity += 5;
    roomClimateData.nmvoc += 5;


    lastRoomClimateUpdate = millis();
    if (INFO) printRoomClimateDataUpdate(roomClimateData);

    writeRoomClimateAllCharacteristics(\
      roomClimateData,\
      temperatureCharacteristic,\
      irradianceCharacteristic,\
      humidityCharacteristic,\
      nmvocCharacteristic\
    );
  }

  /* 
  * PERIODIC UPDATE OF THE TIME TRACKING:
  * Send the current work status after a given time interval.
  * (This works in a push configuration.)
  */
  if (lastTimeUpdate + UPDATE_INTERVAL_TIME < millis()) {
    writeElapsedTimeCharacteristicStructure(\
      {0, UPDATE_INTERVAL_TIME, 0, 0, (uint8_t) session.workMode, (uint8_t) 0},\
      currentElapsedTimeCharacteristic\
    );
    session.lastSessionDuration = millis() - session.startTime; // to-do: fix possible overflow error
    session.startTime = millis();
    lastTimeUpdate = millis();
    if (INFO) printSessionUpdate(session);
  }

  /*
  * MANUAL UPDATE OF THE TIME TRACKING:
  * If a button has been pressed a manual update is triggered.
  * (This works in a push configuration.)
  */
  if (pin_size_t b = whichButtonPressed()) {
    led.turnOff();
    delay(100);

    // Update the work session info so the duration and time etc since the last mode change
    writeElapsedTimeCharacteristicStructure(\
      {0, (millis()-lastTimeUpdate), 0, 0, (uint8_t) session.workMode, (uint8_t) 7},\
      currentElapsedTimeCharacteristic\
    );
    session.workMode = b;
    session.lastSessionDuration = millis() - session.startTime; // to-do: fix possible overflow error
    session.startTime = millis();
    lastTimeUpdate = millis();
    if (INFO) printSessionUpdate(session);
    
    // Update LED status and print led state
    led.setColor(findButtonColor(b));
    led.turnOn();
    if (INFO) printLEDUpdate(led);
    
    delay(BUTTON_COOLDOWN);
  }

  // Poll for changes to any of the registered characteristics.
  BLE.poll();
  delay(300);
}




