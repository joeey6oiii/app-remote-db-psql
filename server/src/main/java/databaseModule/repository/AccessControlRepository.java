package databaseModule.repository;

import java.sql.SQLException;

public interface AccessControlRepository {

    boolean checkAccess(int elementId, int ownerId) throws SQLException;
}
