package databaseModule.repository;

import userModules.users.RegisteredUser;
import userModules.users.data.RegisteredUserData;

import java.sql.SQLException;

public interface RegisteredUserRepository {

    boolean insert(RegisteredUser registeredUser) throws SQLException;

    RegisteredUser read(RegisteredUserData userData) throws SQLException;

//    boolean remove(int id) throws SQLException;                                  // Both can be implemented later if needed

//    boolean update(RegisteredUser registeredUser, int id) throws SQLException;
}
