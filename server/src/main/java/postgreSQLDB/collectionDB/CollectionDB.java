package postgreSQLDB.collectionDB;

import defaultClasses.Person;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

public class CollectionDB<T extends Collection<Person>> implements Closeable {
    private final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/studs";
    private final Connection connection;
    private final T collection;

    public CollectionDB(T collection) throws IOException, SQLException {
        this.collection = collection;

        Properties info = new Properties();
        try (InputStream configStream = getClass().getResourceAsStream("/db.cfg")) {
            info.load(configStream);
        } catch (IOException e) {
            throw new IOException("Error loading db.cfg file", e);
        }

        try {
            connection = DriverManager.getConnection(DB_URL, info);
        } catch (SQLException e) {
            throw new SQLException("Error connecting to the database", e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}
