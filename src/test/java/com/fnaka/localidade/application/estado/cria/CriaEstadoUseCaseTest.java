package com.fnaka.localidade.application.estado.cria;

import com.fnaka.localidade.UseCaseTest;
import com.fnaka.localidade.domain.estado.Estado;
import com.fnaka.localidade.domain.estado.EstadoGateway;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.pais.PaisID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CriaEstadoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCriaEstadoUseCase useCase;

    @Mock
    private EstadoGateway estadoGateway;

    @Mock
    private PaisGateway paisGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(estadoGateway, paisGateway);
    }

    @Test
    void givenAValidParams_whenCallsCriaEstado_shouldEstadoId() {
        // given
        final var expectedNome = "Sao Paulo";
        final var expectedUf = "SP";
        final var expectedPaisId = PaisID.unique();
        final var expectedAtivo = true;

        final var aCommand = CriaEstadoCommand.with(
                expectedNome, expectedUf, expectedPaisId.getValue(), expectedAtivo
        );

        when(paisGateway.existsById(expectedPaisId))
                .thenReturn(true);
        when(estadoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand).get();

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var captor = ArgumentCaptor.forClass(Estado.class);
        verify(estadoGateway).create(captor.capture());
        final var actualEstado = captor.getValue();

        Assertions.assertEquals(expectedNome, actualEstado.getNome());
        Assertions.assertEquals(expectedUf, actualEstado.getUf());
        Assertions.assertEquals(expectedPaisId, actualEstado.getPaisId());
        Assertions.assertEquals(expectedAtivo, actualEstado.isAtivo());
        Assertions.assertNotNull(actualEstado.getCriadoEm());
        Assertions.assertNotNull(actualEstado.getAtualizadoEm());
        Assertions.assertNull(actualEstado.getExcluidoEm());
    }
}
