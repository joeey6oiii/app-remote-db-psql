package databaseModule.repository;

import java.sql.SQLException;

public interface IdentifiableRepository<T, E> {

    T getElementId(E element) throws SQLException;
}
