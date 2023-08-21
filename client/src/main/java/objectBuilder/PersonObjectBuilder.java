package objectBuilder;

import model.Color;
import model.Coordinates;
import model.Location;
import model.Person;
import objectBuilder.utils.StringToDateParser;
import validators.*;

import java.util.Date;
import java.util.Scanner;

/**
 * A class that implements a generating ability.
 */
public class PersonObjectBuilder implements ObjectBuilder {

    /**
     * Method that creates an object of class Person.
     *
     * @return Person object
     */
    public Person buildObject() {
        Scanner consoleInputReader = new Scanner(System.in);
        Person person = new Person();

        person.setCreationDate(new Date());

        System.out.print("Enter name\n$ ");
        String name = consoleInputReader.nextLine();
        while (!new NameValidator().validate(name)) {
            System.out.print("Invalid name. Please enter a valid name\n$ ");
            name = consoleInputReader.nextLine();
        }
        person.setName(name);

        System.out.println("Creating coordinates:");
        CoordinatesObjectBuilder coordinatesBuilder = new CoordinatesObjectBuilder();
        Coordinates coordinates = coordinatesBuilder.buildObject();
        while (!new CoordinatesValidator().validate(coordinates)) {
            System.out.println("Invalid coordinates. Please enter valid coordinates:");
            coordinates = coordinatesBuilder.buildObject();
        }
        person.setCoordinates(coordinates);

        System.out.print("Enter height\n$ ");
        int height = Integer.parseInt(consoleInputReader.next());
        while (!new HeightValidator().validate(height)) {
            try {
                System.out.print("Invalid height. Please enter a valid height\n$ ");
                height = Integer.parseInt(consoleInputReader.next());
            } catch (Exception ignored) {
            }
        }
        person.setHeight(height);

        System.out.print("Enter birthday. Use <<yyyy-MM-dd HH:mm:ss>> pattern\n$ ");
        consoleInputReader.nextLine();
        Date birthday;
        String consoleInput;
        while (true) {
            consoleInput = consoleInputReader.nextLine().trim();
            try {
                if (consoleInput.isEmpty()) {
                    throw new IllegalArgumentException();
                }
                birthday = StringToDateParser.parse(consoleInput);
                break;
            } catch (Exception e) {
                System.out.print("Invalid date. Please enter a valid date. Use <<yyyy-MM-dd HH:mm:ss>> pattern\n$ ");
            }
        }
        person.setBirthday(birthday);

        System.out.print("Enter passportID of length 5 or more\n$ ");
        String passportId = consoleInputReader.nextLine();
        while (!new PassportIDValidator().validate(passportId)) {
            System.out.print("Invalid passportId. Please enter a valid passportId of length 5 or more\n$ ");
            passportId = consoleInputReader.nextLine();
        }
        person.setPassportId(passportId);

        System.out.print("Choose hair color:\n" + Color.listValues() + "\n" +
                "Enter anything except color name to skip this operation\n$ ");
        consoleInput = consoleInputReader.nextLine().trim();
        Color hairColor = null;
        if (!consoleInput.isEmpty()) {
            hairColor = Color.getColorFromLabel(consoleInput.toLowerCase());
        }
        person.setHairColor(hairColor);

        System.out.println("Creating Location. Press \"ENTER\" to skip this operation");
        LocationObjectBuilder locationBuilder = new LocationObjectBuilder();
        Location location = locationBuilder.buildObject();
        while (!new LocationValidator().validate(location)) {
            location = locationBuilder.buildObject();
        }
        person.setLocation(location);

        return person;
    }
}