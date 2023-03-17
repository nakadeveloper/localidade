package com.fnaka.localidade.application.pais.cria;

import com.fnaka.localidade.IntegrationTest;
import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
class CriaPaisUseCaseIntegrationTest {

    @Autowired
    private CriaPaisUseCase useCase;

    @SpyBean
    private PaisGateway paisGateway;

    @Autowired
    private PaisRepository paisRepository;

    @Test
    void givenAValidCommand_whenCallsCriaPais_shouldReturnPaisId() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = true;

        final var aCommand = CriaPaisCommand.with(expectedNome, expectedIsAtivo);

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualPais = paisRepository.findById(actualOutput.id()).get();

        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertNotNull(actualPais.getCriadoEm());
        assertNotNull(actualPais.getAtualizadoEm());
        assertNull(actualPais.getExcluidoEm());

        verify(paisGateway).create(any());
    }

    @Test
    void givenAnInvalidNome_whenCallsCriaPais_shouldThrowsNotificationException() {
        // given
        final String expectedNome = null;
        final var expectedIsAtivo = true;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var aCommand = CriaPaisCommand.with(expectedNome, expectedIsAtivo);

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(paisGateway, times(0)).create(any());
    }

}
