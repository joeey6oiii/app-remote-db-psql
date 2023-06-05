package validators;

import defaultClasses.Person;

/**
 * A class that implements full validation of the {@link Person} object.
 *
 * @see ValidateAble
 * @see NameValidator
 * @see HairColorValidator
 * @see CoordinatesValidator
 * @see HeightValidator
 * @see BirthdayValidator
 * @see PassportIDValidator
 * @see LocationValidator
 */

public class PersonValidator implements ValidateAble<Person> {

    @Override
    public boolean validate(Person person) {
        return person.getId() > 0 && person.getCreationDate() != null
                && new NameValidator().validate(person.getName())
                && new HairColorValidator().validate(person.getHairColor())
                && new CoordinatesValidator().validate(person.getCoordinates())
                && new HeightValidator().validate(person.getHeight())
                && new BirthdayValidator().validate(person.getBirthday())
                && new PassportIDValidator().validate(person.getPassportID())
                && new LocationValidator().validate(person.getLocation());
    }
}