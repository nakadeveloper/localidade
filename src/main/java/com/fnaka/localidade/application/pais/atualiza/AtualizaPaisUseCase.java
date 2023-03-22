package com.fnaka.localidade.application.pais.atualiza;

import com.fnaka.localidade.application.UseCase;
import com.fnaka.localidade.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class AtualizaPaisUseCase
        extends UseCase<AtualizaPaisCommand, Either<Notification, AtualizaPaisOutput>> {
}
