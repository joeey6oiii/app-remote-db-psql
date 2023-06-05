package validators;

import defaultClasses.Location;

import java.util.Objects;

/**
 * A class that implements a validator for the location field.
 */

public class LocationValidator implements ValidateAble<Location> {

    /**
     * @param location location to validate
     * @return true if the location is null or its name is not an empty string and its X and Y coordinates are not null,
     * false otherwise
     */

    @Override
    public boolean validate(Location location) {
        return location == null || (location.getX() != null && location.getY() != null && !Objects.equals(location.getName(), ""));
    }
}