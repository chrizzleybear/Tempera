
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

/**
 * @brief Handler for BLE peripheral connection. 
 *        Turns on the built-in LED if a device is connected.
 * @param central The BLE central device that has connected.
 */
void blePeripheralConnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Connected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
  switchBuiltInLED(1);
};

/**
 * @brief Handler for BLE peripheral disconnection.
 *        Turns off the built-in LED if no device is connected.
 * @param central The BLE central device that has disconnected.
 */
void blePeripheralDisconnectHandler(BLEDevice central) {
  Serial.println("Tempera > [INFO] Disconnected event, central: ");
  Serial.print("Tempera > [INFO]    ");
  Serial.println(central.address());
  switchBuiltInLED(0);
};

/**
 * @brief Event handler for reading manufacturer name.
 * @param central The BLE central device that initiated the read.
 * @param characteristic The BLE characteristic being read.
 */
void readManufacturerName(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] Manufacturer Specifications have been read: ");
  Serial.print("Tempera > [INFO]    ");
  char buffer[64];
  characteristic.readValue(buffer, 64);
  Serial.println(buffer); 
};

/**
 * @brief Event handler for reading the elapsed time characteristic.
 * @param central The BLE central device that initiated the read.
 * @param characteristic The BLE characteristic being read.
 */
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

/**
 * @brief Event handler for reading the serial number which is also used as the unique ID of the device.
 * @param central The BLE central device that initiated the read.
 * @param characteristic The BLE characteristic being read.
 */
void readSerialNumber(BLEDevice central, BLECharacteristic characteristic) {
  Serial.println("Tempera > [INFO] Serial number has been read.");
  Serial.print("Tempera > [INFO]    ");
  char buffer[64];
  characteristic.readValue(buffer, 64);
  Serial.println(buffer);
};

/**
 * @brief Event handler which is triggered if any room climate data characteristic has been read.
 * @param central The BLE central device that initiated the read.
 * @param characteristic The BLE characteristic being read.
 */
void readAnyRoomClimateData(BLEDevice central, BLECharacteristic characteristic) {
    universalRCValueStructure buffer;
    characteristic.readValue(buffer.valueBytes, sizeof(buffer.valueBytes));
    String characteristicUUID = characteristic.uuid();
    Serial.println("Tempera > [INFO] Room climate data has been read.");
    Serial.print("Tempera > [INFO]    Type: ");
    Serial.println(characteristicUUID);
    Serial.print("Tempera > [INFO]    Value: ");
    Serial.println(buffer.value);
};

/**
 * @brief Writes the elapsed time characteristic structure to the elapsed time BLE characteristic.
 * @param etcu The elapsed time characteristic union structure to be written.
 * @param currentElapsedTimeCharacteristic The BLE characteristic to write the structure to.
 */
void writeElapsedTimeCharacteristicStructure(elapsedTimeCharacteristicUnion etcu, BLECharacteristic currentElapsedTimeCharacteristic) {
  if (!currentElapsedTimeCharacteristic.writeValue(etcu.bytes, sizeof(etcu.bytes)) && ERROR) {
    Serial.println("Tempera > [ERROR] Could not write to elapsed time characteristic.");
    return;
  }
  // Test if the values can be retrieved from the characteristic, overwrite the structure and print them.
  if (INFO) {
    currentElapsedTimeCharacteristic.readValue(etcu.bytes, sizeof(etcu.bytes));
    Serial.println("Tempera > [INFO] Elapsed time characteristic has been updated.");
    Serial.print("Tempera > [INFO]    Byte String: ");
    for (uint8_t num : etcu.bytes) {
      Serial.print(num);
      Serial.print(" ");
    }
    Serial.println();
    Serial.print("Tempera > [INFO]    TimeValue: ");
    Serial.println(etcu.structValues.timeValue.ui48);
    Serial.print("Tempera > [INFO]    WorkMode: ");
    Serial.println(etcu.structValues.workMode);
  }
};

/**
 * @brief Writes room climate data to all corresponding BLE characteristics.
 * @param roomClimateData The room climate data structure containing the data to be written.
 * @param temperatureCharacteristic The BLE characteristic for temperature.
 * @param irradianceCharacteristic The BLE characteristic for irradiance.
 * @param humidityCharacteristic The BLE characteristic for humidity.
 * @param nmvocCharacteristic The BLE characteristic for gas resistance.
 */
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

    Serial.print("Tempera > [INFO]    Raw Values: ");
    Serial.print(rcusTest.temperature);
    Serial.print("   ");
    Serial.print(rcusTest.irradiance);
    Serial.print("   ");
    Serial.print(rcusTest.humidity);
    Serial.print("   ");
    Serial.print(rcusTest.nmvoc);
    Serial.print("   ");
    Serial.println();
  }
};
