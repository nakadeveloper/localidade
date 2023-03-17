package com.fnaka.localidade.infrastructure.pais;

import com.fnaka.localidade.MySQLGatewayTest;
import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.domain.pagination.SearchQuery;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisID;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class PaisMySQLGatewayTest {

    @Autowired
    private PaisMySQLGateway paisGateway;

    @Autowired
    private PaisRepository paisRepository;

    @Test
    void givenAValidPais_whenCallsCria_shouldReturnNewPais() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = true;

        final var umPais = Pais.newPais(expectedNome, expectedIsAtivo);

        assertEquals(0, paisRepository.count());

        // when
        final var actualPais = paisGateway.create(umPais);

        // then
        assertEquals(1, paisRepository.count());

        assertEquals(umPais.getId(), actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualPais.getCriadoEm());
        assertEquals(umPais.getAtualizadoEm(), actualPais.getAtualizadoEm());
        assertNull(actualPais.getExcluidoEm());

        final var actualEntity = paisRepository.findById(umPais.getId().getValue()).get();

        assertEquals(umPais.getId().getValue(), actualEntity.getId());
        assertEquals(expectedNome, actualEntity.getNome());
        assertEquals(expectedIsAtivo, actualEntity.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualEntity.getCriadoEm());
        assertEquals(umPais.getAtualizadoEm(), actualEntity.getAtualizadoEm());
        assertNull(actualEntity.getExcluidoEm());
    }

    @Test
    void givenAValidPais_whenCallsAtualiza_shouldReturnPaisAtualizado() {
        // given
        final var expectedNome = "Brasil";
        final var expectedIsAtivo = true;

        final var umPais = Pais.newPais(Fixture.Pais.nome(), false);

        assertEquals(0, paisRepository.count());

        paisRepository.saveAndFlush(PaisJpaEntity.from(umPais));

        assertEquals(1, paisRepository.count());

        final var umPaisAtualizado = Pais.with(umPais)
                .atualiza(expectedNome, expectedIsAtivo);

        // when
        final var actualPais = paisGateway.update(umPaisAtualizado);

        // then
        assertEquals(1, paisRepository.count());

        assertEquals(umPais.getId(), actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualPais.getCriadoEm());
        assertTrue(umPais.getAtualizadoEm().isBefore(actualPais.getAtualizadoEm()));
        assertNull(actualPais.getExcluidoEm());

        final var actualEntity = paisRepository.findById(umPais.getId().getValue()).get();

        assertEquals(umPais.getId().getValue(), actualEntity.getId());
        assertEquals(expectedNome, actualEntity.getNome());
        assertEquals(expectedIsAtivo, actualEntity.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualEntity.getCriadoEm());
        assertTrue(umPais.getAtualizadoEm().isBefore(actualEntity.getAtualizadoEm()));
        assertNull(actualEntity.getExcluidoEm());
    }

    @Test
    void givenAPrePersistedPais_whenCallsBuscaPorId_shouldReturnPais() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = true;

        final var umPais = Pais.newPais(expectedNome, expectedIsAtivo);

        assertEquals(0, paisRepository.count());

        paisRepository.saveAndFlush(PaisJpaEntity.from(umPais));

        assertEquals(1, paisRepository.count());

        // when
        final var actualPais = paisGateway.findById(umPais.getId()).get();

        // then
        assertEquals(1, paisRepository.count());

        assertEquals(umPais.getId(), actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualPais.getCriadoEm());
        assertEquals(umPais.getAtualizadoEm(), actualPais.getAtualizadoEm());
        assertNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenAValidPaisIdNotStored_whenCallsBuscaPorId_shouldReturnEmpty() {
        // given
        assertEquals(0, paisRepository.count());

        // when
        final var actualPais = paisGateway.findById(PaisID.from("empty"));

        // then
        assertEquals(0, paisRepository.count());

        assertTrue(actualPais.isEmpty());
    }

    @Test
    void givenEmptyPais_whenCallsListaTodos_shouldReturnEmpty() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "nome";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualPage = paisGateway.findAll(aQuery);

        //
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "br,0,10,1,1,Brasil",
            "ale,0,10,1,1,Alemanha",
            "col,0,10,1,1,Colombia",
            "uni,0,10,1,1,Estados Unidos",
            "can,0,10,1,1,Canada",
    })
    void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedPaisNome
    ) {
        // given
        mockPaises();
        final var expectedSort = "nome";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = paisGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedPaisNome, actualPage.items().get(0).getNome());
    }

    @ParameterizedTest
    @CsvSource({
            "nome,asc,0,10,5,5,Alemanha",
            "nome,desc,0,10,5,5,Estados Unidos",
            "criadoEm,asc,0,10,5,5,Brasil",
            "criadoEm,desc,0,10,5,5,Alemanha",
    })
    void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedPaisNome
    ) {
        // given
        mockPaises();
        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = paisGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedPaisNome, actualPage.items().get(0).getNome());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Alemanha;Brasil",
            "1,2,2,5,Canada;Colombia",
            "2,2,1,5,Estados Unidos",
    })
    void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedNomes
    ) {
        // given
        mockPaises();

        final var expectedTerms = "";
        final var expectedSort = "nome";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = paisGateway.findAll(aQuery);

        // then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedNome : expectedNomes.split(";")) {
            assertEquals(expectedNome, actualPage.items().get(index).getNome());
            index++;
        }
    }

    @Test
    void givenAPrePersistedPais_whenCallsExistsById_shouldReturnTrue() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = true;

        final var umPais = Pais.newPais(expectedNome, expectedIsAtivo);

        assertEquals(0, paisRepository.count());

        paisRepository.saveAndFlush(PaisJpaEntity.from(umPais));

        assertEquals(1, paisRepository.count());

        // when
        final var actualResult = paisGateway.existsById(umPais.getId());

        // then
        assertEquals(1, paisRepository.count());

        assertTrue(actualResult);
    }

    @Test
    void givenAValidPaisIdNotStored_whenCallsExistsById_shouldReturnFalse() {
        // given
        assertEquals(0, paisRepository.count());

        // when
        final var actualResult = paisGateway.existsById(PaisID.from("empty"));

        // then
        assertEquals(0, paisRepository.count());

        assertFalse(actualResult);
    }

    private void mockPaises() {
        paisRepository.saveAllAndFlush(List.of(
                PaisJpaEntity.from(Pais.newPais("Brasil", true)),
                PaisJpaEntity.from(Pais.newPais("Colombia", true)),
                PaisJpaEntity.from(Pais.newPais("Estados Unidos", true)),
                PaisJpaEntity.from(Pais.newPais("Canada", true)),
                PaisJpaEntity.from(Pais.newPais("Alemanha", true))
        ));
    }
}


