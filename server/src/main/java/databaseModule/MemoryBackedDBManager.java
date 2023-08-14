package databaseModule;

import databaseModule.handler.PersonCollectionHandler;
import databaseModule.loader.PersonCollectionLoader;
import databaseModule.repository.PersonRepositoryImpl;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;

/**
 * The MemoryBackedDBManager class provides a mechanism for managing data elements in memory
 * while synchronizing with a database backend. It allows for efficient access to frequently
 * accessed data by maintaining a synchronized representation in memory, reducing the need for
 * repeated database queries.
 * <p>
 * This class is designed to be thread-safe, allowing multiple threads to access and modify data
 * without risking data integrity issues.
 */
public class MemoryBackedDBManager {
    private static final Logger logger = LogManager.getLogger("logger.MemoryBackedDBManager");
    private static MemoryBackedDBManager singleInstance;

    /**
     * Returns the singleton instance of the MemoryBackedDBManager class. The singleton pattern
     * ensures that only one instance of the MemoryBackedDBManager is created and shared across
     * the application.
     * <p>
     * If an instance does not yet exist, this method creates a new instance and returns it.
     * Subsequent calls to this method will return the already existing instance.
     * <p>
     * This method guarantees that the MemoryBackedDBManager is a singleton, meaning that all
     * parts of the application that obtain a reference to the instance will work with the same
     * shared object.
     * <p>
     * Usage Example:
     * <pre>{@code
     * MemoryBackedDBManager dbManager = MemoryBackedDBManager.getInstance();
     * // Use the singleton instance for data management operations.
     * }</pre>
     *
     * @return The singleton instance of the MemoryBackedDBManager class.
     */
    public static MemoryBackedDBManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new MemoryBackedDBManager();
        }
        return singleInstance;
    }

    /**
     * Adds a new element to the database and memory-backed collection, subject to exception handling.
     *
     * @param person  The Person object to be added
     * @param ownerId The ID of the owner adding the element
     * @return {@code true} if the element was successfully added to both the database and collection
     *         {@code false} if the element could not be added due to an exception during the addition process
     */
    public synchronized boolean addElementToDBAndMemory(Person person, int ownerId) {
        try (PersonRepositoryImpl personRepository = new PersonRepositoryImpl()) {
            if (personRepository.insert(person, ownerId)) {
                PersonCollectionHandler.getInstance().addElement(person);
                return true;
            }
        } catch (SQLException | IOException e) {
            logger.error("Element was not added", e);
        }

        return false;
    }

    /**
     * Removes an element from the database and memory-backed collection, subject to access and exception handling.
     *
     * @param elementId The ID of the element to be removed
     * @param ownerId   The ID of the owner attempting to remove the element
     * @return An integer code representing the removal result:
     *         - 1 if the element was successfully removed
     *         - 0 if the element was not removed due to an exception during the removal process
     *         - 2 if the element was not removed due to access permission failure
     */
    public synchronized int removeElementFromDBAndMemory(int elementId, int ownerId) {
        try (PersonRepositoryImpl personRepository = new PersonRepositoryImpl()) {
            if (!personRepository.checkAccess(elementId, ownerId)) {
                return 2;
            }

            if (personRepository.remove(elementId)) {
                PersonCollectionHandler.getInstance().removeElement(elementId);
                return 1;
            }
        } catch (SQLException | IOException e) {
            logger.error("Element was not removed", e);
        }

        return 0;
    }

    /**
     * Updates a given Person object in the database and corresponding in-memory collection.
     * <p>
     * This method updates a Person object in the database using the provided PersonRepositoryImpl.
     * It also maintains synchronization between the database and an in-memory collection managed
     * by PersonCollectionHandler. The access control is verified before updating, and the updated
     * Person object is replaced in the in-memory collection.
     *
     * @param person   The Person object containing updated information
     * @param elementId The ID of the element (person) to be updated
     * @param ownerId   The ID of the owner requesting the update
     * @return An integer code indicating the result of the update operation:
     *         - 2: Access failure, the user does not have the required access
     *         - 1: The element (person) was successfully updated
     *         - 0: An exception occurred during the update process
     * @throws IOException If the in-memory collection elements do not match the database elements
     * @see Person
     * @see PersonRepositoryImpl
     * @see PersonCollectionHandler
     */
    public synchronized int updateElementInDBAndMemory(Person person, int elementId, int ownerId) throws IOException {
        try (PersonRepositoryImpl personRepository = new PersonRepositoryImpl()) {
            if (!personRepository.checkAccess(elementId, ownerId)) {
                return 2;
            }

            personRepository.update(person, elementId);

            PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
            Optional<Person> optionalPerson = personCollectionHandler.getCollection()
                    .stream().filter(p -> Objects.equals(p.getId(), elementId)).findFirst();

            if (optionalPerson.isPresent()) {
                Person existingPerson = optionalPerson.get();
                personCollectionHandler.getCollection().remove(existingPerson);
                person.setId(elementId);
                personCollectionHandler.addElement(person);
            } else {
                throw new IOException("Collection elements do not match database elements");
            }

            return 1;
        } catch (SQLException e) {
            logger.error("Element was not updated", e);
        }

        return 0;
    }

    /**
     * Clears elements owned by a specified user from both the database and memory.
     * The method removes elements associated with the provided ownerId from the database
     * and also clears them from memory using a collection handler. It returns status codes
     * to indicate the outcome of the operation.
     *
     * @param ownerId The ID of the owner whose elements need to be cleared.
     * @return An integer status code indicating the result of the operation:
     *         <ul>
     *             <li>{@code -1} - Not all elements owned by the specified user were removed</li>
     *             <li>{@code 0} - An exception occurred while performing the operation</li>
     *             <li>{@code 1} - All elements owned by the specified user were successfully removed</li>
     *             <li>{@code 2} - The specified user has not created any elements</li>
     *         </ul>
     */
    public synchronized int clearElementsInDBAndMemory(int ownerId) {
        List<Person> ownerElements = new ArrayList<>();
        int elementsSize = 0;
        try {
            ownerElements = this.getOwnerElements(ownerId, ArrayList::new);
            if (ownerElements.isEmpty()) {
                return 2;
            } else {
                int elementId;
                int counter = 0;
                elementsSize = ownerElements.size();

                try (PersonRepositoryImpl personRepository = new PersonRepositoryImpl()) {
                    for (Iterator<Person> iterator = ownerElements.iterator(); iterator.hasNext(); ) {
                        elementId = iterator.next().getId();

                        personRepository.remove(elementId);
                        PersonCollectionHandler.getInstance().removeElement(elementId);

                        counter++;
                        iterator.remove();
                    }
                }

                if (counter == elementsSize) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } catch (SQLException | IOException e) {
            if (elementsSize > ownerElements.size()) {
                logger.error("Some elements were not removed", e);
                return -1;
            } else {
                logger.error("Elements were not removed", e);
            }
        }

        return 0;
    }

    /**
     * Retrieves a collection of Person elements owned by a specific owner ID from a database.
     *
     * @param ownerId          The ID of the owner for whom to retrieve the elements
     * @param collectionSupplier A supplier function that provides a new instance of the desired collection type
     * @param <C>              The type of collection to return
     * @return A collection of Person elements owned by the specified owner ID
     * @throws SQLException If a database access error occurs
     * @throws IOException  If an I/O error occurs
     */
    public <C extends Collection<Person>> C getOwnerElements(int ownerId, Supplier<C> collectionSupplier) throws SQLException, IOException {
        try (PersonCollectionLoader<C> collectionLoader = new PersonCollectionLoader<>(collectionSupplier.get())) {
            collectionLoader.loadElementsFromDB(ownerId);
            return collectionLoader.getCollection();
        }
    }

    /**
     * Checks the existence of an element identified by the given element ID
     * within the data source. This method queries a repository to determine
     * if an element with the specified ID exists.
     *
     * @param elementId The ID of the element to check for existence
     * @return {@code true} if an element with the specified ID exists,
     *         {@code false} otherwise
     * @throws SQLException If a database access error occurs while querying
     *                      the repository for element existence
     * @throws IOException If an I/O error occurs while establishing the connection
     *                     to the data source, which could include reading data
     *                     from a file
     */
    public boolean checkElementExistence(int elementId) throws SQLException, IOException {
        try (PersonRepositoryImpl personRepository = new PersonRepositoryImpl()) {
            return personRepository.read(elementId) != null;
        }
    }
}
