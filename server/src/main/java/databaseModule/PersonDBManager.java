package databaseModule;

import defaultClasses.Color;
import defaultClasses.Person;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class PersonDBManager implements Closeable {
    private final Connection connection;

    public PersonDBManager() throws IOException, SQLException {
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

    public void insertElementIntoDB(Person p, int userId) throws SQLException {
        String insertQuery = "INSERT INTO person " +
                "(name, coordinates_x, coordinates_y, creation_date, height, " +
                "birthday, passport_id, hair_color, location_x, location_y, " +
                "location_name, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, p.getName());
            preparedStatement.setLong(2, p.getCoordinates().getX());
            preparedStatement.setInt(3, p.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new Timestamp(p.getCreationDate().getTime()));
            preparedStatement.setInt(5, p.getHeight());
            preparedStatement.setDate(6, new java.sql.Date(p.getBirthday().getTime()));
            preparedStatement.setString(7, p.getPassportId());
            preparedStatement.setString(8, p.getHairColor().getLabel());
            preparedStatement.setFloat(9, p.getLocation().getX());
            preparedStatement.setInt(10, p.getLocation().getY());
            preparedStatement.setString(11, p.getLocation().getName());
            preparedStatement.setInt(12, userId);

            preparedStatement.executeUpdate();

            p.setId(this.getLastId());
        } catch (SQLException e) {
            throw new SQLException("Error adding person to the database", e);
        }
    }

    public void removeElementFromDB(int id) throws SQLException {
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

    public void updateElementInDB(Person p, int id) throws SQLException {
        String updateQuery = "UPDATE person " +
                "SET name = ?, coordinates_x = ?, coordinates_y = ?, " +
                "creation_date = ?, height = ?, birthday = ?, " +
                "passport_id = ?, hair_color = ?, location_x = ?, " +
                "location_y = ?, location_name = ?, user_id = ? " +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, p.getName());
            preparedStatement.setLong(2, p.getCoordinates().getX());
            preparedStatement.setInt(3, p.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new Timestamp(p.getCreationDate().getTime()));
            preparedStatement.setInt(5, p.getHeight());
            preparedStatement.setDate(6, new java.sql.Date(p.getBirthday().getTime()));
            preparedStatement.setString(7, p.getPassportId());
            preparedStatement.setString(8, p.getHairColor().getLabel());
            preparedStatement.setFloat(9, p.getLocation().getX());
            preparedStatement.setInt(10, p.getLocation().getY());
            preparedStatement.setString(11, p.getLocation().getName());
            preparedStatement.setInt(12, id); // user_id
            preparedStatement.setInt(13, id); // WHERE id = ?

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Updating person failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating person in the database", e);
        }
    }

    private int getLastId() throws SQLException {
        String getLastIdQuery = "SELECT id FROM person ORDER BY id DESC LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(getLastIdQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return 1; // If no rows are returned
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting last id from the database", e);
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
