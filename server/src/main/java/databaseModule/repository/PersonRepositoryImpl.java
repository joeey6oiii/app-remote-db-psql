package databaseModule.repository;

import model.Coordinates;
import model.Location;
import model.Person;
import utils.MappingUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class PersonRepositoryImpl implements PersonRepository, AccessControlRepository, Closeable {
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
    public Integer insert(Person person, int ownerId) throws SQLException {
        try {
            connection.setAutoCommit(false);

            String insertQuery = "INSERT INTO person " +
                    "(name, coordinates_id, creation_date, height, " +
                    "birthday, passport_id, hair_color, location_id, owner_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Coordinates coordinates = person.getCoordinates();
            Location location = person.getLocation();
            boolean nullLocation = location == null;

            int coordinatesId = coordinatesRepository.insert(coordinates);
            if (!(coordinatesId > 0)) {
                throw new SQLException("Error adding coordinates to the database");
            }

            int locationId = 0;
            if (!nullLocation) {
                locationId = locationRepository.insert(location);
                if (!(locationId > 0)) {
                    throw new SQLException("Error adding location to the database");
                }
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

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        connection.commit();
                        person.setId(generatedKeys.getInt(1));
                        return generatedKeys.getInt(1);
                    }
                } else {
                    connection.rollback();
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Error adding person to the database", e);
            }

            return null;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Person read(int id) throws SQLException {
        String selectQuery = "SELECT * FROM person WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return MappingUtils.mapResultSetToPerson(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading person from the database", e);
        }
    }

    @Override
    public boolean remove(int id) throws SQLException {
        try {
            connection.setAutoCommit(false);

            String selectQuery = "SELECT * FROM person WHERE id = ?";
            String deleteQuery = "DELETE FROM person WHERE id = ?";

            int coordinatesId = 0;
            int locationId = 0;

            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                selectStatement.setInt(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        coordinatesId = resultSet.getInt("coordinates_id");
                        locationId = resultSet.getInt("location_id");
                    } else {
                        return false;
                    }
                }

                deleteStatement.setInt(1, id);
                int affectedRows = deleteStatement.executeUpdate();

                if (affectedRows > 0) {
                    this.coordinatesRepository.remove(coordinatesId);
                    if (locationId != 0) {
                        this.locationRepository.remove(locationId);
                    }

                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Error removing person from the database", e);
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public boolean update(Person person, int id) throws SQLException {
        try {
            connection.setAutoCommit(false);

            String getIdsQuery = "SELECT coordinates_id, location_id FROM person WHERE id = ?";

            try (PreparedStatement getIdStatement = connection.prepareStatement(getIdsQuery)) {
                getIdStatement.setInt(1, id);

                try (ResultSet resultSet = getIdStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int updated = 0;

                        Coordinates coordinates = person.getCoordinates();
                        Location location = person.getLocation();
                        boolean nullLocation = location == null;

                        int coordinatesId = resultSet.getInt("coordinates_id");
                        int locationId = resultSet.getInt("location_id");
                        boolean remove = false;

                        if (resultSet.wasNull() && !nullLocation) {
                            locationId = locationRepository.insert(location);
                            if (locationId > 0) {
                                updated += 1;
                            }
                        } else if (!nullLocation) {
                            if (locationRepository.update(location, locationId)) {
                                updated += 1;
                            }
                        } else if (!resultSet.wasNull()) {
                            remove = true;
                        } else {
                            updated += 1;
                        }

                        if (coordinatesRepository.update(coordinates, coordinatesId)) {
                            updated += 1;
                        }

                        String updateQuery = "UPDATE person " +
                                "SET name = ?, coordinates_id = ?, creation_date = ?, " +
                                "height = ?, birthday = ?, passport_id = ?, " +
                                "hair_color = ?, location_id = ? " +
                                "WHERE id = ?";

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

                            updated += preparedStatement.executeUpdate();

                            if (remove && this.locationRepository.remove(locationId)) {
                                updated += 1;
                            }
                        } catch (SQLException e) {
                            connection.rollback();
                            throw new SQLException("Error updating person in the database", e);
                        }

                        if (updated >= 3) {
                            connection.commit();
                            return true;
                        } else {
                            connection.rollback();
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public boolean checkAccess(int elementId, int ownerId) throws SQLException {
        String getIdsQuery = "SELECT owner_id FROM person WHERE id = ?";

        try (PreparedStatement getIdStatement = connection.prepareStatement(getIdsQuery)) {
            getIdStatement.setInt(1, elementId);

            try (ResultSet resultSet = getIdStatement.executeQuery()) {
                if (resultSet.next()) {
                    int personOwnerId = resultSet.getInt("owner_id");
                    return personOwnerId == ownerId;
                } else {
                    return false;
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
