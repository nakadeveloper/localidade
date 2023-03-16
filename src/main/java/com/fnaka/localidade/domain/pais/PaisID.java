package com.fnaka.localidade.domain.pais;


import com.fnaka.localidade.domain.Identifier;
import com.fnaka.localidade.domain.utils.IdUtils;

import java.util.Objects;

public class PaisID extends Identifier {

    private final String value;

    private PaisID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static PaisID from(final String umId) {
        return new PaisID(umId);
    }

    public static PaisID unique() {
        return PaisID.from(IdUtils.uuid());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PaisID paisID = (PaisID) o;
        return getValue().equals(paisID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
