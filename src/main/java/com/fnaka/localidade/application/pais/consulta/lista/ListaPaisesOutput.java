package com.fnaka.localidade.application.pais.consulta.lista;

import com.fnaka.localidade.domain.pais.Pais;

import java.time.Instant;

public record ListaPaisesOutput(
        String id,
        String nome,
        boolean isAtivo,
        Instant criadoEm,
        Instant excluidoEm
) {

    public static ListaPaisesOutput from(final Pais umPais) {
        return new ListaPaisesOutput(
                umPais.getId().getValue(),
                umPais.getNome(),
                umPais.isAtivo(),
                umPais.getCriadoEm(),
                umPais.getExcluidoEm()
        );
    }
}
