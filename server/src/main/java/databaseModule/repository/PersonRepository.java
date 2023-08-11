package databaseModule.repository;

import model.Person;

import java.sql.SQLException;

public interface PersonRepository {

    void insert(Person person, int ownerId) throws SQLException;

    void remove(int id) throws SQLException;

    void update(Person person, int id) throws SQLException;
}
