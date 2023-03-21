package com.fnaka.localidade.domain.estado;

import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pais.PaisID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoTest {

    @Test
    void givenValidParams_whenCallNewEstado_shouldInstantiateAGenre() {
        // given
        final var expectedNome = "Sao Paulo";
        final var expectedUf = "SP";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;

        // when
        final var actualEstado = Estado.newEstado(
                expectedNome,
                expectedUf,
                expectedPaisId,
                expectedAtivo
        );

        // then
        assertNotNull(actualEstado);
        assertNotNull(actualEstado.getId());
        assertEquals(expectedNome, actualEstado.getNome());
        assertEquals(expectedUf, actualEstado.getUf());
        assertEquals(expectedPaisId, actualEstado.getPaisId());
        assertNotNull(actualEstado.getCriadoEm());
        assertNotNull(actualEstado.getAtualizadoEm());
        assertNull(actualEstado.getExcluidoEm());
    }

    @Test
    void givenInvalidNullNome_whenCallNewEstadoAndValidate_shouldReceiveAnError() {
        // given
        final String expectedNome = null;
        final var expectedUf = "SP";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        expectedNome,
                        expectedUf,
                        expectedPaisId,
                        expectedAtivo
                )
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyNome_whenCallNewEstadoAndValidate_shouldReceiveAnError() {
        // given
        final var expectedNome = "   ";
        final var expectedUf = "SP";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' nao deve ser vazio";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        expectedNome,
                        expectedUf,
                        expectedPaisId,
                        expectedAtivo
                )
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidParamNomeLengthMaiorQue255_whenCallsNewEstado_shouldReturnAnError() {
        // given
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        """
                                O incentivo ao avanço tecnológico, assim como a estrutura atual da organização faz parte de um processo de gerenciamento do investimento em reciclagem técnica. A nível organizacional, o novo modelo estrutural aqui preconizado apresenta tendências no sentido de aprovar a manutenção dos índices pretendidos.
                                """,
                        "SP",
                        PaisID.unique(),
                        true)
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidParamNomeLengthMenorQue3_whenCallsNewEstado_shouldReturnAnError() {
        // given
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'nome' deve ter entre 3 a 255 caracteres";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        "as",
                        "SP",
                        PaisID.unique(),
                        true)
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidNullUf_whenCallNewEstadoAndValidate_shouldReceiveAnError() {
        // given
        final var expectedNome = "Sao Paulo";
        final String expectedUf = null;
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'uf' nao deve ser nulo";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        expectedNome,
                        expectedUf,
                        expectedPaisId,
                        expectedAtivo
                )
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidEmptyUf_whenCallNewEstadoAndValidate_shouldReceiveAnError() {
        // given
        final var expectedNome = "Sao Paulo";
        final var expectedUf = "   ";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'uf' nao deve ser vazio";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        expectedNome,
                        expectedUf,
                        expectedPaisId,
                        expectedAtivo
                )
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidParamUfLength_whenCallsNewEstado_shouldReturnAnError() {
        // given
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'uf' deve ter 2 caracteres";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        "Sao Paulo",
                        "S",
                        PaisID.unique(),
                        true)
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenInvalidNullPaisId_whenCallNewEstadoAndValidate_shouldReceiveAnError() {
        // given
        final var expectedNome = "Sao Paulo";
        final var expectedUf = "SP";
        final PaisID expectedPaisId = null;
        final var expectedAtivo = true;
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'paisId' nao deve ser nulo";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        expectedNome,
                        expectedUf,
                        expectedPaisId,
                        expectedAtivo
                )
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenInvalidParamPaisIdLength_whenCallsNewEstado_shouldReturnAnError() {
        // given
        final var expectedMessage = "Falha na criacao do Agregado Estado";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'paisId' deve ter 32 caracteres";

        // when
        final var actualException = assertThrows(
                NotificationException.class, () -> Estado.newEstado(
                        "Sao Paulo",
                        "SP",
                        PaisID.from("123"),
                        true)
        );

        // then
        assertEquals(expectedMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getFirstError().message());
    }

    @Test
    void givenValidParam_whenCallsDesativa_shouldReturnOK() {
        // given
        final var expectedNome = "Sao Paulo";
        final var expectedUf = "SP";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = false;

        final var actualEstado = Estado.newEstado(expectedNome, expectedUf, expectedPaisId, true);

        assertNotNull(actualEstado);
        assertTrue(actualEstado.isAtivo());
        assertNull(actualEstado.getExcluidoEm());

        final var actualCriadoEm = actualEstado.getCriadoEm();
        final var actualAtualizadoEm = actualEstado.getAtualizadoEm();

        // when
        actualEstado.desativa();

        // then
        assertNotNull(actualEstado.getId());
        assertEquals(expectedNome, actualEstado.getNome());
        assertEquals(expectedAtivo, actualEstado.isAtivo());
        assertEquals(actualCriadoEm, actualEstado.getCriadoEm());
        assertTrue(actualAtualizadoEm.isBefore(actualEstado.getAtualizadoEm()));
        assertNotNull(actualEstado.getExcluidoEm());
    }

    @Test
    void givenValidParam_whenCallsAtiva_shouldReturnOK() {
        // given
        final var expectedNome = "Sao Paulo";
        final var expectedUf = "SP";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;

        final var actualEstado = Estado.newEstado(expectedNome, expectedUf, expectedPaisId, false);

        assertNotNull(actualEstado);
        assertFalse(actualEstado.isAtivo());
        assertNotNull(actualEstado.getExcluidoEm());

        final var actualCriadoEm = actualEstado.getCriadoEm();
        final var actualAtualizadoEm = actualEstado.getAtualizadoEm();

        // when
        actualEstado.ativa();

        // then
        assertNotNull(actualEstado.getId());
        assertEquals(expectedNome, actualEstado.getNome());
        assertEquals(expectedUf, actualEstado.getUf());
        assertEquals(expectedPaisId, actualEstado.getPaisId());
        assertEquals(expectedAtivo, actualEstado.isAtivo());
        assertEquals(actualCriadoEm, actualEstado.getCriadoEm());
        assertTrue(actualAtualizadoEm.isBefore(actualEstado.getAtualizadoEm()));
        assertNull(actualEstado.getExcluidoEm());
    }

    @Test
    void givenAValidEstadoInativo_whenCallsAtualizaWithAtivo_shouldReceiveGenreUpdated() {
        // given
        final var expectedNome = "Sao Paulo";
        final var expectedUf = "SP";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;

        final var actualEstado = Estado.newEstado(
                expectedNome,
                expectedUf,
                expectedPaisId,
                false
        );

        assertNotNull(actualEstado);
        assertFalse(actualEstado.isAtivo());
        assertNotNull(actualEstado.getExcluidoEm());

        final var actualCriadoEm = actualEstado.getCriadoEm();
        final var actualAtualizadoEm = actualEstado.getAtualizadoEm();

        actualEstado.atualiza(expectedNome, expectedUf, expectedPaisId, expectedAtivo);

        assertNotNull(actualEstado.getId());
        assertEquals(expectedNome, actualEstado.getNome());
        assertEquals(expectedUf, actualEstado.getUf());
        assertEquals(expectedPaisId, actualEstado.getPaisId());
        assertEquals(expectedAtivo, actualEstado.isAtivo());
        assertEquals(actualCriadoEm, actualEstado.getCriadoEm());
        assertTrue(actualAtualizadoEm.isBefore(actualEstado.getAtualizadoEm()));
        assertNull(actualEstado.getExcluidoEm());
    }
}