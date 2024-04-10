
#ifndef BLE_CLASSES_STRUCTS
#define BLE_CLASSES_STRUCTS

#include <stdint.h>

// Include custom headers
#include "parameters.h"
#include "classesStructs.h"



// ############### BLE CLASS AND STRUCT DECLARATIONS ############### 

// Setup the elapsed time service for the work time tracking
struct elapsedTimeCharacteristicStructure {
  uint8_t flags = 0;
  uint48_t timeValue; // to-do: check if struct has the correct size such that the byte string will be of the correct length
  uint8_t timeSyncSource = 0;
  uint8_t offset = 0;
  uint8_t workMode; // for our purpose the clock status bit is used as the workMode
  uint8_t clockCapabilities; // 0 is used for periodic updates, 7 is used for updates caused by button presses
};


#endif