
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

void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicUnion structure, BLECharacteristic currentElapsedTimeCharacteristic) {
  if (!currentElapsedTimeCharacteristic.writeValue(structure.bytes, sizeof(structure.bytes)) && ERROR) {
    Serial.println("Tempera > [ERROR] Could not write to elapsed time characteristic.");
    return;
  }
  // Test if the values can be retrieved from the characteristic, overwrite the structure and print them.
  if (INFO) {
    currentElapsedTimeCharacteristic.readValue(structure.bytes, sizeof(structure.bytes));
    Serial.println("Tempera > [INFO] Elapsed time characteristic has been updated.");
    Serial.print("Tempera > [INFO]    Byte String: ");
    for (uint8_t num : structure.bytes) {
    Serial.print(num);
    Serial.print(" ");
    }
    Serial.println();
    Serial.print("Tempera > [INFO]    TimeValue: ");
    Serial.println(structure.structValues.timeValue.ui32); //to-do: adjust print method, this is not properly implement since uint48_t is shortened to uint32_t to print it
    Serial.print("Tempera > [INFO]    WorkMode: ");
    Serial.println(structure.structValues.workMode);
  }
};

void writeRoomClimateAllCharacteristics(\
  roomClimateUnionStructure roomClimateData,\
  BLECharacteristic temperatureCharacteristic,\
  BLECharacteristic irradianceCharacteristic,\
  BLECharacteristic humidityCharacteristic,\
  BLECharacteristic nmvocCharacteristic\
  ) {

  // Write values to characteristics, print an error if it fails
  if ((\
    !temperatureCharacteristic.writeValue(roomClimateData.temperatureBytes, sizeof(roomClimateData.temperatureBytes)) ||\
    !irradianceCharacteristic.writeValue(roomClimateData.irradianceBytes, sizeof(roomClimateData.irradianceBytes)) ||\
    !humidityCharacteristic.writeValue(roomClimateData.humidityBytes, sizeof(roomClimateData.humidityBytes)) ||\
    !nmvocCharacteristic.writeValue(roomClimateData.nmvocBytes, sizeof(roomClimateData.nmvocBytes))\
  ) && ERROR) {
    Serial.println("Tempera > [ERROR] Error updating room climate data.");
    return;
  }

  /*
  * Now try to retrieve the written values from the characteristics and print them:
  * All values are read as bytes which are then written to the structure. 
  * To print the values the same structure can be used since the unions allow for the conversion of the values.  
  */
  if (INFO) {
    roomClimateUnionStructure rcusTest;
    temperatureCharacteristic.readValue(rcusTest.temperatureBytes, sizeof(rcusTest.temperatureBytes));
    irradianceCharacteristic.readValue(rcusTest.irradianceBytes, sizeof(rcusTest.irradianceBytes));
    humidityCharacteristic.readValue(rcusTest.humidityBytes, sizeof(rcusTest.humidityBytes));
    nmvocCharacteristic.readValue(rcusTest.nmvocBytes, sizeof(rcusTest.nmvocBytes));
    Serial.println("Tempera > [INFO] Room climate characteristics have been updated.");
    Serial.print("Tempera > [INFO]    Byte String: ");
    Serial.print(rcusTest.temperatureBytes[0]);
    Serial.print(" ");
    Serial.print(rcusTest.temperatureBytes[1]);
    Serial.print("   ");
    Serial.print(rcusTest.irradianceBytes[0]);
    Serial.print(" ");
    Serial.print(rcusTest.irradianceBytes[1]);
    Serial.print("   ");
    Serial.print(rcusTest.humidityBytes[0]);
    Serial.print(" ");
    Serial.print(rcusTest.humidityBytes[1]);
    Serial.print("   ");
    Serial.print(rcusTest.nmvocBytes[0]);
    Serial.print(" ");
    Serial.print(rcusTest.nmvocBytes[1]);
    Serial.println();
    Serial.print("Tempera > [INFO]    Values without accuracy: ");
    Serial.print(rcusTest.temperature);
    Serial.print("*   ");
    Serial.print(rcusTest.irradiance);
    Serial.print("*   ");
    Serial.print(rcusTest.humidity);
    Serial.print("*   ");
    Serial.print(rcusTest.nmvoc);
    Serial.print("*   ");
    Serial.println();
  }
};
