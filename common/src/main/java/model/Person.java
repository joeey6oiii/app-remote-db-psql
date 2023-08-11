package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * The class on which the collection in the program is based. Contains getters and setters for each class field.
 * <p>
 * Some fields have restrictions.
 */
public class Person implements Buildable, Comparable<Person>, Serializable {
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private int height;
    private Date birthday;
    private String passportID;
    private Color hairColor;
    private Location location;

    /**
     * Restrictions: field can not be null, value of the field must be greater than zero, value of the field must be unique,
     * value of the field must be generated automatically.
     *
     * @return the id of the person on which the method is called
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the specified value to the field id.
     * <p>
     * Restrictions: field can not be null, value of the field must be greater than zero, value of the field must be unique,
     * value of the field must be generated automatically.
     *
     * @param id the new value of the field id
     */
    public void setId(Integer id) { this.id = id; }

    /**
     * Restrictions: field can not be null, string can not be empty
     *
     * @return the name of the person on which the method is called
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the specified value to the field name.
     * <p>
     * Restrictions: field can not be null, string can not be empty
     *
     * @param name the new value of the field name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Restrictions: field can not be null
     *
     * @return the coordinates of the person on which the method is called
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the specified value to the field coordinates.
     * <p>
     * Restrictions: field can not be null
     *
     * @param coordinates the new value of the field coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return the creationDate of the person on which the method is called
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Restrictions: value of the field must be greater than zero
     *
     * @return the height of the person on which the method is called
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the specified value to the field height.
     * <p>
     * Restrictions: value of the field must be greater than zero
     *
     * @param height the new value of the field height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Restrictions: field can not be null
     *
     * @return the birthday of the person on which the method is called
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Sets the specified value to the field birthday.
     * <p>
     * Restrictions: field can not be null
     *
     * @param birthday the new value of the field birthday
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * Restrictions: field can not be null, value must be equal to or greater than five
     *
     * @return the passportID of the person on which the method is called
     */
    public String getPassportId() {
        return passportID;
    }

    /**
     * Sets the specified value to the field passportID.
     * <p>
     * Restrictions: field can not be null, value must be equal to or greater than five
     *
     * @param passportID the new value of the field passportID
     */
    public void setPassportId(String passportID) {
        this.passportID = passportID;
    }

    /**
     * @return the hairColor of the person on which the method is called
     */
    public Color getHairColor() {
        return hairColor;
    }

    /**
     * Sets the specified value to the field hairColor.
     *
     * @param hairColor the new value of the field hairColor
     */
    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * @return the location of the person on which the method is called
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the specified value to the field location.
     *
     * @param location the new value of the field location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @param anotherPerson the object to be compared.
     * @return difference between caller object id and compared object id
     */
    public int compareTo(Person anotherPerson){
        return this.id - anotherPerson.id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", height=" + height +
                ", birthday=" + birthday +
                ", passportID='" + passportID + '\'' +
                ", hairColor=" + hairColor +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id)
                && Objects.equals(name, person.name)
                && Objects.equals(coordinates, person.coordinates)
                && Objects.equals(creationDate, person.creationDate)
                && Objects.equals(height, person.height)
                && Objects.equals(birthday, person.birthday)
                && Objects.equals(passportID, person.passportID)
                && hairColor == person.hairColor
                && Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, height, birthday, passportID, hairColor, location);
    }
}