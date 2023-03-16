package com.fnaka.localidade.application.pais.atualiza;

import com.fnaka.localidade.domain.pais.Pais;

public record AtualizaPaisOutput(String id) {

    public static AtualizaPaisOutput from(final Pais aPais) {
        return new AtualizaPaisOutput(aPais.getId().getValue());
    }
}
