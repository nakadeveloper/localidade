package com.fnaka.localidade.application.pais.consulta.lista;

import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;
import com.fnaka.localidade.domain.pais.PaisGateway;

import java.util.Objects;

public class DefaultListaPaisesUseCase extends ListaPaisesUseCase {

    private final PaisGateway paisGateway;

    public DefaultListaPaisesUseCase(final PaisGateway paisGateway) {
        this.paisGateway = Objects.requireNonNull(paisGateway);
    }

    @Override
    public Pagination<ListaPaisesOutput> execute(SearchQuery umaQuery) {
        return this.paisGateway.findAll(umaQuery)
                .map(ListaPaisesOutput::from);
    }
}
