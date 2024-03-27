
// LIBRARIES
#include <Arduino.h>
#include <ArduinoBLE.h>

// Libraries for the BME sensor module
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"





// DEFINITIONS

// Define Output Pins for LED
#define LED_R A0
#define LED_G A1
#define LED_B A2

// Define aliases for the button modes
// Order: Deep-Work, Meeting, Out-Of-Office, Anwesend
#define B_DW D2 
#define B_MT D3
#define B_OO D4
#define B_AW D5
// Set their colors (rgb value):
#define B_DW_COLOR {0, 0, 255}
#define B_MT_COLOR {255, 32, 0}
#define B_OO_COLOR {255, 0, 0}
#define B_AW_COLOR {0, 64, 0}

// Serial data rate in bits/s
#define SERIAL_DATA_RATE 9600

// Delay time after each iteration
#define DELAY 500





// FUNCTIONS AND STRUCT DECLARATIONS

pin_size_t whichButtonPressed();
struct color getButtonColor(pin_size_t button);

struct color {
  int red;
  int green;
  int blue;
};





// CLASS DECLARATIONS
class LED {
  int ledStatus = 0;
  struct color color = {0, 0, 0};

  public:

  void toggleLED() {
    this->ledStatus = !this->ledStatus;
    this->ledStatus ? turnOn() : turnOff();
    return;
  }

  void setColor(struct color color) {
    this->color.red = color.red;
    this->color.green = color.green;
    this->color.blue = color.blue;
    return;
  }

  void turnOn() {
    this->ledStatus = 1;
    analogWrite(LED_R, color.red);
    analogWrite(LED_G, color.green);
    analogWrite(LED_B, color.blue);
    return;
  }

  void turnOff() {
    setColor({0, 0, 0});
    turnOn();
    this->ledStatus = 0;
    return;
  }

  int getStatus() {
    return this->ledStatus;
  }
};





// SETUP CODE
void setup() {
  // Setup for the rgb-led pins
  pinMode(LED_R, OUTPUT);
  pinMode(LED_G, OUTPUT);
  pinMode(LED_B, OUTPUT);

  // Setup for the button pins
  pinMode(B_DW, INPUT_PULLUP);
  pinMode(B_MT, INPUT_PULLUP);
  pinMode(B_OO, INPUT_PULLUP);
  pinMode(B_AW, INPUT_PULLUP);

  // Set serial output data rate in bits/s
  Serial.begin(SERIAL_DATA_RATE);

}

// Get the LED Object
LED led;





// RUNTIME CODE
void loop() {

  if (pin_size_t b = whichButtonPressed()) {
    led.setColor(getButtonColor(b));
    led.toggleLED();
  }

  // Wait for a bit
  delay(DELAY);
}





// FUNCTION 

pin_size_t whichButtonPressed() {
  if (!digitalRead(B_DW)) {
    return B_DW;
  } else if (!digitalRead(B_MT)) {
    return B_MT;
  } else if (!digitalRead(B_OO)) {
    return B_OO;
  } else if (!digitalRead(B_AW)) {
    return B_AW;
  } else {
    return 0;
  }
}

struct color getButtonColor(pin_size_t button) {
  switch (button) {
  case B_AW: 
    return B_AW_COLOR;
    break;
  case B_DW:
    return B_DW_COLOR;
    break;
  case B_MT:
    return B_MT_COLOR;
    break;
  case B_OO:
    return B_OO_COLOR;
    break;
  default:
    return {0, 0, 0};
    break;
  }
}

