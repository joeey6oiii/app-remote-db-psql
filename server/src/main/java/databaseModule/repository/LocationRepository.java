package databaseModule.repository;

import model.Location;

import java.sql.SQLException;

public interface LocationRepository {

    void insert(Location location) throws SQLException;

    Location read(int id) throws SQLException;

    void remove(int id) throws SQLException;

    void update(Location location, int id) throws SQLException;
}
