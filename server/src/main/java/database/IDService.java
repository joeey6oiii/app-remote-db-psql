package database;

import defaultClasses.Person;

/**
 * A class that works with the {@link Person} object id.
 */

public class IDService {
    private static Integer maxId;

    /**
     * A method that recalculates {@link Person} object id by the specified Person object.
     *
     * @param person the person whose id needs to be recalculated
     */

    public static Person recalculateId(Person person) {
        maxId += 1;
        person.setId(maxId);

        return person;
    }

    /**
     * A method to set max id to the maxId field.
     *
     * @param id max id of an element in the database
     */

    protected static void setMaxId(Integer id) {
        maxId = id;
    }

}
