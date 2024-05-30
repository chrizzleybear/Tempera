
#ifndef BLE_CLASSES_STRUCTS
#define BLE_CLASSES_STRUCTS

#include <stdint.h>

// Include custom headers
#include "parameters.h"
#include "classesStructs.h"



// ############### BLE CLASS AND STRUCT DECLARATIONS ############### 


/**
 * @brief Data structure for the elapsed time characteristic with correct byte sequence.
 * 
 * This structure represents the elapsed time characteristic with the correct byte sequence.
 * The `__packed__` attribute is used to ensure correct alignment of data bytes in memory, 
 * necessary when writing to characteristics.
 * 
 * The structure includes:
 * - flags: (not used)
 * - timeValue: The elapsed time in ms.
 * - timeSyncSource: (not used)
 * - offset: (not used)
 * - workMode: (default: clock status) 
 *             Work mode represented by clock status bit (2-5 are DW, MT, OO, PT).
 * - manualUpdate: (default: clock capabilities) 
 *             Indicator for updates (0 for periodic updates, work mode 2-5 for updates by button presses).
 */
typedef struct __attribute__( (__packed__) ) {
  uint8_t flags = 0;
  uint48_t timeValue;
  uint8_t timeSyncSource = 0;
  uint8_t offset = 0;
  uint8_t workMode;
  uint8_t manualUpdate;
} elapsedTimeCharacteristicStructure;

/**
 * @brief Union used to write and retrieve data from the elapsed time characteristic.
 * The union includes:
 * - structValues: Structure representing the characteristics of the elapsed time.
 * - bytes: Structure in the form of an array of bytes .
 */
typedef union {
  elapsedTimeCharacteristicStructure structValues;
  uint8_t bytes[ELAPSED_TIME_CHARACTERISTICS_BYTES];
} elapsedTimeCharacteristicUnion;


/**
 * @brief Data structure for the characteristics of the environmental sensing service.
 * 
 * This structure represents the characteristics of the environmental sensing service.
 * Unions are used to write and retrieve data from the characteristics.
 * The `__packed__` attribute is used to ensure correct alignment of data bytes in memory, 
 * necessary when writing to characteristics.
 * 
 * The accuracy translates to a multiplier that has to be used when writing a value to a characteristic.
 * So if we want to transmit a temperature of 4 °C we need to write a value of 4/0.01 = 400 to the characteristic.
 * 
 * The NMVOC characteristic uses a medfloat16 value (see ISO/IEEE 11073-20601) according to the specifications.
 * For our purpose we simplify this by using a scaled uint16_t value.
 */
typedef struct __attribute__( (__packed__) ) {
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
    uint16_t nmvoc;      // in 10^(-2) * Ohm, accuracy of 1
    uint8_t nmvocBytes[ROOM_CLIMATE_STRUCTURE_BYTES/4];
  };
} roomClimateUnionStructure;

/**
 * @brief Universal structure to print any room climate data field.
 */
typedef struct __attribute__( (__packed__) ) {
  union {
    uint16_t value;
    uint8_t valueBytes[2];
  };
} universalRCValueStructure;


#endif