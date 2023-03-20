package com.fnaka.localidade.infrastructure.pais.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AtualizaPaisRequest(
        @JsonProperty("nome") String nome,
        @JsonProperty("ativo") Boolean ativo
) {
}
