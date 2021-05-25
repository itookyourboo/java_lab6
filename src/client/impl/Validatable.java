package client.impl;

import client.gui.FieldValidator;

public interface Validatable {
    FieldValidator.ValidationResult validate(String input);
}
