package com.fnaka.localidade.application.pais.atualiza;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
class AtualizaPaisUseCaseIntegrationTest {

    @Autowired
    private AtualizaPaisUseCase useCase;

    @Autowired
    private PaisRepository paisRepository;

    @SpyBean
    private PaisGateway paisGateway;

    @Test
    void givenAValidCommand_whenCallsAtualizaPais_shouldReturnItsIdentifier() {
        // given
        final var aPais = Pais.newPais("Bras", true);

        this.paisRepository.saveAndFlush(PaisJpaEntity.from(aPais));

        final var expectedId = aPais.getId();
        final var expectedNome = Fixture.Pais.nome();

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                expectedNome,
                true
        );

        // when
        final var actualOutput = useCase.execute(aCommand).get();

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualPersistedMember =
                this.paisRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedNome, actualPersistedMember.getNome());
        assertEquals(aPais.getCriadoEm(), actualPersistedMember.getCriadoEm());
        assertTrue(aPais.getAtualizadoEm().isBefore(actualPersistedMember.getAtualizadoEm()));

        verify(paisGateway).findById(any());
        verify(paisGateway).update(any());
    }

    @Test
    void givenAnInvalidNome_whenCallsAtualizaPais_shouldThrowsNotificationException() {
        // given
        final var aPais = Pais.newPais("Bras", true);

        this.paisRepository.saveAndFlush(PaisJpaEntity.from(aPais));

        final var expectedId = aPais.getId();
        final String expectedNome = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                expectedNome,
                true
        );

        // when
        final var notification = useCase.execute(aCommand).getLeft();


        // then
        assertNotNull(notification);

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        verify(paisGateway).findById(any());
        verify(paisGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidId_whenCallsAtualizaPais_shouldThrowsNotFoundException() {
        // given
        final var expectedId = PaisID.from("123");
        final var expectedName = Fixture.Pais.nome();

        final var expectedErrorMessage = "Pais with ID 123 was not found";

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                expectedName,
                true
        );

        // when
        final var actualException = assertThrows(
                NotFoundException.class, () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(paisGateway).findById(any());
        verify(paisGateway, times(0)).update(any());
    }
}
