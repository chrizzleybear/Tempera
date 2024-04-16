
#include "../include/BLEfunctions.h"

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Include further headers
#include "../include/parameters.h"
#include "../include/BLEClassesStructs.h"



// ############### BLE FUNCTIONS ###############

void blePeripheralConnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Connected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
};

void blePeripheralDisconnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Disconnected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
};

void readManufacturerName(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] Manufacturer Specifications have been read: ");
  Serial.print("Tempera > [INFO]    ");
  char buffer[64];
  characteristic.readValue(buffer, 64);
  Serial.println(buffer); 
};

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
};

void readSerialNumber(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] Serial number has been read.");
  Serial.print("Tempera > [INFO]    ");
  char buffer[64];
  characteristic.readValue(buffer, 64);
  Serial.println(buffer);
};

void readAnyRoomClimateData(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] A room climate data field has been read.");
  Serial.print("Tempera > [INFO]    (Value not specified)");   // to-do: maybe specify which one has been read
};

void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicStructure structure, BLECharacteristic currentElapsedTimeCharacteristic) {
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
};

void writeRoomClimateAllCharacteristics(\
      roomClimateStructure roomClimateData,\
      BLECharacteristic temperatureCharacteristic,\
      BLECharacteristic irradianceCharacteristic,\
      BLECharacteristic humidityCharacteristic,\
      BLECharacteristic nmvocCharacteristic\
  ) {
      return;
};
