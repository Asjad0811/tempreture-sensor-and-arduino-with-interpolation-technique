import serial
import csv
import time

COM_PORT = 'COM7'        # Change to your actual COM port (e.g. COM4)
BAUD_RATE = 9600
FILE_NAME = 'temperature_log.csv'

try:
    ser = serial.Serial(COM_PORT, BAUD_RATE, timeout=1)
    print(f"Connected to {COM_PORT}")
    time.sleep(2)

    with open(FILE_NAME, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Time', 'Temperature'])

        print("Logging... Press Ctrl+C to stop.\n")

        while True:
            line = ser.readline().decode('utf-8').strip()
            if ',' in line:
                try:
                    time_val, temp_val = map(str.strip, line.split(","))
                    print(f"{time_val},{temp_val}")
                    writer.writerow([time_val, temp_val])
                except Exception as e:
                    print("Invalid data:", line)
except KeyboardInterrupt:
    print("\nLogging stopped by user.")
except Exception as e:
    print(f"Error: {e}")
finally:
    if 'ser' in locals() and ser.is_open:
        ser.close()
