package database;

import defaultClasses.Person;

import java.util.List;

/**
 * A class that provides loading service.
 */

public class LoadService {

    /**
     * A method that loads the collection to the database.
     * Sets to the {@link IDService} max id found.
     *
     * @param list collection to load
     * @param database where to load to
     */

    public void loadToDatabase(List<Person> list, Database database) {
        list.forEach(database::add);

        Integer maxId = list.stream()
                .map(Person::getId)
                .max(Integer::compareTo)
                .orElse(1);
        IDService.setMaxId(maxId);
    }

}
