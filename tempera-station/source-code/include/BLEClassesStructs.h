
#ifndef BLE_CLASSES_STRUCTS
#define BLE_CLASSES_STRUCTS

#include <stdint.h>

// Include custom headers
#include "parameters.h"
#include "classesStructs.h"



// ############### BLE CLASS AND STRUCT DECLARATIONS ############### 

// Data structure for the elapsed time characteristic according to its bit sequence
struct elapsedTimeCharacteristicStructure {
  uint8_t flags = 0;
  uint48_t timeValue; // to-do: check if struct has the correct size such that the byte string will be of the correct length
  uint8_t timeSyncSource = 0;
  uint8_t offset = 0;
  uint8_t workMode; // for our purpose the clock status bit is used as the workMode
  uint8_t clockCapabilities; // 0 is used for periodic updates, 7 is used for updates caused by button presses
};


// Data structures for the characteristics of the environmental sensing service
struct temperatureCharacteristicStructure {
  int16_t temperature; // in C
};

struct irradianceCharacteristicStructure {
  uint16_t irradiance; // in W/m^2, accuracy of 0.1
};

struct humidityCharacteristicStructure {
  uint16_t humidity; // in %, range 0 to 100, accuracy of 0.01
};

struct nmvocCharacteristicStructure {
  float_t nmvoc; // in kg/m^3 
}


#endif