package comparators;

import defaultClasses.Person;

import java.util.Comparator;

/**
 * A class to compare {@link Person} objects by height.
 *
 * @author Dmitrii Chebanenko
 */
public class HeightComparator implements Comparator<Person> {

    /**
     * A method that compares two specified objects.
     *
     * @param p1 the first object to be compared.
     * @param p2 the second object to be compared.
     * @return -1, 0, 1
     */

    public int compare(Person p1, Person p2){
        return Integer.compare(p1.getHeight(), p2.getHeight());
    }
}
