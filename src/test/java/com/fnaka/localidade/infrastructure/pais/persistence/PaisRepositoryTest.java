package com.fnaka.localidade.infrastructure.pais.persistence;

import com.fnaka.localidade.MySQLGatewayTest;
import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity;
import com.fnaka.localidade.infrastructure.pais.persistence.PaisRepository;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
class PaisRepositoryTest {

    @Autowired
    private PaisRepository paisRepository;

    @Test
    void givenAnInvalidNomeNull_whenCallsSave_shouldReturnError() {
        // given
        final var expectedPropertyName = "nome";
        final var expectedMessage = "not-null property references a null or transient value : com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity.nome";
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var anEntity = PaisJpaEntity.from(umPais);
        anEntity.setNome(null);

        // when
        final var actualException = assertThrows(
                DataIntegrityViolationException.class, () -> paisRepository.save(anEntity)
        );

        // then
        final var actualCause = assertInstanceOf(
                PropertyValueException.class, actualException.getCause()
        );

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidCriadoEmNull_whenCallsSave_shouldReturnError() {
        // given
        final var expectedPropertyName = "criadoEm";
        final var expectedMessage = "not-null property references a null or transient value : com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity.criadoEm";
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var anEntity = PaisJpaEntity.from(umPais);
        anEntity.setCriadoEm(null);

        // when
        final var actualException = assertThrows(
                DataIntegrityViolationException.class, () -> paisRepository.save(anEntity)
        );

        // then
        final var actualCause = assertInstanceOf(
                PropertyValueException.class, actualException.getCause()
        );

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidAtualizadoEmNull_whenCallsSave_shouldReturnError() {
        // given
        final var expectedPropertyName = "atualizadoEm";
        final var expectedMessage = "not-null property references a null or transient value : com.fnaka.localidade.infrastructure.pais.persistence.PaisJpaEntity.atualizadoEm";
        final var umPais = Pais.newPais(Fixture.Pais.nome(), true);
        final var anEntity = PaisJpaEntity.from(umPais);
        anEntity.setAtualizadoEm(null);

        // when
        final var actualException = assertThrows(
                DataIntegrityViolationException.class, () -> paisRepository.save(anEntity)
        );

        // then
        final var actualCause = assertInstanceOf(
                PropertyValueException.class, actualException.getCause()
        );

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());
    }
}
