package databaseModule.repository;

import model.Location;
import utils.MappingUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class LocationRepositoryImpl implements LocationRepository, IdentifiableRepository<Integer, Location>, Closeable {
    private final Connection connection;

    public LocationRepositoryImpl() throws IOException, SQLException {
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

    public LocationRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(Location location) throws SQLException {
        String insertQuery = "INSERT INTO location (location_x, location_y, location_name) VALUES (?, ?, ?)";

        int affectedRows;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setFloat(1, location.getX());
            preparedStatement.setInt(2, location.getY());
            preparedStatement.setString(3, location.getName());

            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding location to the database", e);
        }

        return affectedRows > 0;
    }

    @Override
    public Location read(int id) throws SQLException {
        String selectQuery = "SELECT * FROM location WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return MappingUtils.mapResultSetToLocation(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading location from the database", e);
        }
    }

    @Override
    public boolean remove(int id) throws SQLException {
        String deleteQuery = "DELETE FROM location WHERE id = ?";

        int affectedRows;
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);

            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error removing location from the database", e);
        }

        return affectedRows > 0;
    }

    @Override
    public boolean update(Location location, int id) throws SQLException {
        String updateQuery = "UPDATE location SET location_x = ?, location_y = ?, location_name = ? WHERE id = ?";

        int affectedRows;
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setFloat(1, location.getX());
            preparedStatement.setInt(2, location.getY());
            preparedStatement.setString(3, location.getName());
            preparedStatement.setInt(4, id);

            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating location in the database", e);
        }

        return affectedRows > 0;
    }

    @Override
    public Integer getElementId(Location location) throws SQLException {
        String query = "SELECT id FROM Location WHERE location_x = ? AND location_y = ? AND location_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setFloat(1, location.getX());
            preparedStatement.setInt(2, location.getY());
            if (location.getName() != null) {
                preparedStatement.setString(3, location.getName());
            } else {
                preparedStatement.setNull(3, Types.VARCHAR);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting location id from the database", e);
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
