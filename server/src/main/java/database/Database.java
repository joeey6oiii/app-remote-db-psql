package database;

import defaultClasses.Person;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class that represents database and contains collection of the {@link Person} objects.
 */

public class Database {
    private static Database singleInstance;
    private HashSet<Person> database;
    private final LocalDateTime initializationTime;

    /**
     * A database constructor. Creates new collection and sets initialization time.
     */

    private Database() {
        database = new HashSet<>();
        initializationTime = LocalDateTime.now();
    }

    /**
     * A method for getting database at any point in the program.
     */

    public static Database getInstance() {
        if (singleInstance == null) {
            singleInstance = new Database();
        }
        return singleInstance;
    }

    /**
     * @return database
     */

    public HashSet<Person> getCollection() {
        return this.database;
    }

    /**
     * @return InitializationTime
     */

    public LocalDateTime getInitializationTime() {
        return this.initializationTime;
    }

    /**
     * Sorts the collection using the {@link Person} {@link comparators.HeightComparator}.
     */

    public void sort() {
        database = database.stream().sorted(Person::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Clears the collection.
     */

    public void clear() {
        this.database.clear();
    }

    /**
     * Adds the specified element to the collection in the database.
     *
     * @param person the element to add
     */

    public void add(Person person) {
        this.database.add(person);

        this.sort();
    }

    /**
     * Removes an element from the collection in the database by the specified id.
     *
     * @param id the id of the element to be removed
     * @return true, if the element was removed, otherwise false
     */

    public boolean remove(int id) {
        return database.removeIf(person -> person.getId().equals(id));
    }
}