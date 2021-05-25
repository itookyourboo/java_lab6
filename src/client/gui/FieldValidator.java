package client.gui;

import client.impl.Validatable;
import client.util.LocaleManager;
import client.util.Validator;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInBoundsException;
import common.model.FormOfEducation;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class FieldValidator {

    public enum Field {
        ID,
        GROUP_NAME,
        COORDINATE_X,
        COORDINATE_Y,
        STUDENTS_COUNT,
        EXPELLED_STUDENTS,
        SHOULD_BE_EXPELLED,
        ADMIN_NAME,
        ADMIN_WEIGHT,
        ADMIN_PASSPORT,
        LOCATION_X,
        LOCATION_Y,
        LOCATION_NAME,
        FORM_OF_EDUCATION
    }

    public static class ValidationResult<T> {
        public final boolean isCorrect;
        public final String message;
        public final T result;

        public ValidationResult(boolean isCorrect, String message, T result) {
            this.isCorrect = isCorrect;
            this.message = message;
            this.result = result;
        }
    }

    public static ValidationResult<String> validateLocationName(String s) {
        s = s.trim();
        try {
            return new ValidationResult<>(true, "", Validator.validateLocationName(s));
        } catch (NotInBoundsException e) {
            return stringBounds(Field.LOCATION_NAME, e);
        } catch (MustBeNotEmptyException e) {
            return getMustBeNotEmptyTemplate(Field.LOCATION_NAME);
        }
    }

    public static ValidationResult<Integer> validateLocationY(String s) {
        s = s.trim();
        try {
            Integer y = Validator.validateLocationY(s);
            return new ValidationResult<>(true, "", y);
        } catch (NotInBoundsException e) {
            return integerBounds(Field.LOCATION_Y, e);
        } catch (NumberFormatException e) {
            return integerFormat(Field.LOCATION_Y);
        }
    }

    public static ValidationResult<Integer> validateLocationX(String s) {
        s = s.trim();
        try {
            Integer x = Validator.validateLocationX(s);
            return new ValidationResult<>(true, "", x);
        } catch (NotInBoundsException e) {
            return integerBounds(Field.LOCATION_X, e);
        } catch (NumberFormatException e) {
            return integerFormat(Field.LOCATION_X);
        }
    }

    public static ValidationResult<Long> validateAdminWeight(String s) {
        s = s.trim();
        try {
            Long weight = Validator.validatePersonWeight(s);
            return new ValidationResult<>(true, "", weight);
        } catch (NotInBoundsException e) {
            return longBounds(Field.ADMIN_WEIGHT, e);
        } catch (NumberFormatException e) {
            return longFormat(Field.ADMIN_WEIGHT);
        }
    }

    public static ValidationResult<String> validateAdminPassport(String s) {
        s = s.trim();
        try {
            return new ValidationResult<>(true, "", Validator.validatePassportID(s));
        } catch (NotInBoundsException e) {
            return stringBounds(Field.ADMIN_PASSPORT, e);
        } catch (MustBeNotEmptyException e) {
            return getMustBeNotEmptyTemplate(Field.ADMIN_PASSPORT);
        }
    }

    public static ValidationResult<String> validateAdminName(String s) {
        s = s.trim();
        try {
            return new ValidationResult<>(true, "", Validator.validatePersonName(s));
        } catch (NotInBoundsException e) {
            return stringBounds(Field.ADMIN_NAME, e);
        } catch (MustBeNotEmptyException e) {
            return getMustBeNotEmptyTemplate(Field.ADMIN_NAME);
        }
    }

    public static ValidationResult<FormOfEducation> validateFormOfEducation(String s) {
        s = s.trim();
        try {
            FormOfEducation formOfEducation = FormOfEducation.valueOf(s);
            return new ValidationResult<>(true, "", formOfEducation);
        } catch (Exception e) {
            return new ValidationResult<>(false, LocaleManager.getString("noSuchFormOfEducation"), null);
        }
    }

    public static ValidationResult<Integer> validateShouldBeExpelled(String s) {
        s = s.trim();
        try {
            Integer count = Validator.validateShouldBeExpelled(s);
            return new ValidationResult<>(true, "", count);
        } catch (NotInBoundsException e) {
            return integerBounds(Field.SHOULD_BE_EXPELLED, e);
        } catch (NumberFormatException e) {
            return integerFormat(Field.SHOULD_BE_EXPELLED);
        }
    }

    public static ValidationResult<Long> validateExpelledStudents(String s) {
        s = s.trim();
        try {
            Long count = Validator.validateExpelledStudents(s);
            return new ValidationResult<>(true, "", count);
        } catch (NotInBoundsException e) {
            return longBounds(Field.EXPELLED_STUDENTS, e);
        } catch (NumberFormatException e) {
            return longFormat(Field.EXPELLED_STUDENTS);
        }
    }

    public static ValidationResult<Integer> validateStudentsCount(String s) {
        s = s.trim();
        try {
            Integer count = Validator.validateStudentsCount(s);
            return new ValidationResult<>(true, "", count);
        } catch (NotInBoundsException e) {
            return integerBounds(Field.STUDENTS_COUNT, e);
        } catch (NumberFormatException e) {
            return integerFormat(Field.STUDENTS_COUNT);
        }
    }

    public static ValidationResult<Long> validateCoordinateY(String s) {
        s = s.trim();
        try {
            Long y = Validator.validateCoordinatesY(s);
            return new ValidationResult<>(true, "", y);
        } catch (NotInBoundsException e) {
            return longBounds(Field.COORDINATE_Y, e);
        } catch (NumberFormatException e) {
            return longFormat(Field.COORDINATE_Y);
        }
    }

    public static ValidationResult<Integer> validateCoordinateX(String s) {
        s = s.trim();
        try {
            Integer x = Validator.validateCoordinatesX(s);
            return new ValidationResult<>(true, "", x);
        } catch (NotInBoundsException e) {
            return integerBounds(Field.COORDINATE_X, e);
        } catch (NumberFormatException e) {
            return integerFormat(Field.COORDINATE_X);
        }
    }

    public static ValidationResult<String> validateGroupName(String s) {
        s = s.trim();
        try {
            return new ValidationResult<>(true, "", Validator.validateGroupName(s));
        } catch (NotInBoundsException e) {
            return stringBounds(Field.GROUP_NAME, e);
        } catch (MustBeNotEmptyException e) {
            return getMustBeNotEmptyTemplate(Field.GROUP_NAME);
        }
    }

    public static ValidationResult<Integer> validateID(String s) {
        s = s.trim();
        try {
            Integer id = Integer.parseInt(s);
            return new ValidationResult<>(true, "", id);
        } catch (NumberFormatException exception) {
            return integerFormat(Field.ID);
        }
    }

    private static ValidationResult<String> stringBounds(Field field, Exception exception) {
        return new ValidationResult<>(false,
                generateMessage(
                        field,
                        LocaleManager.getString("stringBounds") + " " + exception.getMessage()),
                null);
    }

    private static ValidationResult<Integer> integerBounds(Field field, Exception exception) {
        return new ValidationResult<Integer>(false,
                generateMessage(
                        field,
                        LocaleManager.getString("numberBounds") + " " + exception.getMessage()),
                null);
    }

    private static ValidationResult<Long> longBounds(Field field, Exception exception) {
        return new ValidationResult<Long>(false,
                generateMessage(
                        field,
                        LocaleManager.getString("numberBounds") + " " + exception.getMessage()),
                null);
    }

    private static ValidationResult<String> getMustBeNotEmptyTemplate(Field field) {
        return new ValidationResult<>(false,
                generateMessage(
                        field,
                        LocaleManager.getString("mustBeNotEmptyException")),
                null);
    }

    private static ValidationResult<Integer> integerFormat(Field field) {
        return new ValidationResult<Integer>(false,
                generateMessage(
                        field,
                        LocaleManager.getString("numberFormatException")),
                null
        );
    }

    private static ValidationResult<Long> longFormat(Field field) {
        return new ValidationResult<Long>(false,
                generateMessage(
                        field,
                        LocaleManager.getString("numberFormatException")),
                null
        );
    }

    private static String generateMessage(Field field, String exception) {
        return String.format("%s: %s", field.toString(), exception);
    }
}
