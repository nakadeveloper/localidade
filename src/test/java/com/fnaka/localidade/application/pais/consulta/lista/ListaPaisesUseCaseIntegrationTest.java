package com.fnaka.localidade.application.pais.consulta.lista;

import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.IntegrationTest;
import com.fnaka.localidade.domain.pagination.SearchQuery;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@IntegrationTest
class ListaPaisesUseCaseIntegrationTest {

    @Autowired
    private ListaPaisesUseCase useCase;

    @Autowired
    private PaisRepository paisRepository;

    @SpyBean
    private PaisGateway paisGateway;

    @Test
    void givenAValidQuery_whenCallsListaPaises_shouldReturnAll() {
        // given
        final var paises = List.of(
                Pais.newPais(Fixture.Pais.nome(), true),
                Pais.newPais(Fixture.Pais.nome(), true)
        );

        paisRepository.saveAllAndFlush(paises.stream()
                .map(PaisJpaEntity::from)
                .toList()
        );

        assertEquals(2, paisRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = paises.stream()
                .map(ListaPaisesOutput::from)
                .toList();

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms, expectedSort,
                expectedDirection
        );
        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.items().size()
                        && expectedItems.containsAll(actualOutput.items())
        );

        verify(paisGateway).findAll(any());
    }

    @Test
    void givenAValidQuery_whenCallsListaPaisesAndIsEmpty_shouldReturn() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<ListaPaisesOutput>of();

        assertEquals(0, paisRepository.count());

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(paisGateway).findAll(any());
    }
}
