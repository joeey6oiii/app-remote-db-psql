package validators;

import java.util.Date;

/**
 * A class that implements a validator for the birthday field.
 */

public class BirthdayValidator implements ValidateAble<Date> {

    /**
     * @param birthday {@link Date} to validate
     * @return true if birthday ({@link Date}) is not null
     */

    @Override
    public boolean validate(Date birthday) {
        return birthday != null;
    }
}
