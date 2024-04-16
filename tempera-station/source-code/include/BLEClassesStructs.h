
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
  uint48_t timeValue;           // to-do: check if struct has the correct size such that the byte string will be of the correct length
  uint8_t timeSyncSource = 0;
  uint8_t offset = 0;
  uint8_t workMode;             // for our purpose the clock status bit is used as the workMode
  uint8_t clockCapabilities;    // 0 is used for periodic updates, 7 is used for updates caused by button presses
};

/* 
* Data structure for the characteristics of the environmental sensing service.
*
* This characteristic uses a medfloat16 value (see ISO/IEEE 11073-20601) according to the specifications. 
* For our purpose we simplify this by using a scaled uint16_t value.
* To scale the value, we change the units from kg/m^3 to g/m^3.
* The accuracy translates to a multiplier that has to be used when writing a value to a characteristic.
* So if we want to transmit a temperature of 4 °C we need to write a value of 4/0.01 = 400 to the characteristic. 
*/
struct roomClimateStructure {
  int16_t temperature; // in C, accuracy of 0.01
  uint16_t irradiance; // in W/m^2, accuracy of 0.1
  uint16_t humidity;   // in %, range 0 to 100, accuracy of 0.01
  uint16_t nmvoc;      // in g/m^3
};



#endif