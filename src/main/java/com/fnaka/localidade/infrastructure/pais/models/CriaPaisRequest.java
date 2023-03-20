package com.fnaka.localidade.infrastructure.pais.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CriaPaisRequest(
        @JsonProperty("nome") String nome,
        @JsonProperty("ativo") Boolean ativo
) {
}
