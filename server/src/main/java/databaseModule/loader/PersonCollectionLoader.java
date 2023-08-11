package databaseModule.loader;

import model.Color;
import model.Coordinates;
import model.Location;
import model.Person;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;

public class PersonCollectionLoader<T extends Collection<Person>> implements CollectionLoader, Closeable {
    private final Connection connection;
    private final T collection;

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

    public PersonCollectionLoader(Connection connection, T collection) {
        this.connection = connection;
        this.collection = collection;
    }

    public T getCollection() {
        return this.collection;
    }

    public void loadElementsFromDB() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM person");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                collection.add(this.mapResultSetToPerson(resultSet));
            }
        }
    }

    public void loadElementsFromDB(int ownerId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE owner_id = ?")) {
            statement.setInt(1, ownerId);
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

        Coordinates coordinates = new Coordinates();
        coordinates.setX(resultSet.getLong("coordinates_x"));
        coordinates.setY(resultSet.getInt("coordinates_y"));
        person.setCoordinates(coordinates);

        person.setCreationDate(resultSet.getTimestamp("creation_date"));
        person.setHeight(resultSet.getInt("height"));
        person.setBirthday(resultSet.getTimestamp("birthday"));
        person.setPassportId(resultSet.getString("passport_id"));

        String hairColorLabel = resultSet.getString("hair_color");
        if (hairColorLabel != null) {
            Color hairColor = Color.getColorFromLabel(hairColorLabel);
            person.setHairColor(hairColor);
        }

        if (!resultSet.wasNull()) {
            Location location = new Location();
            location.setX(resultSet.getFloat("location_x"));
            location.setY(resultSet.getInt("location_y"));
            location.setName(resultSet.getString("location_name"));
            person.setLocation(location);
        }

        return person;
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
