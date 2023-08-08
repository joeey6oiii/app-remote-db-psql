package databaseModule;

import defaultClasses.Person;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class that represents in-program database and contains collection of the {@link Person} objects.
 */
public class PersonCollectionHandler implements CollectionHandler<Person> {
    private static PersonCollectionHandler singleInstance;
    private HashSet<Person> collection;
    private final LocalDateTime initializationTime;

    /**
     * A singleton constructor. Creates new collection and sets initialization time.
     */
    private PersonCollectionHandler() {
        collection = new HashSet<>();
        initializationTime = LocalDateTime.now();
    }

    /**
     * Retrieves the single instance of the {@link PersonCollectionHandler} class, allowing access to the in-program database.
     *
     * @return The {@link PersonCollectionHandler} instance.
     */
    public static PersonCollectionHandler getInstance() {
        if (singleInstance == null) {
            singleInstance = new PersonCollectionHandler();
        }
        return singleInstance;
    }

    /**
     * Retrieves the collection of {@link Person} objects from the database.
     *
     * @return The collection of {@link Person} objects.
     */
    public synchronized HashSet<Person> getCollection() {
        return this.collection;
    }

    /**
     * Retrieves the initialization time of the database.
     *
     * @return The initialization time as a {@link LocalDateTime} object.
     */
    public LocalDateTime getInitializationDate() {
        return this.initializationTime;
    }

    /**
     * Sorts the collection of {@link Person} objects using the {@link comparators.HeightComparator}.
     * The sorting order is based on the natural order defined in the {@link Person#compareTo(Person)} method.
     */
    public synchronized void sortCollection() {
        collection = collection.stream().sorted(Person::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Clears the collection of {@link Person} objects in the database.
     * After calling this method, the database will have no elements.
     */
    public synchronized void clearCollection() {
        this.collection.clear();
    }

    /**
     * Adds the specified {@link Person} object to the collection in the in-program database.
     * The collection is automatically sorted after adding the element.
     *
     * @param person The {@link Person} object to be added.
     */
    public synchronized void addElement(Person person) {
        this.collection.add(person);
        this.sortCollection();
    }

    /**
     * Removes the {@link Person} object from the collection in the in-program database based on the specified ID.
     *
     * @param id The ID of the {@link Person} object to be removed.
     * @return {@code true} if the element was removed successfully, {@code false} if no matching ID was found.
     */
    public synchronized boolean removeElement(int id) {
        return collection.removeIf(person -> person.getId().equals(id));
    }
}