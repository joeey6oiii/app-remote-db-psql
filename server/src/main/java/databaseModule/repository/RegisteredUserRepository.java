package databaseModule.repository;

import userModules.users.RegisteredUser;
import userModules.users.data.RegisteredUserData;

import java.sql.SQLException;

public interface RegisteredUserRepository {

    Integer insert(RegisteredUser registeredUser) throws SQLException;

    RegisteredUser read(String login) throws SQLException;
}
