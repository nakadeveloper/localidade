package com.fnaka.localidade.infrastructure.pais;

import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.pais.PaisID;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisRepository;
import com.fnaka.localidade.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaisMySQLGateway implements PaisGateway {

    private final PaisRepository repository;

    public PaisMySQLGateway(final PaisRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pais create(final Pais aPais) {
        return save(aPais);
    }

    @Override
    public Optional<Pais> findById(PaisID anId) {
        return this.repository.findById(anId.getValue())
                .map(PaisJpaEntity::toAggregate);
    }

    @Override
    public Pais update(final Pais aPais) {
        return save(aPais);
    }

    private Pais save(final Pais umPais) {
        return this.repository.save(PaisJpaEntity.from(umPais))
                .toAggregate();
    }

    @Override
    public Pagination<Pais> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(where), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(PaisJpaEntity::toAggregate).stream().toList()
        );
    }

    @Override
    public boolean existsById(PaisID anId) {
        return this.repository.existsById(anId.getValue());
    }

    private Specification<PaisJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("nome", terms);
    }
}
