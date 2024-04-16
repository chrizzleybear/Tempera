
#include "../include/BLEfunctions.h"

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Include further headers
#include "../include/parameters.h"
#include "../include/BLEClassesStructs.h"
#include "../include/functions.h"



// ############### BLE FUNCTIONS ###############

void blePeripheralConnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Connected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
  switchBuiltInLED(1);
};

void blePeripheralDisconnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Disconnected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
  switchBuiltInLED(0);
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
  Serial.print("Tempera > [INFO]    Value: ");
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
  Serial.println("Tempera > [INFO] A room climate characteristic has been read.");
  Serial.println("Tempera > [INFO]    (Value not specified)");   // to-do: specify which one has been read
};

void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicStructure structure, BLECharacteristic currentElapsedTimeCharacteristic) {
  uint8_t buffer[sizeof(structure)]; // to-do: there might be an error here with writing the data, written values seem really weird...?
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
  // explicit writing is required for the nmvoc value because we write an uint16_t instead of a medfloat16 
  uint8_t uBuffer[sizeof(roomClimateData.nmvoc)];
  memcpy(uBuffer, &roomClimateData.nmvoc, sizeof(roomClimateData.nmvoc));
  if ((\
    !temperatureCharacteristic.writeValue(roomClimateData.temperature) ||\
    !irradianceCharacteristic.writeValue(roomClimateData.irradiance) ||\
    !humidityCharacteristic.writeValue(roomClimateData.humidity) ||\
    !nmvocCharacteristic.writeValue(uBuffer, sizeof(uBuffer))\
  ) && ERROR) {
    Serial.println("Tempera > [ERROR] Error updating room climate data.");
    return;
  }
  unsigned byteCount = 4;
  uint8_t buffer[4*byteCount];
  if (INFO) {    
    Serial.println("Tempera > [INFO] Room climate characteristics have been updated.");
    Serial.print("Tempera > [INFO]    Written values: "); 
    /* to-do: the values are not retrieved correctly
    memcpy(buffer, temperatureCharacteristic.value(), byteCount);
    memcpy(buffer+byteCount, irradianceCharacteristic.value(), byteCount);
    memcpy(buffer+byteCount, humidityCharacteristic.value(), byteCount);
    memcpy(buffer+byteCount, nmvocCharacteristic.value(), byteCount);
    int cnt = 0;
    for (uint8_t num : buffer) {
      cnt++;
      Serial.print(num);
      Serial.print((cnt % 4 == 0) ? "   " : " ");
    }
    */
    Serial.println();
  }
};
