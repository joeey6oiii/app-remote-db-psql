package objectBuilder;

import model.Color;
import model.Coordinates;
import model.Location;
import model.Person;
import objectBuilder.utils.StringToDateParser;
import outputService.ColoredPrintStream;
import outputService.OutputSource;
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
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());
        Scanner consoleInputReader = new Scanner(System.in);
        Person person = new Person();

        person.setCreationDate(new Date());

        cps.print("Enter name\n$ ");
        String name = consoleInputReader.nextLine();
        while (!new NameValidator().validate(name)) {
            cps.print("Invalid name. Please enter a valid name\n$ ");
            name = consoleInputReader.nextLine();
        }
        person.setName(name);

        cps.println("Creating coordinates:");
        CoordinatesObjectBuilder coordinatesBuilder = new CoordinatesObjectBuilder();
        Coordinates coordinates = coordinatesBuilder.buildObject();
        while (!new CoordinatesValidator().validate(coordinates)) {
            cps.println("Invalid coordinates. Please enter valid coordinates:");
            coordinates = coordinatesBuilder.buildObject();
        }
        person.setCoordinates(coordinates);

        cps.print("Enter height\n$ ");
        int height = Integer.parseInt(consoleInputReader.next());
        while (!new HeightValidator().validate(height)) {
            try {
                cps.print("Invalid height. Please enter a valid height\n$ ");
                height = Integer.parseInt(consoleInputReader.next());
            } catch (Exception ignored) {
            }
        }
        person.setHeight(height);

        cps.print("Enter birthday. Use <<yyyy-MM-dd HH:mm:ss>> pattern\n$ ");
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
                cps.print("Invalid date. Please enter a valid date. Use <<" + StringToDateParser.getPattern() + ">> pattern\n$ ");
            }
        }
        person.setBirthday(birthday);

        cps.print("Enter passportID of length 5 or more\n$ ");
        String passportId = consoleInputReader.nextLine();
        while (!new PassportIDValidator().validate(passportId)) {
            cps.print("Invalid passportId. Please enter a valid passportId of length 5 or more\n$ ");
            passportId = consoleInputReader.nextLine();
        }
        person.setPassportId(passportId);

        cps.print("Choose hair color:\n" + Color.listValues() + "\n" +
                "Enter anything except color name to skip this operation\n$ ");
        consoleInput = consoleInputReader.nextLine().trim();
        Color hairColor = null;
        if (!consoleInput.isEmpty()) {
            hairColor = Color.getColorFromLabel(consoleInput.toLowerCase());
        }
        person.setHairColor(hairColor);

        cps.println("Creating Location. Press \"ENTER\" to skip this operation");
        LocationObjectBuilder locationBuilder = new LocationObjectBuilder();
        Location location = locationBuilder.buildObject();
        while (!new LocationValidator().validate(location)) {
            location = locationBuilder.buildObject();
        }
        person.setLocation(location);

        return person;
    }
}