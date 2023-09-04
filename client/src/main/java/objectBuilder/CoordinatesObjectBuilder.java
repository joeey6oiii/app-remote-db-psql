package objectBuilder;

import model.Coordinates;
import outputService.ColoredPrintStream;
import outputService.OutputSource;

import java.util.Scanner;

/**
 * A class that allows the user to create an object of class Coordinates.
 */
public class CoordinatesObjectBuilder implements ObjectBuilder {

    /**
     * Method that creates an object of class Coordinates.
     *
     * @return coordinates
     */
    public Coordinates buildObject() {
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());

        Coordinates coordinates;
        try {
            coordinates = new Coordinates();
            Scanner consoleInputReader = new Scanner(System.in);

            cps.print("Enter X coordinate\n$ ");
            long x = Long.parseLong(consoleInputReader.nextLine());
            coordinates.setX(x);

            cps.print("Enter Y coordinate\n$ ");
            int y = Integer.parseInt(consoleInputReader.nextLine());
            coordinates.setY(y);
        } catch (Exception e) {
            return null;
        }

        return coordinates;
    }
}