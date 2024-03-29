package databaseModule.repository;

import model.Person;

import java.sql.SQLException;

public interface PersonRepository {

    Integer insert(Person person, int ownerId) throws SQLException;

    Person read(int id) throws SQLException;

    boolean remove(int id) throws SQLException;

    boolean update(Person person, int id) throws SQLException;
}
