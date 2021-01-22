package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleManager {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeToConsole(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String readString = null;
        do {
            try {
                readString = reader.readLine();
            } catch (IOException e) {
                System.out.println("Error: provided message is not a string, try again");
            }

        } while (readString == null);
        return readString;
    }

    public static int readInt() {
        int numberRead;

        while (true) {
            try {
                numberRead = Integer.parseInt(readString());
                return numberRead;
            } catch (NumberFormatException e) {
                System.out.println("Error: incorrect number provided, try again");
            }
        }
    }
}
