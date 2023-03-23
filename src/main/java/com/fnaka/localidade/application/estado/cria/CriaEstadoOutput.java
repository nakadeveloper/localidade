package com.fnaka.localidade.application.estado.cria;

import com.fnaka.localidade.domain.estado.Estado;

public record CriaEstadoOutput(String id) {

    public static CriaEstadoOutput from(final Estado umEstado) {
        return new CriaEstadoOutput(umEstado.getId().getValue());
    }
}
