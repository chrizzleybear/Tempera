
#include <Arduino.h>
#include <ArduinoBLE.h>

// Libraries for the BME sensor module
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"





// ############### MAKROS ############### 

// Toggle additional Informations in serial output:
#define INFO 1

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
#define B_MT_COLOR {255, 40, 10}
#define B_OO_COLOR {255, 0, 0}
#define B_AW_COLOR {0, 64, 0}

// Serial data rate in bits/s
#define SERIAL_DATA_RATE 9600

// Delay after which a new button press will be accepted
#define BUTTON_COOLDOWN 500





// ############### FUNCTIONS DECLARATIONS ############### 

pin_size_t whichButtonPressed();
struct color findButtonColor(pin_size_t button);
void printSessionUpdate();
void printLEDUpdate();





// ############### CLASS AND STRUCT DECLARATIONS ############### 
// Attributes are public to skip Getter and Setter.

struct color {
  int red;
  int green;
  int blue;
};

class timedSession {
  public:
    int active = 0;
    int workMode = 0;
    unsigned long startTime = millis();
    unsigned long lastSessionDuration = 0;

    int toggleActive () {
      this->active = !this->active;
      return this->active;
    }
};

class LED {
  public:
    int ledStatus = 0;
    struct color color = {0, 0, 0};

    void toggleLED() {
      this->ledStatus = !this->ledStatus;
      this->ledStatus ? turnOn() : turnOff();
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

    void setColor(struct color color) {
      this->color.red = color.red;
      this->color.green = color.green;
      this->color.blue = color.blue;
      return;
    }
};





// ############### BLE SETUP ###############










// ############### SETUP CODE ###############

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

  Serial.println("Tempera > [INFO] System started...");
}

// Create objects
LED led;
timedSession session;





// ############### RUNTIME CODE ###############

void loop() {

  // If a button has been pressed change the following:
  if (pin_size_t b = whichButtonPressed()) {

    // Update the work session info so the duration and time etc since the last mode change
    if (session.toggleActive()) {
      session.workMode = b;
      session.startTime = millis();
      session.lastSessionDuration = 0;
    } else {
      session.workMode = 0;
      session.lastSessionDuration = millis() - session.startTime;
      
      // check for overflow, if yes correct the time
      // an overflow will happen after leaving the device on for than approximately 50 consecutive days
      if (session.lastSessionDuration < 0) {
        
      }
      
      session.startTime = 0;
    }
    if (INFO) printSessionUpdate();
    
    // Update LED status and print
    led.setColor(findButtonColor(b));
    led.toggleLED();
    if (INFO) printLEDUpdate();
    
    delay(BUTTON_COOLDOWN);
  }

  // Wait for a bit
  delay(300);
}





// ############### FUNCTIONS ###############

// Checks if one of the four buttons is currently pressed and returns the pressed one or 0 
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

// Returns the corresponding color for each button
struct color findButtonColor(pin_size_t button) {
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

void printSessionUpdate() {
  Serial.println("Tempera > [INFO] Session Info has been updated:");
  Serial.print("Tempera > [INFO]    Active: ");
  Serial.println(session.active);
  Serial.print("Tempera > [INFO]    Current work mode: ");
  Serial.println(session.workMode);
  Serial.print("Tempera > [INFO]    Start time: ");
  Serial.println(session.startTime);
  Serial.print("Tempera > [INFO]    Last session duration: ");
  Serial.println(session.lastSessionDuration);
  return;
}

void printLEDUpdate() {
  Serial.println("Tempera > [INFO] LED state has been updated:");
  Serial.print("Tempera > [INFO]    Active: ");
  Serial.println(led.ledStatus);
  Serial.print("Tempera > [INFO]    Color (R-G-B): ");
  Serial.print(led.color.red);
  Serial.print(" ");
  Serial.print(led.color.green);
  Serial.print(" ");
  Serial.println(led.color.blue);
  return;
}
