package org.mihok.csv.parser.exception;

import org.mihok.csv.parser.core.RowError;

import java.util.List;

public class ParsefyException extends Exception {
    private final List<RowError> errors;

    public ParsefyException(String message, List<RowError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<RowError> getErrors() {
        return errors;
    }
}