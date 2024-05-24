
#ifndef CLASSES_STRUCTS
#define CLASSES_STRUCTS

#include <stdint.h>

// Arduino libraries
#include <Arduino.h>

// Include custom headers
#include "parameters.h"



// ############### CLASS AND STRUCT DECLARATIONS ############### 

/**
 * @brief Union to convert a 64-bit uint to a 48-bit uint.
 * 
 * The `__packed__` attribute is used to disable padding to maintain the correct size.
 */
typedef union __attribute__( (__packed__) ) {
    uint64_t ui48: 48;
} uint48_t;

/**
 * @brief Structure representing a color for the LED.
 */
struct color {
    unsigned red;
    unsigned green;
    unsigned blue;
};

/**
 * @brief Class representing a timed session.
 * 
 * The class represents a timed session with attributes for work mode, start time, and last session duration.
 */
class timedSession {
    public:
        uint8_t workMode = 0;
        unsigned long startTime = millis();
        unsigned long lastSessionDuration = 0;
};

/**
 * @brief Class representing the RGB-LED.
 * 
 * The class represents the RGB-LED with attributes for color and some methods to control it.
 */
class LED {
    public:
        struct color color = {0, 0, 0};

    /**
     * @brief Turns on the LED with the current color.
     */
    void turnOn() {
        analogWrite(LED_R, color.red);
        analogWrite(LED_G, color.green);
        analogWrite(LED_B, color.blue);
    }

    /**
     * @brief Turns off the LED.
     */
    void turnOff() {
        setColor({0, 0, 0});
        turnOn();
    }

    /**
     * @brief Sets the color of the LED.
     * 
     * @param color The color to set.
     */
    void setColor(struct color color) {
        this->color.red = color.red;
        this->color.green = color.green;
        this->color.blue = color.blue;
    }
};

#endif
