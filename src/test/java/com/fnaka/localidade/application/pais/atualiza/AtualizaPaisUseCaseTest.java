package com.fnaka.localidade.application.pais.atualiza;

import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.UseCaseTest;
import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AtualizaPaisUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultAtualizaPaisUseCase useCase;

    @Mock
    private PaisGateway paisGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(paisGateway);
    }

    @Test
    void givenValidCommand_whenCallsAtualizaPais_shouldReturnPaisId() {
        // given
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var expectedId = umPais.getId();

        final var expectedNome = "Brasil";
        final var expectedIsAtivo = true;

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                expectedNome,
                expectedIsAtivo
        );

        when(paisGateway.findById(any()))
                .thenReturn(Optional.of(Pais.with(umPais)));

        when(paisGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(paisGateway).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Pais.class);
        verify(paisGateway).update(captor.capture());
        final var actualPais = captor.getValue();

        assertEquals(expectedId, actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualPais.getCriadoEm());
        assertTrue(umPais.getAtualizadoEm().isBefore(actualPais.getAtualizadoEm()));
        assertNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenValidCommandInativo_whenCallsAtualizaPais_shouldReturnPaisId() {
        // given
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var expectedId = umPais.getId();

        final var expectedNome = "Brasil";
        final var expectedIsAtivo = false;

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                expectedNome,
                expectedIsAtivo
        );

        when(paisGateway.findById(any()))
                .thenReturn(Optional.of(Pais.with(umPais)));

        when(paisGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(paisGateway).findById(eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Pais.class);
        verify(paisGateway).update(captor.capture());
        final var actualPais = captor.getValue();

        assertEquals(expectedId, actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualPais.getCriadoEm());
        assertTrue(umPais.getAtualizadoEm().isBefore(actualPais.getAtualizadoEm()));
        assertNotNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenInvalidCommandNomeNull_whenCallsAtualizaPais_shouldReturnUmErro() {
        //given
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var expectedId = umPais.getId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                null,
                true
        );

        when(paisGateway.findById(any()))
                .thenReturn(Optional.of(Pais.with(umPais)));

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());

        verify(paisGateway).findById(eq(expectedId));

        verify(paisGateway, times(0)).update(any());
    }

    @Test
    void givenInvalidCommandNomeEmpty_whenCallsAtualizaPais_shouldReturnAnError() {
        // given
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var expectedId = umPais.getId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser vazio";

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                "  ",
                true
        );

        when(paisGateway.findById(any()))
                .thenReturn(Optional.of(Pais.with(umPais)));

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());

        verify(paisGateway).findById(eq(expectedId));

        verify(paisGateway, times(0)).update(any());
    }

    @Test
    void givenInvalidCommandNomeMaiorQue255_whenCallsAtualizaPais_shouldReturnUmErro() {
        // given
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var expectedId = umPais.getId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                """
                        O incentivo ao avanço tecnológico, assim como a estrutura atual da organização faz parte de um processo de gerenciamento do investimento em reciclagem técnica. A nível organizacional, o novo modelo estrutural aqui preconizado apresenta tendências no sentido de aprovar a manutenção dos índices pretendidos.
                        """,
                true
        );

        when(paisGateway.findById(any()))
                .thenReturn(Optional.of(Pais.with(umPais)));

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());

        verify(paisGateway).findById(eq(expectedId));

        verify(paisGateway, times(0)).update(any());
    }

    @Test
    void givenInvalidCommandNomeMenorQue3_whenCallAtualizaPais_shouldReturnUmErro() {
        // given
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var expectedId = umPais.getId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        final var aCommand = AtualizaPaisCommand.with(
                expectedId.getValue(),
                "as",
                true
        );

        when(paisGateway.findById(any()))
                .thenReturn(Optional.of(Pais.with(umPais)));

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());

        verify(paisGateway).findById(eq(expectedId));

        verify(paisGateway, times(0)).update(any());
    }
}