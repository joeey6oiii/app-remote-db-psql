package defaultClasses;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class whose object is used in the another class. Contains getters and setters for each class field.
 * <p>
 * Fields have no restrictions.
 */

public class Coordinates implements isBuildable, Serializable {
    private long x;
    private int y;

    /**
     * Creates new Coordinates.
     */

    public Coordinates() {}

    /**
     * Creates new Coordinates with the specified X and Y parameters.
     *
     * @param x x coordinate for coordinates
     * @param y y coordinate for coordinates
     */

    public Coordinates(long x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the value of the field X
     */

    public long getX() {
        return x;
    }

    /**
     * Sets the specified value to the field X.
     *
     * @param x the new value of the field X
     */

    public void setX(long x) {
        this.x = x;
    }

    /**
     * @return the value of the field Y
     */

    public int getY() {
        return y;
    }

    /**
     * Sets the specified value to the field Y.
     *
     * @param y the new value of the field Y
     */

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
