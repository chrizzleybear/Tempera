
#ifndef BLE_FUNCTIONS
#define BLE_FUNCTIONS

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Include custom headers
#include "BLEClassesStructs.h"



// ############### BLE FUNCTIONS DECLARATIONS ############### 

void blePeripheralConnectHandler(BLEDevice central);
void blePeripheralDisconnectHandler(BLEDevice central);

void readManufacturerName(BLEDevice central, BLECharacteristic characteristic);
void readElapsedTime(BLEDevice central, BLECharacteristic characteristic);
void readSerialNumber(BLEDevice central, BLECharacteristic characteristic);

void writeElapsedTimeCharacteristicStructure(BLECharacteristic characteristic, elapsedTimeCharacteristicStructure structure);


#endif