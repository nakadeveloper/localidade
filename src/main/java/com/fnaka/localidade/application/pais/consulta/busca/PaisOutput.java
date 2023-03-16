package com.fnaka.localidade.application.pais.consulta.busca;

import com.fnaka.localidade.domain.pais.Pais;

import java.time.Instant;

public record PaisOutput(
        String id,
        String nome,
        boolean isAtivo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {

    public static PaisOutput from(final Pais umPais) {
        return new PaisOutput(
                umPais.getId().getValue(),
                umPais.getNome(),
                umPais.isAtivo(),
                umPais.getCriadoEm(),
                umPais.getAtualizadoEm(),
                umPais.getExcluidoEm()
        );
    }
}
