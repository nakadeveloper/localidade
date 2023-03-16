package com.fnaka.localidade.domain.exceptions;

import com.fnaka.localidade.domain.validation.Error;

import java.util.List;


public class DomainException extends NoStacktraceException {

    protected final List<Error> errors;

    public DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public List<Error> getErrors() {
        return errors;
    }

    public Error getFirstError() {
        if (errors == null || errors.isEmpty()) {
            return null;
        }
        return errors.get(0);
    }
}
