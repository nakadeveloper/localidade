package com.fnaka.localidade.infrastructure.pais.models;

import java.time.Instant;

public record PaisResponse(
        String id,
        String nome,
        boolean ativo,
        Instant criadoEm,
        Instant atualizadoEm,
        Instant excluidoEm
) {
}
