package validators;

/**
 * A class that implements a validator for the name field.
 */

public class NameValidator implements ValidateAble<String> {

    /**
     * @param name name to validate
     * @return true if name is not null and not equal to the empty string
     */

    @Override
    public boolean validate(String name) {
        return !(name == null || name.equals(""));
    }
}
