
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
roomClimateUnionStructure roomClimateData = {0, 0, 0, 0}; // explicit initialization simplifies sizeof operations
BLECharacteristic temperatureCharacteristic("2A6E", BLERead, sizeof(roomClimateData.temperature));
BLECharacteristic irradianceCharacteristic("2A77", BLERead, sizeof(roomClimateData.irradiance));
BLECharacteristic humidityCharacteristic("2A6F", BLERead, sizeof(roomClimateData.humidity)); 
BLECharacteristic nmvocCharacteristic("2BD3", BLERead, sizeof(roomClimateData.nmvoc));



// ############### SETUP CODE ###############

// Create objects and set variables
unsigned long lastTimeUpdate = millis();
unsigned long lastRoomClimateUpdate = millis();
LED led;
timedSession session;
elapsedTimeCharacteristicUnion currentElapsedTime = {0, 0, 0, 0, (uint8_t) 1, (uint8_t) 7};
Adafruit_BME680 bme; // get an I2C-Instance


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

  // BME688: Set up oversampling and filter initialization
  if (!bme.begin() && ERROR) {
    Serial.println("Tempera > [ERROR] Could not find BME688-board.");
  } else {
    bme.setTemperatureOversampling(BME680_OS_8X);
    bme.setHumidityOversampling(BME680_OS_2X);
    bme.setPressureOversampling(BME680_OS_4X);
    bme.setIIRFilterSize(BME680_FILTER_SIZE_3);
    bme.setGasHeater(320, 150); // 320*C for 150 ms
    if (INFO) Serial.println("Tempera > [INFO] BME688-Board initialized.");
  }
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
  serialNumberCharacteristic.writeValue(DEVICE_SN);
  serialNumberCharacteristic.setEventHandler(BLERead, readSerialNumber);
  deviceInformationService.addCharacteristic(serialNumberCharacteristic);

  BLE.addService(deviceInformationService);

  // BLE: Setup for elapsed time characteristic
  BLEDescriptor elapsedTimeDescriptor("2901", "Elapsed Time service used for time tracking, time in ms.");
  currentElapsedTimeCharacteristic.addDescriptor(elapsedTimeDescriptor);
  currentElapsedTimeCharacteristic.setEventHandler(BLERead, readElapsedTime);
  elapsedTimeService.addCharacteristic(currentElapsedTimeCharacteristic);
  BLE.addService(elapsedTimeService);

  // BLE: Setup for room climate data
  BLECharacteristic characteristics[] = {temperatureCharacteristic, irradianceCharacteristic, humidityCharacteristic, nmvocCharacteristic};
  BLEDescriptor descriptors[] = {
    BLEDescriptor("2901", "Last measured temperature in deg. C, accuracy of 0.01."),
    BLEDescriptor("2901", "Last measured irradiance in W/m^2, accuracy of 0.1"),
    BLEDescriptor("2901", "Last measured relative humidity in %, accuracy of 0.01"),
    BLEDescriptor("2901", "Last measured non-methane-volatile-organic-compound-concentration in 10^(-2)*Ohm, accuracy of 1")
  };
  String names[] = {"Temperature", "Irradiance", "Humidity", "NMVOC"};
  for (int i = 0; i < 4; i++) {
    characteristics[i].setEventHandler(BLERead, readAnyRoomClimateData);
    characteristics[i].addDescriptor(descriptors[i]);
    environmentalSensingService.addCharacteristic(characteristics[i]);
  }

  BLE.addService(environmentalSensingService);

  // BLE: Advertise services
  BLE.advertise();
}



// ############### RUNTIME CODE ###############

void loop() {

  /* 
  * PERIODIC UPDATE OF THE ROOM CLIMATE DATA:
  * Updates the current room climate data struct.
  * (The transmission via BLE works in a pull configuration.)
  */  
  if (lastRoomClimateUpdate + UPDATE_INTERVAL_RC < millis()) {
    bme.setGasHeater(0, 0); // turn off the gas heater to not manipulate other measurements
    unsigned long endTime = bme.beginReading();
    if (endTime == 0 && ERROR) {
      Serial.println("Tempera > [ERROR] Could not start room climate measurements.");
    } else if (INFO) {
      Serial.println("Tempera > [INFO] Started room climate measurement: ");
      Serial.print("Tempera > [INFO]    Start time: ");
      Serial.println(millis());
      Serial.print("Tempera > [INFO]    Finish time: ");
      Serial.println(endTime);
    }

    // Wait briefly for the measurement to be completed.
    delay(200);

    if (!bme.endReading() && ERROR) {
      Serial.println("Tempera > [ERROR] Could not complete room climate measurements.");
      Serial.println("Tempera > [ERROR]    Previously set values will be used.");
    } else {
      // Write temperature and humidity measurements with respective accuracies to roomClimateStructure
      roomClimateData.temperature = bme.temperature / 0.01 * TEMP_CALIBRATION_FACTOR;
      roomClimateData.humidity = bme.humidity / 0.01;
      // start a new measurement to only measure NMVOC
      bme.setGasHeater(320, 150); // 320*C for 150 ms
      endTime = bme.beginReading();
      if (endTime == 0 && ERROR) {
        Serial.println("Tempera > [ERROR] Could not perform air quality measurement.");
      }
      delay(200);
      if (!bme.endReading() && ERROR) {
        Serial.println("Tempera > [ERROR] Could not complete air quality measurement.");
      }
      // now write only the NMVOC to roomClimateData since the other measurements are skewed due to the heating
      roomClimateData.nmvoc = bme.gas_resistance /  100.0;
      roomClimateData.irradiance = analogRead(PT_PIN) / 0.01; // to-do: measure over longer time spans, use exponential smoothing, possibly moving average
    }

    // Confirm set values
    if (INFO) printRoomClimateDataUpdate(roomClimateData);
    lastRoomClimateUpdate = millis();

    // Write to characteristics
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
  * (The transmission via BLE works in a push configuration.)
  */
  if (lastTimeUpdate + UPDATE_INTERVAL_TIME < millis()) {
    writeElapsedTimeCharacteristicStructure(\
      {0, (millis() - session.startTime), 0, 0, session.workMode, (uint8_t) 0},\
      currentElapsedTimeCharacteristic\
    );
    session.lastSessionDuration = millis() - session.startTime;
    // Overflow fix for internal clock
    if (millis() < session.startTime) {
      session.lastSessionDuration = fixTimeOverflow(session);
    }
    session.startTime = millis();
    lastTimeUpdate = millis();
    if (INFO) printSessionUpdate(session);
  }

  /*
  * MANUAL UPDATE OF THE TIME TRACKING:
  * If a button has been pressed a manual update is triggered.
  * (The transmission via BLE works in a push configuration.)
  */
  if (pin_size_t b = whichButtonPressed()) {
    led.turnOff();
    delay(100);

    // Update the work session info so the duration and time etc since the last mode change
    writeElapsedTimeCharacteristicStructure(\
      {0, (millis()-lastTimeUpdate), 0, 0, session.workMode, (uint8_t) b},\
      currentElapsedTimeCharacteristic\
    );
    session.workMode = b;
    session.lastSessionDuration = millis() - session.startTime;
    // Overflow fix for internal clock
    if (millis() < session.startTime) {
      session.lastSessionDuration = fixTimeOverflow(session);
    }
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




