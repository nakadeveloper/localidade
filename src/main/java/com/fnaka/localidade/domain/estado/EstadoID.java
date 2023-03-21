package com.fnaka.localidade.domain.estado;

import com.fnaka.localidade.domain.Identifier;
import com.fnaka.localidade.domain.utils.IdUtils;

import java.util.Objects;

public class EstadoID extends Identifier {

    private final String value;

    public EstadoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static EstadoID from(final String anId) {
        return new EstadoID(anId);
    }

    public static EstadoID unique() {
        return EstadoID.from(IdUtils.uuid());
    }

    @Override
    public String getValue() {
        return value;
    }
}
