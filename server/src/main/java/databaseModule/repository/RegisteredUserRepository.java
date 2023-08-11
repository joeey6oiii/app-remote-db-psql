package databaseModule.repository;

import userModules.users.RegisteredUser;

import java.sql.SQLException;
import java.util.Optional;

public interface RegisteredUserRepository {

    void insert(RegisteredUser registeredUser) throws SQLException;

    Optional<RegisteredUser> read(int id) throws SQLException;

//    void remove(int id) throws SQLException;                                  // Both can be implemented later if needed

//    void update(RegisteredUser registeredUser, int id) throws SQLException;
}
