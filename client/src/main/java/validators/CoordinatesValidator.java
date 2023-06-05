package validators;

import defaultClasses.Coordinates;

/**
 * A class that implements a validator for the coordinates field.
 */

public class CoordinatesValidator implements ValidateAble<Coordinates> {

    /**
     * @param coordinates coordinates to validate
     * @return true if the coordinates (field) is not null
     */

    @Override
    public boolean validate(Coordinates coordinates) {
        return coordinates != null;
    }
}
