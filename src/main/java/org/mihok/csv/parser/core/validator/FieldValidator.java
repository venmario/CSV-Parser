package org.mihok.csv.parser.core.validator;

import org.mihok.csv.parser.validation.ValidationResult;

public interface FieldValidator<T> {
    ValidationResult validate(T value);
}
