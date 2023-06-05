package utils;

/**
 * A class with help parse method.
 */

public class StringToDateParser {

    /**
     * A method that pareses {@link String} to a {@link java.util.Date}
     *
     * @param str <code>String</code> representation of a date
     * @return {@link java.util.Date} parsed from {@link String}
     * @throws java.text.ParseException if the format of the specified <code>String</code> date is incorrect
     */

    public static java.util.Date parse(String str) throws java.text.ParseException {
        java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.parse(str);
    }
}
