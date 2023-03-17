package com.fnaka.localidade.application.pais.consulta.busca;

import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.IntegrationTest;
import com.fnaka.localidade.domain.exceptions.NotFoundException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.pais.PaisID;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@IntegrationTest
class BuscaPaisPorIdUseCaseIntegrationTest {

    @Autowired
    private BuscaPaisPorIdUseCase useCase;

    @Autowired
    private PaisRepository paisRepository;

    @SpyBean
    private PaisGateway paisGateway;

    @Test
    void givenAValidId_whenCallsBuscaPaisPorId_shouldReturnIt() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = true;

        final var aPais = Pais.newPais(expectedNome, expectedIsAtivo);

        final var expectedId = aPais.getId();

        paisRepository.saveAndFlush(PaisJpaEntity.from(aPais));

        assertEquals(1, paisRepository.count());

        // when
        final var actualOutput = useCase.execute(expectedId.getValue());

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedNome, actualOutput.nome());
        assertEquals(expectedIsAtivo, actualOutput.isAtivo());
        assertEquals(aPais.getCriadoEm(), actualOutput.criadoEm());
        assertEquals(aPais.getAtualizadoEm(), actualOutput.atualizadoEm());

        verify(paisGateway).findById(any());
    }

    @Test
    void givenAnInvalidId_whenCallsBusPaisPorId_shouldReturnNotFoundException() {
        // given
        final var expectedId = PaisID.from("123");
        final var expectedErrorMessage = "Pais with ID 123 was not found";

        // when
        final var actualException = assertThrows(
                NotFoundException.class, () -> useCase.execute(expectedId.getValue())
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(paisGateway).findById(eq(expectedId));
    }
}
