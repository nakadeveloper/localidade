package com.fnaka.localidade.application.pais.atualiza;

import com.fnaka.localidade.domain.Identifier;
import com.fnaka.localidade.domain.exceptions.NotFoundException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.pais.PaisID;
import com.fnaka.localidade.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultAtualizaPaisUseCase extends AtualizaPaisUseCase {

    private final PaisGateway paisGateway;

    public DefaultAtualizaPaisUseCase(final PaisGateway paisGateway) {
        this.paisGateway = Objects.requireNonNull(paisGateway);
    }

    @Override
    public Either<Notification, AtualizaPaisOutput> execute(final AtualizaPaisCommand aCommand) {
        final var anId = PaisID.from(aCommand.id());
        final var umNome = aCommand.nome();
        final var isAtivo = aCommand.ativo();

        final var umPais = this.paisGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.validate(() -> umPais.atualiza(umNome, isAtivo));

        return notification.hasError() ? API.Left(notification) : update(umPais);
    }

    private Either<Notification, AtualizaPaisOutput> update(final Pais umPais) {
        return API.Try(() -> this.paisGateway.update(umPais))
                .toEither()
                .bimap(Notification::create, AtualizaPaisOutput::from);
    }

    private Supplier<NotFoundException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Pais.class, anId);
    }
}
