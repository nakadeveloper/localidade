package com.fnaka.localidade.domain.exceptions;

import com.fnaka.localidade.domain.validation.Error;
import com.fnaka.localidade.domain.AggregateRoot;
import com.fnaka.localidade.domain.Identifier;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    public NotFoundException(String aMessage, List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
    ) {
        final var anError = "%s with ID %s was not found".formatted(anAggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anError, Collections.emptyList());
    }
}
