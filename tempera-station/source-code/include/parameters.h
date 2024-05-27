
#ifndef PARAMETERS
#define PARAMETERS



// ############### PARAMETERS ###############

// Parameters are split into two groups, those that should not be changed and others which may be changed.

// ############### DO NOT CHANGE THESE IF YOU ARE NOT SURE WHAT THEY DO ###############

// Define Output Pins for LED
#define LED_R A0
#define LED_G A1
#define LED_B A2

// Define phototransistor measurement pin
#define PT_PIN A7

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

// Parameters for the Adafruit BME688 sensor board
#define BME_SCK 13
#define BME_MISO 12
#define BME_MOSI 11
#define BME_CS 10
#define SEALEVELPRESSURE_HPA (1013.25)

// Luminosity smoothing factor used for exponential smoothing and conversion function
#define LUMINOSITY_SMOOTHING_FACTOR 0.2
#define LUMINOSITY_CONVERSION_FUNCTION(lum) (43*lum+4)

// TO-DO: Temperature calibration factor (temporary solution to heavily biased measurements)
#define TEMP_CALIBRATION_FACTOR (21.0/26.0)

// ############### YOU MAY CHANGE THE PARAMETERS BELOW ###############

// Toggle additional Informations in serial output:
#define INFO 1
#define ERROR 1

// Set LED-color for different work modes (r-g-b value):
#define DW_COLOR {0, 0, 255}
#define MT_COLOR {255, 40, 10}
#define OO_COLOR {255, 0, 0}
#define PT_COLOR {0, 64, 0}

// Delay in ms after which a new button press will be accepted
#define BUTTON_COOLDOWN 600

// Update interval in ms after which the station transmits the current time state
#define UPDATE_INTERVAL_TIME 60000

// Update interval in ms after which the station locally updates the current ROOM CLIMATE data
#define UPDATE_INTERVAL_RC 5000

// Device name and custom id
#define DEVICE_NAME "G4T1-Tempera-Station #1"
#define DEVICE_SN "tempera_station_1"

#endif
