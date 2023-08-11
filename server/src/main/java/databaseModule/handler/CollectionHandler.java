package databaseModule.handler;

import java.time.LocalDateTime;
import java.util.Collection;

public interface CollectionHandler<T> {

    /**
     * Retrieves the collection of objects from the database.
     *
     * @return The collection of objects.
     */
    Collection<T> getCollection();

    /**
     * Retrieves the initialization time of the database.
     *
     * @return The initialization time as a LocalDateTime object.
     */
    LocalDateTime getInitializationDate();

    /**
     * Sorts the collection of objects.
     */
    void sortCollection();

    /**
     * Clears the collection of objects in the database.
     */
    void clearCollection();

    /**
     * Adds the specified object to the collection in the database.
     *
     * @param item The object to be added.
     */
    void addElement(T item);

    /**
     * Removes the object from the collection in the database based on the specified ID.
     *
     * @param id The ID of the object to be removed.
     * @return true if the element was removed successfully, false if no matching ID was found.
     */
    boolean removeElement(int id);
}
