import matplotlib.pyplot as plt
import csv

file_path = "results.csv"
times = []
actual = []
interpolated = []

with open(file_path, 'r') as file:
    reader = csv.reader(file)
    next(reader)  # skip header
    for row in reader:
        try:
            times.append(float(row[0]))
            actual.append(float(row[1]))
            interpolated.append(float(row[2]))
        except ValueError:
            continue

plt.figure(figsize=(10, 6))
plt.plot(times, actual, 'bo-', label='Actual Temperature')
plt.plot(times, interpolated, 'ro--', label='Interpolated Temperature')
plt.xlabel("Time (s)")
plt.ylabel("Temperature (°C)")
plt.title("Actual vs Interpolated Temperature")
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.show()
