package databaseModule.repository;

import model.Location;

import java.sql.SQLException;
import java.util.Optional;

public interface LocationRepository {

    void insert(Location location) throws SQLException;

    Optional<Location> read(int id) throws SQLException;

    void remove(int id) throws SQLException;

    void update(Location location, int id) throws SQLException;
}
