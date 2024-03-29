package databaseModule.repository;

import userModules.passwordService.EncryptedPassword;
import userModules.users.RegisteredUser;
import userModules.users.User;
import userModules.users.data.RegisteredUserData;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class RegisteredUserRepositoryImpl implements RegisteredUserRepository, Closeable {
    private final Connection connection;

    public RegisteredUserRepositoryImpl() throws IOException, SQLException {
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

    public RegisteredUserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Integer insert(RegisteredUser registeredUser) throws SQLException {
        String insertQuery = "INSERT INTO registeredusers (login, hashedpassword, salt) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            RegisteredUserData userData = registeredUser.getRegisteredUserData();

            preparedStatement.setString(1, userData.getLogin());
            preparedStatement.setBytes(2, userData.getEncryptedPassword().getHashedPassword());
            preparedStatement.setBytes(3, userData.getEncryptedPassword().getSalt());

            int affectedRows = preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                registeredUser.setId(generatedKeys.getInt(1));
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding registered user to the database", e);
        }

        return null;
    }

    @Override
    public RegisteredUser read(String login) throws SQLException {
        String selectQuery = "SELECT * FROM registeredusers WHERE login = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    RegisteredUserData data = new RegisteredUserData(login,
                            new EncryptedPassword(resultSet.getBytes("hashedpassword"),
                                    resultSet.getBytes("salt")));
                    RegisteredUser registeredUser = new RegisteredUser(data, new User(null, 0));
                    registeredUser.setId(resultSet.getInt("id"));
                    return registeredUser;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading registered user from the database", e);
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
