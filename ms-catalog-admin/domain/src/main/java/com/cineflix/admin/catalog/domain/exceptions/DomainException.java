package com.cineflix.admin.catalog.domain.exceptions;

import java.util.List;
import com.cineflix.admin.catalog.domain.validation.Error;

public class DomainException extends NoStacktraceException {
    private List<Error> errors;

    private DomainException(String message, List<Error> errors) {
        super("");
        this.errors = errors;
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    public static DomainException with(final Error error) {
        return new DomainException("", List.of(error));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
