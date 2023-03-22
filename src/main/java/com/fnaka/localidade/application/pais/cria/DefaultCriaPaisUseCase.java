package com.fnaka.localidade.application.pais.cria;

import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;


import java.util.Objects;

import static io.vavr.API.Try;

public class DefaultCriaPaisUseCase extends CriaPaisUseCase {

    private final PaisGateway paisGateway;

    public DefaultCriaPaisUseCase(final PaisGateway paisGateway) {
        this.paisGateway = Objects.requireNonNull(paisGateway);
    }

    @Override
    public Either<Notification, CriaPaisOutput> execute(CriaPaisCommand aCommand) {
        final var umNome = aCommand.nome();
        final var isAtivo = aCommand.ativo();

        final var notification = Notification.create();

        final var umPais = notification.validate(() -> Pais.newPais(umNome, isAtivo));

        return notification.hasError() ? API.Left(notification) : create(umPais);
    }

    private Either<Notification, CriaPaisOutput> create(Pais umPais) {
        return Try(() -> this.paisGateway.create(umPais))
                .toEither()
                .bimap(Notification::create, CriaPaisOutput::from);
    }
}
