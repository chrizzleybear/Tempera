
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






// ############### BLE SETUP ###############

// Setup the device information service
BLEService deviceInformationService("180A");
BLEStringCharacteristic manufacturerNameCharacteristic("2A29", BLERead, 64);
BLEStringCharacteristic serialNumberCharacteristic("2A25", BLERead, 64);

// Setup the elapsed time service for the work time tracking
struct elapsedTimeCharacteristicStructure {
  uint8_t flags = 0;
  uint48_t timeValue;
  uint8_t timeSyncSource = 0;
  uint8_t offset = 0;
  uint8_t workMode; // for our purpose the clock status bit is used as the workMode
  uint8_t clockCapabilities; // to-do: set 7 if button was pressed when already in given workMode
};
BLEService elapsedTimeService("183F");
BLECharacteristic currentElapsedTimeCharacteristic("2BF2", BLERead | BLEIndicate, sizeof(elapsedTimeCharacteristicStructure));

// Setup for the climate measurements:
/*
ESS service: 0x181A
Temperature char.: 0x2A6E
Irradiance char.: 0x2A77
Humidity char.: 0x2A6F
NMVOC char.: 0x2BD3
*/

// Functions for connection and data transfer
void blePeripheralConnectHandler(BLEDevice central);
void blePeripheralDisconnectHandler(BLEDevice central);

void readManufacturerName(BLEDevice central, BLECharacteristic characteristic);
void readElapsedTime(BLEDevice central, BLECharacteristic characteristic);
void readSerialNumber(BLEDevice central, BLECharacteristic characteristic);

void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicStructure structure);





// ############### SETUP CODE ###############

// Create objects and variables
unsigned long lastUpdate = millis();
LED led;
timedSession session;
elapsedTimeCharacteristicStructure currentElapsedTime = {0, 0, 0, 0, 1, 0};


void setup() {
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

  // BLE: Advertise services
  BLE.advertise();
}





// ############### RUNTIME CODE ###############

void loop() {
  //to-do: activate built in led when a device is connected

  // send the current work status after a given time interval
  if (lastUpdate + UPDATE_INTERVAL < millis()) {
    writeElapsedTimeCharacteristicStructure({0, UPDATE_INTERVAL, 0, 0, (uint8_t) session.workMode, (uint8_t) 0});
    session.lastSessionDuration = millis() - session.startTime; // to-do: fix possible overflow error
    session.startTime = millis();
    lastUpdate = millis();
    if (INFO) printSessionUpdate();
  }

  // If a button has been pressed change the following:
  if (pin_size_t b = whichButtonPressed()) {
    led.turnOff();
    delay(100);

    // Update the work session info so the duration and time etc since the last mode change
    writeElapsedTimeCharacteristicStructure({0, (millis()-lastUpdate), 0, 0, (uint8_t) session.workMode, (uint8_t) 7});
    session.workMode = b;
    session.lastSessionDuration = millis() - session.startTime; // to-do: fix possible overflow error
    session.startTime = millis();
    lastUpdate = millis();
    if (INFO) printSessionUpdate();
    
    // Update LED status and print
    led.setColor(findButtonColor(b));
    led.turnOn();
    if (INFO) printLEDUpdate();
    
    delay(BUTTON_COOLDOWN);
  }

  // Check for events
  BLE.poll();

  // Wait for a bit
  delay(300);
}




void blePeripheralConnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Connected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
}

void blePeripheralDisconnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Disconnected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
}

void readManufacturerName(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] Manufacturer Specifications have been read: ");
  Serial.print("Tempera > [INFO]    ");
  char buffer[64];
  characteristic.readValue(buffer, 64);
  Serial.println(buffer); 
}

void readElapsedTime(BLEDevice central, BLECharacteristic characteristic) {
  uint8_t buffer[sizeof(elapsedTimeCharacteristicStructure)];
  characteristic.readValue(buffer, sizeof(buffer));
  Serial.println("Tempera > [INFO] Elapsed time characteristic has been read: ");
  Serial.print("Tempera > [INFO]    ");
  for (uint8_t num : buffer) {
    Serial.print(num);
    Serial.print(" ");
  }
  Serial.println();
}

void readSerialNumber(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] Serial number has been read.");
  Serial.print("Tempera > [INFO]    ");
  char buffer[64];
  characteristic.readValue(buffer, 64);
  Serial.println(buffer);
}

void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicStructure structure) {
  uint8_t buffer[sizeof(structure)];
  memcpy(buffer, &structure, sizeof(structure));
  if (!currentElapsedTimeCharacteristic.writeValue(buffer, sizeof(buffer)) && ERROR) {
    Serial.println("Tempera > [ERROR] Could not write to elapsed time characteristic.");
    return;
  }
  if (INFO) {
    Serial.println("Tempera > [INFO] Elapsed time characteristic has been updated.");
    Serial.print("Tempera > [INFO]    Value: ");
    for (uint8_t num : buffer) {
    Serial.print(num);
    Serial.print(" ");
    }
    Serial.println();
  }
}
