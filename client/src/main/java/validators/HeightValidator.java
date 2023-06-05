package validators;

/**
 * A class that implements a validator for the height field.
 */

public class HeightValidator implements ValidateAble<Integer> {

    /**
     * @param height height to validate
     * @return true if the height is greater than zero
     */

    @Override
    public boolean validate(Integer height) {
        return height > 0;
    }
}
