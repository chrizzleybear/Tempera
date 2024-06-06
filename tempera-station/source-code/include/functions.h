
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
int fixTimeOverflow(timedSession session);
unsigned luminosityConversionFunction(double lum);

void printSessionUpdate(timedSession session);
void printLEDUpdate(LED led);
void printRoomClimateDataUpdate(roomClimateUnionStructure roomClimateData);

#endif
