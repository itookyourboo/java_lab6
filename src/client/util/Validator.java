package client.util;

import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInBoundsException;
import common.model.FormOfEducation;

import java.util.regex.Pattern;

public class Validator {

    private static Pattern patternNumber = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static Pattern patternUsername = Pattern.compile("[_0-9A-Za-z]{3,12}");
    private static Pattern patternPassword = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[_0-9a-zA-Z!@#$%^&*]{6,30}");

    public static boolean validateUsername(String username) {
        return patternUsername.matcher(username).matches();
    }

    public static boolean validatePassword(String password) {
        return patternPassword.matcher(password).matches();
    }

    public static String validateGroupName(String groupName) throws NotInBoundsException, MustBeNotEmptyException {
        return validateName(groupName, 1, Integer.MAX_VALUE);
    }

    public static String validatePersonName(String personName) throws NotInBoundsException, MustBeNotEmptyException {
        return validateName(personName, 1, Integer.MAX_VALUE);
    }

    public static String validateLocationName(String locationName) throws NotInBoundsException, MustBeNotEmptyException {
        return validateName(locationName, 1, 272);
    }

    public static Integer validateCoordinatesX(String x) throws NotInBoundsException, NumberFormatException {
        return validateInteger(x, -512, Integer.MAX_VALUE);
    }

    public static Long validateCoordinatesY(String y) throws NotInBoundsException, NumberFormatException {
        return validateLong(y, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static Integer validateStudentsCount(String count) throws NotInBoundsException, NumberFormatException {
        return validateInteger(count, 0, Integer.MAX_VALUE);
    }

    public static Long validateExpelledStudents(String count) throws NotInBoundsException, NumberFormatException {
        return validateLong(count, 0, Long.MAX_VALUE);
    }

    public static Integer validateShouldBeExpelled(String count) throws NotInBoundsException, NumberFormatException {
        return validateInteger(count, 0, Integer.MAX_VALUE);
    }

    public static FormOfEducation validateFormOfEducation(String form) throws Exception {
        return FormOfEducation.valueOf(form.toUpperCase());
    }

    public static Long validatePersonWeight(String weight) throws NotInBoundsException, NumberFormatException {
        return validateLong(weight, 0, Long.MAX_VALUE);
    }

    public static String validatePassportID(String passportID) throws NotInBoundsException, MustBeNotEmptyException {
        return validateName(passportID, 3, Integer.MAX_VALUE);
    }

    public static Integer validateLocationX(String x) throws NotInBoundsException, NumberFormatException {
        return validateInteger(x, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static Integer validateLocationY(String y) throws NotInBoundsException, NumberFormatException{
        return validateInteger(y, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static String validateName(String name, int minLength, int maxLength) throws MustBeNotEmptyException, NotInBoundsException {
        if (name.trim().equals("")) throw new MustBeNotEmptyException();
        if (name.length() < minLength) throw new NotInBoundsException(String.format("(%d; %d)", minLength, maxLength));
        if (name.length() > maxLength) throw new NotInBoundsException(String.format("(%d; %d)", minLength, maxLength));
        return name;
    }

    private static Integer validateInteger(String number, int min, int max) throws NotInBoundsException, NumberFormatException {
        number = number.trim();
        int result;
        try {
            result = Integer.parseInt(number);
            if (result < min) throw new NotInBoundsException(String.format("(%d; %d)", min, max));
            if (result > max) throw new NotInBoundsException(String.format("(%d; %d)", min, max));
            return result;
        } catch (NumberFormatException e) {
            if (patternNumber.matcher(number).matches())
                throw new NotInBoundsException(String.format("(%d; %d)", min, max));
            throw e;
        }
    }

    private static Long validateLong(String number, long min, long max) throws NotInBoundsException, NumberFormatException {
        number = number.trim();
        long result;
        try {
            result = Long.parseLong(number);
            if (result < min) throw new NotInBoundsException(String.format("(%d; %d)", min, max));
            if (result > max) throw new NotInBoundsException(String.format("(%d; %d)", min, max));
            return result;
        } catch (NumberFormatException e) {
            if (patternNumber.matcher(number).matches())
                throw new NotInBoundsException(String.format("(%d; %d)", min, max));
            throw new NumberFormatException();
        }
    }
}
