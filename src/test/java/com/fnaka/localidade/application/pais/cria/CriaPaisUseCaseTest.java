package com.fnaka.localidade.application.pais.cria;

import com.fnaka.localidade.UseCaseTest;
import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class CriaPaisUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCriaPaisUseCase useCase;

    @Mock
    private PaisGateway paisGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(paisGateway);
    }

    @Test
    void givenValidCommand_whenCallsCriaPais_shouldReturnPaisId() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = true;

        final var umCommand = CriaPaisCommand.with(expectedNome, expectedIsAtivo);

        when(paisGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(umCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var captor = ArgumentCaptor.forClass(Pais.class);

        verify(paisGateway).create(captor.capture());

        final var actualPais = captor.getValue();
        assertNotNull(actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertNotNull(actualPais.getCriadoEm());
        assertNotNull(actualPais.getAtualizadoEm());
        assertNull(actualPais.getExcluidoEm());

    }

    @Test
    void givenValidCommandInativo_whenCallsCriaPais_shouldReturnPaisId() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = false;

        final var umCommand = CriaPaisCommand.with(expectedNome, expectedIsAtivo);

        when(paisGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(umCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(paisGateway).create(argThat(umPais ->
                Objects.equals(expectedNome, umPais.getNome())
                        && Objects.equals(expectedIsAtivo, umPais.isAtivo())
                        && Objects.nonNull(umPais.getId())
                        && Objects.nonNull(umPais.getCriadoEm())
                        && Objects.nonNull(umPais.getAtualizadoEm())
                        && Objects.nonNull(umPais.getExcluidoEm())
        ));
    }

    @Test
    void givenInvalidCommandNomeNull_whenCallsCriaPais_shouldReturnUmErro() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var umCommand = CriaPaisCommand.with(null, true);

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(umCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidCommandNomeEmpty_whenCallsCriaPais_shouldReturnUmErro() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser vazio";

        final var umCommand = CriaPaisCommand.with(" ", true);

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(umCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());

        verify(paisGateway, times(0)).create(any());
    }

    @Test
    void givenInvalidCommandNomeMaiorQue255_whenCallsCriaPais_shouldReturnUmErro() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        final var umCommand = CriaPaisCommand.with(
                """
                O incentivo ao avanço tecnológico, assim como a estrutura atual da organização faz parte de um processo de gerenciamento do investimento em reciclagem técnica. A nível organizacional, o novo modelo estrutural aqui preconizado apresenta tendências no sentido de aprovar a manutenção dos índices pretendidos.
                """,
                true
        );

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(umCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());

        verify(paisGateway, times(0)).create(any());
    }

    @Test
    void givenInvalidCommandNomeMenorQue3_whenCallCriaPais_shouldReturnUmErro() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        final var umCommand = CriaPaisCommand.with("as",true);

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> useCase.execute(umCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());

        verify(paisGateway, times(0)).create(any());
    }
}
