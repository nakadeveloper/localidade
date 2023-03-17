package com.fnaka.localidade.application.pais.consulta.lista;

import com.fnaka.localidade.UseCaseTest;
import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListaPaisesUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListaPaisesUseCase useCase;

    @Mock
    private PaisGateway paisGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(paisGateway);
    }

    @Test
    void givenValidQuery_whenCallsListaPais_shouldReturnPaises() {
        // given
        final var paises = List.of(
                Pais.newPais("Brasil", true),
                Pais.newPais("Bolivia", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "B";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = paises.stream()
                .map(ListaPaisesOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                paises
        );

        when(paisGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(paisGateway).findAll(eq(aQuery));
    }

    @Test
    void givenValidQuery_whenCallsListaPaisAndGatewayThrowsError_shouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "B";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(paisGateway.findAll(any()))
                .thenThrow(new IllegalStateException((expectedErrorMessage)));

        final var aQuery = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        // when
        final var actualException = assertThrows(
                IllegalStateException.class, () -> useCase.execute(aQuery)
        );

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(paisGateway).findAll(eq(aQuery));
    }

    @Test
    void givenValidQUery_whenCallsListaPaisAndResultIsEmpty_shouldReturnEmpty() {
        // given
        final var paises = List.<Pais>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "B";
        final var expectedSort = "criadoEm";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<ListaPaisesOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                paises
        );

        when(paisGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var umaQuery = new SearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        // when
        final var actualOutput = useCase.execute(umaQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(paisGateway).findAll(eq(umaQuery));
    }
}