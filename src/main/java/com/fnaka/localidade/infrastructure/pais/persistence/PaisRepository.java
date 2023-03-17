package com.fnaka.localidade.infrastructure.pais.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaisRepository extends JpaRepository<PaisJpaEntity, String> {

    Page<PaisJpaEntity> findAll(Specification<PaisJpaEntity> whereClause, Pageable page);
}
