
#include "../include/functions.h"

// Standard libraries
#include <stdint.h>

// Arduino libraries
#include <Arduino.h>
#include <ArduinoBLE.h>

// Include further headers
#include "../include/classesStructs.h"
#include "../include/parameters.h"



// ############### FUNCTIONS ###############

/**
 * @brief Checks if one of the four buttons is currently pressed and returns the pressed one or 0.
 * @return The pin number of the pressed button (DW, MT, OO, PT) or 0 if no button is pressed.
 */
pin_size_t whichButtonPressed() {
  if (!digitalRead(DW)) {
    return DW;
  } else if (!digitalRead(MT)) {
    return MT;
  } else if (!digitalRead(OO)) {
    return OO;
  } else if (!digitalRead(PT)) {
    return PT;
  } else {
    return 0;
  }
};

/**
 * @brief Finds the color mapped to the specified button.
 * @param button The pin number / work mode of the button for which to find the color.
 * @return The color associated with the specified button.
 */
struct color findButtonColor(pin_size_t button) {
  switch (button) {
  case PT: 
    return PT_COLOR;
    break;
  case DW:
    return DW_COLOR;
    break;
  case MT:
    return MT_COLOR;
    break;
  case OO:
    return OO_COLOR;
    break;
  default:
    return {0, 0, 0};
    break;
  }
};

/**
 * @brief Fixes a time overflow of the internal clock which may occur if the 
 *        Arduino is not rebooted for more than ~50 days.
 * @param session The timed session structure containing the start time.
 * @return The remaining time until the unsigned long overflow in milliseconds.
 */
int fixTimeOverflow(timedSession session) {
  // ULONG_MAX
  return 4294967295 - session.startTime + millis();
};

/**
 * @brief Switches the built-in LED on or off.
 * @param state The state of the LED (LOW for off, HIGH for on).
 */
void switchBuiltInLED(unsigned state) {
    digitalWrite(LED_BUILTIN, (state ? HIGH : LOW) );
};

/**
 * @brief Prints the information of a timed session to the serial output.
 * @param session The timed session structure containing the updated information.
 */
void printSessionUpdate(timedSession session) {
  Serial.println("Tempera > [INFO] Session Info has been updated:");
  Serial.print("Tempera > [INFO]    Current work mode: ");
  Serial.println(session.workMode);
  Serial.print("Tempera > [INFO]    Start time: ");
  Serial.println(session.startTime);
  Serial.print("Tempera > [INFO]    Last session duration: ");
  Serial.println(session.lastSessionDuration);
};

/**
 * @brief Prints the updated color of the main RGB-LED.
 * @param led The LED object containing the updated state.
 */
void printLEDUpdate(LED led) {
  Serial.println("Tempera > [INFO] LED state has been updated:");
  Serial.print("Tempera > [INFO]    Color (R-G-B): ");
  Serial.print(led.color.red);
  Serial.print(" ");
  Serial.print(led.color.green);
  Serial.print(" ");
  Serial.println(led.color.blue);
};

/**
 * @brief Prints the updated room climate data.
 * @param roomClimateData The room climate union structure containing the updated data.
 */
void printRoomClimateDataUpdate(roomClimateUnionStructure roomClimateData) {
  Serial.println("Tempera > [INFO] Room climate data has been updated:");
  Serial.print("Tempera > [INFO]    Temperature: ");
  Serial.println(roomClimateData.temperature);
  Serial.print("Tempera > [INFO]    Irradiance: ");
  Serial.println(roomClimateData.irradiance);
  Serial.print("Tempera > [INFO]    Humidity: ");
  Serial.println(roomClimateData.humidity);
  Serial.print("Tempera > [INFO]    Gas resistance: ");
  Serial.println(roomClimateData.nmvoc);
};

/**
 * @brief Conversion function which approximately converts the measured voltage to lux
 * @param lum Luminsoity, measured from the analog readout pin, that should be converted.
 */
int convertAnalogReadoutToLux(double lum) {
  int res = 0;
  
  return res;
}
3.5 100
1.7 40
4.5 200
9 400
22 800
40 2000 