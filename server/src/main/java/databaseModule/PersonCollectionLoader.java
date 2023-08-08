package databaseModule;

import defaultClasses.Color;
import defaultClasses.Coordinates;
import defaultClasses.Location;
import defaultClasses.Person;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;

public class PersonCollectionLoader<T extends Collection<Person>> implements Closeable {
    private final Connection connection;
    private final T collection;

    private static final String SELECT_ALL_PERSONS_QUERY = "SELECT * FROM person";
    private static final String SELECT_PERSONS_BY_USER_ID_QUERY = "SELECT * FROM person WHERE user_id = ?";

    public PersonCollectionLoader(T collection) throws IOException, SQLException {
        this.collection = collection;

        Properties info = new Properties();
        try (InputStream configStream = getClass().getResourceAsStream("/db.cfg")) {
            info.load(configStream);
        } catch (IOException e) {
            throw new IOException("Error loading db.cfg file", e);
        }

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/studs", info);
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database", e);
        }
    }

    public void loadPersonsFromDB() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PERSONS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                collection.add(this.mapResultSetToPerson(resultSet));
            }
        }
    }

    public void loadPersonsFromDB(int userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_PERSONS_BY_USER_ID_QUERY)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    collection.add(this.mapResultSetToPerson(resultSet));
                }
            }
        }
    }

    private Person mapResultSetToPerson(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));
        person.setCoordinates(new Coordinates(resultSet.getLong("coordinates_x"),
                resultSet.getInt("coordinates_y")));
        person.setCreationDate(resultSet.getTimestamp("creation_date"));
        person.setCreationDate(new Date(resultSet.getTimestamp("creation_date").getTime()));
        person.setHeight(resultSet.getInt("height"));
        person.setBirthday(resultSet.getDate("birthday"));
        person.setPassportID(resultSet.getString("passport_id"));
        person.setHairColor(Color.getColorFromLabel(resultSet.getString("hair_color")));
        person.setLocation(new Location(resultSet.getFloat("location_x"),
                resultSet.getInt("location_y"), resultSet.getString("location_name")));

        return person;
    }

    public T getCollection() {
        return this.collection;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
