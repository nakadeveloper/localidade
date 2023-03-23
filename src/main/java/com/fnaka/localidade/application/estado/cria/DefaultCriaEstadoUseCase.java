package com.fnaka.localidade.application.estado.cria;

import com.fnaka.localidade.domain.estado.Estado;
import com.fnaka.localidade.domain.estado.EstadoGateway;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.pais.PaisID;
import com.fnaka.localidade.domain.validation.Error;
import com.fnaka.localidade.domain.validation.ValidationHandler;
import com.fnaka.localidade.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

public class DefaultCriaEstadoUseCase extends CriaEstadoUseCase{

    private final PaisGateway paisGateway;
    private final EstadoGateway estadoGateway;

    public DefaultCriaEstadoUseCase(
            final PaisGateway paisGateway,
            final EstadoGateway estadoGateway
    ) {
        this.paisGateway = Objects.requireNonNull(paisGateway);
        this.estadoGateway = Objects.requireNonNull(estadoGateway);
    }

    @Override
    public Either<Notification, CriaEstadoOutput> execute(final CriaEstadoCommand aCommand) {
        final var umNome = aCommand.nome();
        final var umaUf = aCommand.uf();
        final var anPaisId = PaisID.from(aCommand.paisId());
        final var isAtivo = aCommand.ativo();

        final var notification = Notification.create();
        notification.append(validatePaisId(anPaisId));

        final var umEstado = notification.validate(
                () -> Estado.newEstado(umNome, umaUf, anPaisId, isAtivo)
        );

        return notification.hasError() ? API.Left(notification) : create(umEstado);
    }

    private Either<Notification, CriaEstadoOutput> create(final Estado umEstado) {
        return API.Try(() -> estadoGateway.create(umEstado))
                .toEither()
                .bimap(Notification::create, CriaEstadoOutput::from);
    }

    private ValidationHandler validatePaisId(final PaisID anPaisId) {
        final var notification = Notification.create();
        if (!paisGateway.existsById(anPaisId)) {
            notification.append(new Error("Pais nao encontrado: %s".formatted(anPaisId.getValue())));
        }
        return notification;
    }
}
