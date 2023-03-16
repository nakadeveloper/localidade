package com.fnaka.localidade.domain.pais;

import com.fnaka.localidade.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaisTest {

    @Test
    void givenValidParams_whenCallsNewPais_shouldInstanciarUmPais() {
        // given
        final var expectedNome = "Brasil";
        final var expectedIsAtivo = true;

        // when
        final var actualPais = Pais.newPais(expectedNome, expectedIsAtivo);

        // then
        assertNotNull(actualPais);
        assertNotNull(actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertNotNull(actualPais.getCriadoEm());
        assertNotNull(actualPais.getAtualizadoEm());
        assertEquals(actualPais.getCriadoEm(), actualPais.getAtualizadoEm());
        assertNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenValidParamInativo_whenCallsNewPais_shouldInstaciarUmPais() {
        // given
        final var expectedNome = "Brasil";
        final var expectedIsAtivo = false;

        // when
        final var actualPais = Pais.newPais(expectedNome, expectedIsAtivo);

        // then
        assertNotNull(actualPais);
        assertNotNull(actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertNotNull(actualPais.getCriadoEm());
        assertNotNull(actualPais.getAtualizadoEm());
        assertEquals(actualPais.getCriadoEm(), actualPais.getAtualizadoEm());
        assertNotNull(actualPais.getExcluidoEm());
        assertEquals(actualPais.getCriadoEm(), actualPais.getExcluidoEm());
    }

    @Test
    void givenInvalidParamNomeNull_whenCallsNewPais_shouldReturnAnError() {
        //given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Pais.newPais(null, true)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidParamNomeEmpty_whenCallsNewPais_shouldReturnAnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser vazio";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Pais.newPais("  ", true)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidParamNomeLengthMaiorQue255_whenCallsNewPais_shouldReturnAnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Pais.newPais(
                        """
                                O incentivo ao avanço tecnológico, assim como a estrutura atual da organização faz parte de um processo de gerenciamento do investimento em reciclagem técnica. A nível organizacional, o novo modelo estrutural aqui preconizado apresenta tendências no sentido de aprovar a manutenção dos índices pretendidos.
                                """,
                        true)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidParamNomeLengthMenorQue3_whenCallsNewPais_shouldReturnAnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Pais.newPais("as", true)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenValidParam_whenCallsDesativa_shouldReturnOK() {
        // given
        final var expectedNome = "Brasil";
        final var expectedIsAtivo = false;

        final var actualPais = Pais.newPais(expectedNome, true);

        assertNotNull(actualPais);
        assertTrue(actualPais.isAtivo());
        assertNull(actualPais.getExcluidoEm());

        final var actualCriadoEm = actualPais.getCriadoEm();
        final var actualAtualizadoEm = actualPais.getAtualizadoEm();

        // when
        actualPais.desativa();

        // then
        assertNotNull(actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(actualCriadoEm, actualPais.getCriadoEm());
        assertTrue(actualAtualizadoEm.isBefore(actualPais.getAtualizadoEm()));
        assertNotNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenValidParam_whenCallsAtiva_shouldReturnOK() {
        // given
        final var expectedNome = "Brasil";
        final var expectedIsAtivo = true;

        final var actualPais = Pais.newPais(expectedNome, false);

        assertNotNull(actualPais);
        assertFalse(actualPais.isAtivo());
        assertNotNull(actualPais.getExcluidoEm());

        final var actualCriadoEm = actualPais.getCriadoEm();
        final var actualAtualizadoEm = actualPais.getAtualizadoEm();

        // when
        actualPais.ativa();

        // then
        assertNotNull(actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(actualCriadoEm, actualPais.getCriadoEm());
        assertTrue(actualAtualizadoEm.isBefore(actualPais.getAtualizadoEm()));
        assertNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenValidParamAtivo_whenCallsAtualiza_shouldReturnPaisAtualizado() {
        // given
        final var expectedNome = "Brasil";
        final var expectedIsAtivo = true;

        final var actualPais = Pais.newPais("Bras", false);

        assertNotNull(actualPais);
        assertFalse(actualPais.isAtivo());
        assertNotNull(actualPais.getExcluidoEm());

        final var actualCriadoEm = actualPais.getCriadoEm();
        final var actualAtualizadoEm = actualPais.getAtualizadoEm();

        // when
        actualPais.atualiza(expectedNome, expectedIsAtivo);

        // then
        assertNotNull(actualPais);
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(actualCriadoEm, actualPais.getCriadoEm());
        assertTrue(actualAtualizadoEm.isBefore(actualPais.getAtualizadoEm()));
        assertNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenValidParamInativo_whenCallsAtualiza_shouldReturnPaisAtualizado() {
        // given
        final var expectedNome = "Brasil";
        final var expectedIsAtivo = false;

        final var actualPais = Pais.newPais("Bras", true);

        assertNotNull(actualPais);
        assertTrue(actualPais.isAtivo());
        assertNull(actualPais.getExcluidoEm());

        final var actualCriadoEm = actualPais.getCriadoEm();
        final var actualAtualizadoEm = actualPais.getAtualizadoEm();

        // when
        actualPais.atualiza(expectedNome, expectedIsAtivo);

        // then
        assertNotNull(actualPais.getId());
        assertEquals(expectedNome, actualPais.getNome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(actualCriadoEm, actualPais.getCriadoEm());
        assertTrue(actualAtualizadoEm.isBefore(actualPais.getAtualizadoEm()));
        assertNotNull(actualPais.getExcluidoEm());
    }

    @Test
    void givenInvalidParamNomeNull_whenCallsAtualiza_shouldReturnAnError() {
        // given
        final var actualPais = Pais.newPais("Brasil", true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> actualPais.atualiza(null, true)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidParamNomeEmpty_whenCallsAtualiza_shouldReturnAnError() {
        // given
        final var actualPais = Pais.newPais("Brasil", true);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser vazio";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> actualPais.atualiza(" ", true)
        );

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

}