package database;

import defaultClasses.Person;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

public class PersonDB<T extends Collection<Person>> implements Closeable {
    private final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/studs";
    private final Connection connection;
    private final T collection;

    public PersonDB(T collection) throws IOException, SQLException {
        this.collection = collection;
        Properties info = new Properties();
        try (InputStream configStream = getClass().getResourceAsStream("/db.cfg")) {
            info.load(configStream);
        }
        connection = DriverManager.getConnection(DB_URL, info);
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }

}
