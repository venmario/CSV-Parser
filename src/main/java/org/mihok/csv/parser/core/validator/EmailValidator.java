package org.mihok.csv.parser.core.validator;

import org.mihok.csv.parser.validation.ValidationResult;

public class EmailValidator implements FieldValidator<String> {
    private final String message;

    public EmailValidator(String message) {
        this.message = message;
    }

    @Override
    public ValidationResult validate(String value) {
        if (value != null && !value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return ValidationResult.error(message);
        }
        return ValidationResult.success();
    }
}