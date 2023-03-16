package com.fnaka.localidade.domain.pais;

import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;

import java.util.Optional;

public interface PaisGateway {

    Pais create(Pais aPais);

    Optional<Pais> findById(PaisID anId);

    Pais update(Pais aPais);

    Pagination<Pais> findAll(SearchQuery aQuery);

    boolean existsById(PaisID anId);
}
