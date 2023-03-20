package com.fnaka.localidade.infrastructure.pais.models;

import java.time.Instant;

public record ListaPaisesResponse(
        String id,
        String nome,
        boolean ativo,
        Instant criadoEm,
        Instant excluidoEm
) {
}
