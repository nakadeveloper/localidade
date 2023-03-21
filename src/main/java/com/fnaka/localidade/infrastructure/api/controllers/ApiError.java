package com.fnaka.localidade.infrastructure.api.controllers;

import com.fnaka.localidade.domain.exceptions.DomainException;
import com.fnaka.localidade.domain.validation.Error;

import java.util.List;

public record ApiError(String message, List<Error> errors) {

    public static ApiError from(final DomainException ex) {
        return new ApiError(ex.getMessage(), ex.getErrors());
    }
}
