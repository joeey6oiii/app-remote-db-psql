package databaseModule.repository;

import model.Coordinates;

import java.sql.SQLException;
import java.util.Optional;

public interface CoordinatesRepository {

    void insert(Coordinates coordinates) throws SQLException;

    Optional<Coordinates> read(int id) throws SQLException;

    void remove(int id) throws SQLException;

    void update(Coordinates coordinates, int id) throws SQLException;
}
