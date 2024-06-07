
# TEMPERA - Station

The station makes use of an Arduino Nano 33 BLE and a Adafruit BME688 sensory board, as well as additional hardware. 
The Arduino itself is an open-source electronics platform with its own IDE  and hardware but for our purposes we 
use the extension _PlatformIO_ for VS-Code. 

Common issues with Arduino involve excessive current flow caused by programming errors, 
which can exceed the limits of electronic components, potentially causing damage.
So always ensure the circuit design is sound, double check any connections and verify your code.

If you want to try out this program just import the source-code directory as its own project using
_PlatformIO_ and don't forget to set the _upload_port_ accordingly! You are then able to build it and upload
it to your Arduino (the serial output also returns some feedback of what the station is doing once it's on).
For further details and the circuit layout please refer to the corresponding wiki page. 

If you have any question feel free to Mail me: `simon.c.froehlich@student.uibk.ac.at`

## Additional helpful resources:

- Arduino Nano 33 BLE: https://docs.arduino.cc/hardware/nano-33-ble
- Pin Layput: https://content.arduino.cc/assets/Pinout-NANOble_latest.pdf
- Datasheet: https://docs.arduino.cc/resources/datasheets/ABX00030-datasheet.pdf
