package com.fnaka.localidade.application.pais.consulta.busca;

import com.fnaka.localidade.domain.exceptions.NotFoundException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.pais.PaisID;

import java.util.Objects;

public class DefaultBuscaPaisPorIdUseCase extends BuscaPaisPorIdUseCase {

    private final PaisGateway paisGateway;

    public DefaultBuscaPaisPorIdUseCase(final PaisGateway paisGateway) {
        this.paisGateway = Objects.requireNonNull(paisGateway);
    }

    @Override
    public PaisOutput execute(final String anId) {
        final var aPaisId = PaisID.from(anId);
        return this.paisGateway.findById(aPaisId)
                .map(PaisOutput::from)
                .orElseThrow(() -> NotFoundException.with(Pais.class, aPaisId));
    }
}
