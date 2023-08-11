package databaseModule.loader;

import java.sql.SQLException;

public interface CollectionLoader {

    void loadElementsFromDB() throws SQLException;

    void loadElementsFromDB(int ownerId) throws SQLException;
}
