package databaseModule.repository;

import model.Coordinates;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class CoordinatesRepositoryImpl implements CoordinatesRepository,
        IdentifiableRepository<Integer, Coordinates>, Closeable {
    private final Connection connection;

    public CoordinatesRepositoryImpl() throws IOException, SQLException {
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

    public CoordinatesRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Coordinates coordinates) throws SQLException {
        String insertQuery = "INSERT INTO coordinates (coordinates_x, coordinates_y) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, coordinates.getX());
            preparedStatement.setInt(2, coordinates.getY());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding coordinates to the database", e);
        }
    }

    @Override
    public Coordinates read(int id) throws SQLException {
        String selectQuery = "SELECT * FROM coordinates WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(resultSet.getLong("coordinates_x"));
                    coordinates.setY(resultSet.getInt("coordinates_y"));
                    return coordinates;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading coordinates from the database", e);
        }
        return null;
    }

    @Override
    public void remove(int id) throws SQLException {
        String deleteQuery = "DELETE FROM coordinates WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Deleting coordinates failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error removing coordinates from the database", e);
        }
    }

    @Override
    public void update(Coordinates coordinates, int id) throws SQLException {
        String updateQuery = "UPDATE coordinates SET coordinates_x = ?, coordinates_y = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setLong(1, coordinates.getX());
            preparedStatement.setInt(2, coordinates.getY());
            preparedStatement.setInt(3, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Updating coordinates failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating coordinates in the database", e);
        }
    }

    @Override
    public Integer getElementId(Coordinates coordinates) throws SQLException {
        String query = "SELECT id FROM Coordinates WHERE coordinates_x = ? AND coordinates_y = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, coordinates.getX());
            preparedStatement.setInt(2, coordinates.getY());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Location not found.");
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
