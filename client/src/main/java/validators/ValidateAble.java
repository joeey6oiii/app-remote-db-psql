package validators;

/**
 * An interface to be implemented by a validator.
 *
 * @param <T> validation value type
 */

@FunctionalInterface
public interface ValidateAble<T> {

    /**
     * Provides a validation method.
     * <p>
     * Used to check if the specified value is correct.
     *
     * @param value value to validate
     * @return true if all validation conditions are met, false otherwise
     */

    boolean validate(T value);
}
