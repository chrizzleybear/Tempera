
#ifndef FUNCTIONS
#define FUNCTIONS

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Include custom headers
#include "classesStructs.h"
#include "BLEClassesStructs.h"



// ############### FUNCTIONS DECLARATIONS ############### 

pin_size_t whichButtonPressed();
struct color findButtonColor(pin_size_t button);
void switchBuiltInLED(unsigned state);

void printSessionUpdate(timedSession session);
void printLEDUpdate(LED led);
void printRoomClimateDataUpdate(roomClimateStructure roomClimateData);

#endif
