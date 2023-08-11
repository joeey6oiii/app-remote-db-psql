package databaseModule.repository;

import model.Coordinates;

import java.sql.SQLException;

public interface CoordinatesRepository {

    void insert(Coordinates coordinates) throws SQLException;

    Coordinates read(int id) throws SQLException;

    void remove(int id) throws SQLException;

    void update(Coordinates coordinates, int id) throws SQLException;
}
