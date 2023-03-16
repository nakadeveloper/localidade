package com.fnaka.localidade.application.pais.cria;

import com.fnaka.localidade.domain.pais.Pais;

public record CriaPaisOutput(
        String id
) {

    public static CriaPaisOutput from(final String anId) {
        return new CriaPaisOutput(anId);
    }

    public static CriaPaisOutput from(final Pais umPais) {
        return from(umPais.getId().getValue());
    }
}
