package objectBuilder;

import defaultClasses.Location;

import java.util.Scanner;

/**
 * A class that allows the user to create an object of class Location.
 *
 * @author Dmitrii Chebanenko
 */

public class LocationBuilder implements BuildAble {

    /**
     * Method that creates an object of class Location.
     *
     * @return Location
     */

    public Location buildObject() {
        Scanner consoleInputReader = new Scanner(System.in);
        Location location = new Location();
        String consoleInput;

        System.out.print("Enter X coordinate\n$ ");
        while (true) {
            try {
                consoleInput = consoleInputReader.nextLine();
                if (consoleInput.isEmpty()) {
                    return null;
                } else {
                    Float x = Float.parseFloat(consoleInput);
                    location.setX(x);
                    break;
                }
            } catch (Exception e){
                System.out.print("Invalid coordinate X. Please enter a valid X coordinate\n$ ");
            }
        }

        System.out.print("Enter Y coordinate\n$ ");
        while (true) {
            try {
                consoleInput = consoleInputReader.nextLine();
                if (consoleInput.isEmpty()) {
                    return null;
                } else {
                    Integer y = Integer.parseInt(consoleInput);
                    location.setY(y);
                    break;
                }
            } catch (Exception e){
                System.out.print("Invalid coordinate Y. Please enter a valid Y coordinate\n$ ");
            }
        }

        System.out.print("Enter name. Press \"ENTER\" to skip this operation\n$ ");
        String name = consoleInputReader.nextLine();
        if (name.isEmpty()) {
            location.setName(null);
        } else {
            location.setName(name);
        }

        return location;
     }

}
