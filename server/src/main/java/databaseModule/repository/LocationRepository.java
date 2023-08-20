package databaseModule.repository;

import model.Location;

import java.sql.SQLException;

public interface LocationRepository {

    Integer insert(Location location) throws SQLException;

    Location read(int id) throws SQLException;

    boolean remove(int id) throws SQLException;

    boolean update(Location location, int id) throws SQLException;
}
