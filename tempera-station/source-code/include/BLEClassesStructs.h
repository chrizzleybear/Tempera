
#ifndef BLE_CLASSES_STRUCTS
#define BLE_CLASSES_STRUCTS

#include <stdint.h>

// Include custom headers
#include "parameters.h"
#include "classesStructs.h"



// ############### BLE CLASS AND STRUCT DECLARATIONS ############### 

// Data structure for the elapsed time characteristic in the correct byte sequence.
typedef struct __attribute__( (packed) ) { // use the attribute packed to correctly align data bytes in memory, this is necessary for the conversion to bytes 
  uint8_t flags = 0;
  uint48_t timeValue; // ! to-do: check if value is written correctly, it seems to not quite work
  uint8_t timeSyncSource = 0;
  uint8_t offset = 0;
  uint8_t workMode;             // for our purpose the clock status bit is used as the workMode, 2-5 are DW, MT, OO, PT
  uint8_t manualUpdate;    // 0 is used for periodic updates, 7 is used for updates caused by button presses
} elapsedTimeCharacteristicStructure;

// Union used to write and retrieve data from the characteristic
typedef union {
  elapsedTimeCharacteristicStructure structValues;
  uint8_t bytes[ELAPSED_TIME_CHARACTERISTICS_BYTES];
} elapsedTimeCharacteristicUnion;

/* 
* Data structure for the characteristics of the environmental sensing service. 
* The unions are used to write to the respective characteristics.
*
* The accuracy translates to a multiplier that has to be used when writing a value to a characteristic.
* So if we want to transmit a temperature of 4 °C we need to write a value of 4/0.01 = 400 to the characteristic. 
* The NMVOC characteristic uses a medfloat16 value (see ISO/IEEE 11073-20601) according to the specifications. 
* For our purpose we simplify this by using a scaled uint16_t value.
* To scale the value, we change the units from kg/m^3 to g/m^3.
*/
typedef struct __attribute__( (packed) ) { // use the attribute packed to correctly align data bytes in memory
  union {
    int16_t temperature; // in C, accuracy of 0.01
    uint8_t temperatureBytes[ROOM_CLIMATE_STRUCTURE_BYTES/4];
  };
  union {
    uint16_t irradiance; // in W/m^2, accuracy of 0.1
    uint8_t irradianceBytes[ROOM_CLIMATE_STRUCTURE_BYTES/4];
  };
  union {
    uint16_t humidity;   // in %, range 0 to 100, accuracy of 0.01
    uint8_t humidityBytes[ROOM_CLIMATE_STRUCTURE_BYTES/4];
  };
  union {
    uint16_t nmvoc;      // in g/m^3
    uint8_t nmvocBytes[ROOM_CLIMATE_STRUCTURE_BYTES/4];
  };
} roomClimateUnionStructure;



#endif