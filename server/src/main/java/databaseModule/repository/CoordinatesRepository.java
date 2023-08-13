package databaseModule.repository;

import model.Coordinates;

import java.sql.SQLException;

public interface CoordinatesRepository {

    boolean insert(Coordinates coordinates) throws SQLException;

    Coordinates read(int id) throws SQLException;

    boolean remove(int id) throws SQLException;

    boolean update(Coordinates coordinates, int id) throws SQLException;
}
