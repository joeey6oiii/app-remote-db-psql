package databaseModule.repository;

import model.Coordinates;
import model.Location;
import model.Person;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class PersonRepositoryImpl implements PersonRepository, Closeable {
    private final Connection connection;
    private final CoordinatesRepositoryImpl coordinatesRepository;
    private final LocationRepositoryImpl locationRepository;

    public PersonRepositoryImpl() throws IOException, SQLException {
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

        coordinatesRepository = new CoordinatesRepositoryImpl(this.connection);
        locationRepository = new LocationRepositoryImpl(this.connection);
    }

    public PersonRepositoryImpl(Connection connection) {
        this.connection = connection;

        coordinatesRepository = new CoordinatesRepositoryImpl(this.connection);
        locationRepository = new LocationRepositoryImpl(this.connection);
    }

    @Override
    public void insert(Person person, int ownerId) throws SQLException {
        String insertQuery = "INSERT INTO person " +
                "(name, coordinates_id, creation_date, height, " +
                "birthday, passport_id, hair_color, location_id, owner_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Coordinates coordinates = person.getCoordinates();
        Location location = person.getLocation();
        boolean nullLocation = location == null;

        coordinatesRepository.insert(coordinates);
        if (!nullLocation) {
            locationRepository.insert(location);   
        }

        int coordinatesId = coordinatesRepository.getElementId(coordinates);
        int locationId = 0;
        if (!nullLocation) {
            locationId = locationRepository.getElementId(location);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, coordinatesId);
            preparedStatement.setTimestamp(3, new Timestamp(person.getCreationDate().getTime()));
            preparedStatement.setInt(4, person.getHeight());
            preparedStatement.setDate(5, new java.sql.Date(person.getBirthday().getTime()));
            preparedStatement.setString(6, person.getPassportId());
            preparedStatement.setString(7, (person.getHairColor() != null) ? person.getHairColor().getLabel() : null);
            if (!nullLocation) {
                preparedStatement.setInt(8, locationId);
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }
            preparedStatement.setInt(9, ownerId);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding person to the database", e);
        }
    }

    @Override
    public void remove(int id) throws SQLException {
        String deleteQuery = "DELETE FROM person WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Deleting person failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error removing person from the database", e);
        }
    }

    @Override
    public void update(Person person, int id) throws SQLException {
        String updateQuery = "UPDATE person " +
                "SET name = ?, coordinates_id = ?, creation_date = ?, " +
                "height = ?, birthday = ?, passport_id = ?, " +
                "hair_color = ?, location_id = ? " +
                "WHERE id = ?";

        Coordinates coordinates = person.getCoordinates();
        Location location = person.getLocation();
        boolean nullLocation = location == null;

        int coordinatesId = coordinatesRepository.getElementId(coordinates);
        int locationId = 0;
        if (!nullLocation) {
            locationId = locationRepository.getElementId(location);
        }

        coordinatesRepository.update(coordinates, coordinatesId);
        if (!nullLocation) {
            locationRepository.update(location, locationId);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, coordinatesId);
            preparedStatement.setTimestamp(3, new Timestamp(person.getCreationDate().getTime()));
            preparedStatement.setInt(4, person.getHeight());
            preparedStatement.setDate(5, new java.sql.Date(person.getBirthday().getTime()));
            preparedStatement.setString(6, person.getPassportId());
            preparedStatement.setString(7, (person.getHairColor() != null) ? person.getHairColor().getLabel() : null);
            if (!nullLocation) {
                preparedStatement.setInt(8, locationId);
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }
            preparedStatement.setInt(9, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Updating person failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating person in the database", e);
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
