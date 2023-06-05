package defaultClasses;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class whose object is used in the another class. Contains getters and setters for each class field.
 * <p>
 * Some fields have restrictions.
 */

public class Location implements isBuildable, Serializable {
    private Float x;
    private Integer y;
    private String name;

    /**
     * Creates a new Location.
     */

    public Location() {}

    /**
     * Creates a new location with the specified parameters.
     *
     * @param x the field x of the location (not null)
     * @param y the field y of the location (not null)
     * @param name the field name of the location (not null or empty string)
     */

    public Location(Float x, Integer y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Restrictions: field can not be null
     *
     * @return the value of the field X
     */

    public Float getX() {
        return x;
    }

    /**
     * Sets the specified value to the field X.
     * <p>
     * Restrictions: field can not be null
     *
     * @param x the new value of the field X
     */

    public void setX(Float x) {
        this.x = x;
    }

    /**
     * Restrictions: field can not be null
     *
     * @return the value of the field Y
     */

    public Integer getY() {
        return y;
    }

    /**
     * Sets the specified value to the field Y.
     * <p>
     * Restrictions: field can not be null
     * @param y the new value of the field Y
     */

    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * Restrictions: string can not be empty
     *
     * @return the value of the field name
     */

    public String getName() {
        return name;
    }

    /**
     * Sets the specified value to the field name.
     * <p>
     * Restrictions: string can not be empty
     * @param name the new value of the field name
     */

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(x, location.x) && Objects.equals(y, location.y)
                && Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }
}