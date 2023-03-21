package com.fnaka.localidade.domain.estado;

import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;

import java.util.Optional;

public interface EstadoGateway {

    Estado create(Estado umEstado);

    Optional<Estado> findById(EstadoID anId);

    Estado update(Estado umEstado);

    Pagination<Estado> findAll(SearchQuery aQuery);
}
