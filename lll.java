import java.io.*;
import java.util.*;

public class lll {

    public static double interpolate(List<Double> x, List<Double> y, double target) {
        int n = x.size();
        double result = 0;
        for (int i = 0; i < n; i++) {
            double term = y.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (target - x.get(j)) / (x.get(i) - x.get(j));
                }
            }
            result += term;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter path to CSV file (e.g., temperature_log.csv): ");
        String filePath = sc.nextLine();

        List<Double> timeStamps = new ArrayList<>();
        List<Double> temperatures = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    try {
                        double time = Double.parseDouble(parts[0].trim());
                        double temp = Double.parseDouble(parts[1].trim());
                        timeStamps.add(time);
                        temperatures.add(temp);
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }

            if (timeStamps.size() < 10) {
                System.out.println("Not enough data. Need at least 10 readings.");
                return;
            }

            List<Double> lastTimes = timeStamps.subList(timeStamps.size() - 10, timeStamps.size());
            List<Double> lastTemps = temperatures.subList(temperatures.size() - 10, temperatures.size());

            System.out.print("Enter timestamp to interpolate: ");
            double targetTime = sc.nextDouble();
            double interpolatedTemp = interpolate(lastTimes, lastTemps, targetTime);
            System.out.printf("Interpolated Temperature at %.2f sec: %.2f °C\n", targetTime, interpolatedTemp);

            sc.nextLine(); // clear buffer
            System.out.print("Enter actual temperature at that time (if known), or press Enter to skip: ");
            String actualInput = sc.nextLine();

            if (!actualInput.isEmpty()) {
                try {
                    double actualTemp = Double.parseDouble(actualInput);
                    double absError = Math.abs(actualTemp - interpolatedTemp);
                    double relError = (absError / Math.abs(actualTemp)) * 100;

                    System.out.printf("Absolute Error: %.4f °C\n", absError);
                    System.out.printf("Relative Error: %.2f%%\n", relError);

                    File resultFile = new File("results.csv");
                    boolean isNew = !resultFile.exists();
                    try (PrintWriter out = new PrintWriter(new FileWriter(resultFile, true))) {
                        if (isNew) out.println("Time,Actual,Interpolated");
                        out.printf("%.2f,%.2f,%.2f\n", targetTime, actualTemp, interpolatedTemp);
                        System.out.println("Saved to results.csv");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid actual temperature input.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
