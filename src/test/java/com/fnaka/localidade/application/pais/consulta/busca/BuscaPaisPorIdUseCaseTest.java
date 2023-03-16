package com.fnaka.localidade.application.pais.consulta.busca;

import com.fnaka.localidade.UseCaseTest;
import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.domain.exceptions.NotFoundException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.pais.PaisID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuscaPaisPorIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultBuscaPaisPorIdUseCase useCase;

    @Mock
    private PaisGateway paisGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(paisGateway);
    }

    @Test
    void givenValidId_whenCallsBuscaPais_shouldReturnPais() {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedIsAtivo = true;

        final var umPais = Pais.newPais(expectedNome, expectedIsAtivo);
        final var expectedId = umPais.getId();

        when(paisGateway.findById(any()))
                .thenReturn(Optional.of(umPais));

        // when
        final var actualPais = useCase.execute(expectedId.getValue());

        // then
        assertEquals(expectedId.getValue(), actualPais.id());
        assertEquals(expectedNome, actualPais.nome());
        assertEquals(expectedIsAtivo, actualPais.isAtivo());
        assertEquals(umPais.getCriadoEm(), actualPais.criadoEm());
        assertEquals(umPais.getAtualizadoEm(), actualPais.atualizadoEm());
        assertEquals(umPais.getExcluidoEm(), actualPais.excluidoEm());

        verify(paisGateway).findById(eq(expectedId));
    }

    @Test
    void givenInvalidId_whenCallsBuscaPais_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Pais with ID 123 was not found";
        final var expectedId = PaisID.from("123");

        when(paisGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(paisGateway).findById(eq(expectedId));
    }
}