package com.fnaka.localidade.application.pais.cria;

import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisGateway;
import com.fnaka.localidade.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultCriaPaisUseCase extends CriaPaisUseCase {

    private final PaisGateway paisGateway;

    public DefaultCriaPaisUseCase(final PaisGateway paisGateway) {
        this.paisGateway = Objects.requireNonNull(paisGateway);
    }

    @Override
    public CriaPaisOutput execute(CriaPaisCommand umCommand) {
        final var umNome = umCommand.nome();
        final var isAtivo = umCommand.ativo();

        final var notification = Notification.create();

        final var umPais = notification.validate(() -> Pais.newPais(umNome, isAtivo));

        if (notification.hasError()) {
            throw new NotificationException("Nao foi possivel criar agregado Pais", notification);
        }

        return CriaPaisOutput.from(this.paisGateway.create(umPais));
    }
}
