package validators;

import defaultClasses.Color;

/**
 * A class that implements a validator for the hairColor field
 */

public class HairColorValidator implements ValidateAble<Color> {

    /**
     * @param color color to validate
     * @return true if the color is null or the {@link Color} enum contains specified color, false otherwise
     */

    @Override
    public boolean validate(Color color) {
        return color == null || Color.listValues().contains(color);
    }
}
