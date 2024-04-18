
#ifndef PARAMETERS
#define PARAMETERS



// ############### PARAMETERS ###############

// Parameters are split into two groups, those that may be changed and others that should not be changed.

// ############### DO NOT CHANGE THESE IF YOU ARE NOT SURE WHAT THEY DO ###############

// Define Output Pins for LED
#define LED_R A0
#define LED_G A1
#define LED_B A2

// Define aliases for the button modes
// Order: Deep-Work, Meeting, Out-Of-Office, Present
#define DW D2 
#define MT D3
#define OO D4
#define PT D5

// Serial data rate in bits/s
#define SERIAL_DATA_RATE 9600

// Size of the BLE structures in bytes that are written to the characteristics
# define ELAPSED_TIME_CHARACTERISTICS_BYTES 11
# define ROOM_CLIMATE_STRUCTURE_BYTES 8

// ############### YOU MAY CHANGE THE PARAMETERS BELOW ###############

// Toggle additional Informations in serial output:
#define INFO 1
#define ERROR 1

// Set their colors (r-g-b value):
#define DW_COLOR {0, 0, 255}
#define MT_COLOR {255, 40, 10}
#define OO_COLOR {255, 0, 0}
#define PT_COLOR {0, 64, 0}

// Delay in ms after which a new button press will be accepted
#define BUTTON_COOLDOWN 500

// Update interval in ms after which the station transmits the current time state
#define UPDATE_INTERVAL_TIME 30000

// Update interval in ms after which the station locally updates the current ROOM CLIMATE data
#define UPDATE_INTERVAL_RC 30000

// Device name and custom id
#define DEVICE_NAME "G4T1-Tempera-Station #1"
#define DEVICE_ID "1234567890"

#endif
