package com.fnaka.localidade.infrastructure.pais.presenters;

import com.fnaka.localidade.application.pais.consulta.busca.PaisOutput;
import com.fnaka.localidade.application.pais.consulta.lista.ListaPaisesOutput;
import com.fnaka.localidade.infrastructure.pais.models.ListaPaisesResponse;
import com.fnaka.localidade.infrastructure.pais.models.PaisResponse;

public interface PaisApiPresenter {

    static PaisResponse present(final PaisOutput output) {
        return new PaisResponse(
                output.id(),
                output.nome(),
                output.isAtivo(),
                output.criadoEm(),
                output.atualizadoEm(),
                output.excluidoEm()
        );
    }

    static ListaPaisesResponse present(final ListaPaisesOutput output) {
        return new ListaPaisesResponse(
                output.id(),
                output.nome(),
                output.isAtivo(),
                output.criadoEm(),
                output.excluidoEm()
        );
    }
}
