package databaseModule.loader;

import model.Person;
import utils.MappingUtils;

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
        String selectQuery = "SELECT * FROM person";

        try (PreparedStatement statement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                collection.add(MappingUtils.mapResultSetToPerson(resultSet));
            }
        }
    }

    public void loadElementsFromDB(int ownerId) throws SQLException {
        String selectQuery = "SELECT * FROM person WHERE owner_id = ?";

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
