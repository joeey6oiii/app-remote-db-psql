package validators;

/**
 * A class that implements a validator for the passportID field.
 */

public class PassportIDValidator implements ValidateAble<String> {

    /**
     * @param passportID passportID to validate
     * @return true if passportID is not null and its length equals or is greater than 5, false otherwise
     */

    @Override
    public boolean validate(String passportID) {
        return passportID != null && passportID.length() >= 5;
    }
}
