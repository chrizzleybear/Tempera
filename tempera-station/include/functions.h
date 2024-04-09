
#ifndef FUNCTIONS
#define FUNCTIONS

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>



// ############### FUNCTIONS DECLARATIONS ############### 

pin_size_t whichButtonPressed();
struct color findButtonColor(pin_size_t button);

void printSessionUpdate();
void printLEDUpdate();

#endif
