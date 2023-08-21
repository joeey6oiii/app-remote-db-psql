package databaseModule.repository.utils;

import model.Color;
import model.Coordinates;
import model.Location;
import model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MappingUtils {

    public static Person mapResultSetToPerson(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));

        person.setCoordinates(mapResultSetToCoordinates(resultSet));

        person.setCreationDate(new Date(resultSet.getTimestamp("creation_date").getTime()));
        person.setHeight(resultSet.getInt("height"));
        person.setBirthday(new Date(resultSet.getTimestamp("birthday").getTime()));
        person.setPassportId(resultSet.getString("passport_id"));

        String hairColorLabel = resultSet.getString("hair_color");
        if (hairColorLabel != null) {
            Color hairColor = Color.getColorFromLabel(hairColorLabel);
            person.setHairColor(hairColor);
        }

        int locationId = resultSet.getInt("location_id");
        if (!resultSet.wasNull()) {
            person.setLocation(mapResultSetToLocation(resultSet));
        }

        return person;
    }

    public static Location mapResultSetToLocation(ResultSet resultSet) throws SQLException {
        Location location = new Location();
        location.setX(resultSet.getFloat("location_x"));
        location.setY(resultSet.getInt("location_y"));
        location.setName(resultSet.getString("location_name"));

        return location;
    }

    public static Coordinates mapResultSetToCoordinates(ResultSet resultSet) throws SQLException {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(resultSet.getLong("coordinates_x"));
        coordinates.setY(resultSet.getInt("coordinates_y"));

        return coordinates;
    }
}
