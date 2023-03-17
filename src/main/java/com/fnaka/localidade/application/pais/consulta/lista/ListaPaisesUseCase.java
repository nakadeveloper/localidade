package com.fnaka.localidade.application.pais.consulta.lista;

import com.fnaka.localidade.application.UseCase;
import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;

public abstract class ListaPaisesUseCase
        extends UseCase<SearchQuery, Pagination<ListaPaisesOutput>> {
}
