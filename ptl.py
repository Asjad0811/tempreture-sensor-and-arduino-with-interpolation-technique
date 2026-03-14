import os
import csv
import matplotlib.pyplot as plt

file_path = "temperature_log.csv"
if not os.path.isfile(file_path):
    raise FileNotFoundError(f"{file_path} not found")

times = []
temperatures = []

with open(file_path, 'r', newline='') as file:
    reader = csv.reader(file)
    for row in reader:
        if len(row) < 2:
            continue
        try:
            time = float(row[0])
            temp = float(row[1])
        except ValueError:
            continue
        times.append(time)
        temperatures.append(temp)

if not times or not temperatures:
    raise ValueError("No valid numeric data found in CSV")

plt.figure(figsize=(10, 6))
plt.plot(times, temperatures, 'b-o', label='Temperature')
plt.xlabel("Time (seconds)")
plt.ylabel("Temperature (°C)")
plt.title("Temperature vs Time")
plt.grid(True)
plt.legend()
plt.tight_layout()
plt.show()