
#ifndef CLASSES_STRUCTS
#define CLASSES_STRUCTS

#include <stdint.h>

// Arduino libraries
#include <Arduino.h>

// Include custom headers
#include "parameters.h"



// ############### CLASS AND STRUCT DECLARATIONS ############### 

typedef union {
    uint64_t ui48: 48;
    uint32_t ui32;
} uint48_t;

struct color {
    unsigned red;
    unsigned green;
    unsigned blue;
};

class timedSession {
    public:
        unsigned workMode = 0;
        unsigned long startTime = millis();
        unsigned long lastSessionDuration = 0;
};

class LED {
    public:
        struct color color = {0, 0, 0};

    void turnOn() {
        analogWrite(LED_R, color.red);
        analogWrite(LED_G, color.green);
        analogWrite(LED_B, color.blue);
    }

    void turnOff() {
        setColor({0, 0, 0});
        turnOn();
    }

    void setColor(struct color color) {
        this->color.red = color.red;
        this->color.green = color.green;
        this->color.blue = color.blue;
    }
};

#endif
