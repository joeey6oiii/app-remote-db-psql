package databaseModule.loader;

import model.Person;
import databaseModule.repository.utils.MappingUtils;

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
        String selectQuery = """
                SELECT\s
                    person.id,
                    person.name AS name,
                    coordinates.coordinates_x AS coordinates_x,
                    coordinates.coordinates_y AS coordinates_y,
                    person.creation_date,
                    person.height AS height,
                    person.birthday AS birthday,
                    person.passport_id,
                    person.hair_color,
                    person.location_id,
                    location.location_x AS location_x,
                    location.location_y AS location_y,
                    location.location_name AS location_name\s
                FROM person\s
                LEFT JOIN coordinates ON person.coordinates_id = coordinates.id\s
                LEFT JOIN location ON person.location_id = location.id;
                """;

        try (PreparedStatement statement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                collection.add(MappingUtils.mapResultSetToPerson(resultSet));
            }
        }
    }

    public void loadElementsFromDB(int ownerId) throws SQLException {
        String selectQuery = """
                SELECT\s
                    person.id,
                    person.name AS name,
                    coordinates.coordinates_x AS coordinates_x,
                    coordinates.coordinates_y AS coordinates_y,
                    person.creation_date,
                    person.height AS height,
                    person.birthday AS birthday,
                    person.passport_id,
                    person.hair_color,
                    person.location_id,
                    location.location_x AS location_x,
                    location.location_y AS location_y,
                    location.location_name AS location_name,
                    registeredusers.id AS registered_user_id,
                    registeredusers.hashedpassword,
                    registeredusers.salt\s
                FROM person
                LEFT JOIN coordinates ON person.coordinates_id = coordinates.id
                LEFT JOIN location ON person.location_id = location.id
                LEFT JOIN registeredusers ON person.owner_id = registeredusers.id
                WHERE person.owner_id = ?;
                """;

        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, ownerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    collection.add(MappingUtils.mapResultSetToPerson(resultSet));
                }
            }
        }
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
