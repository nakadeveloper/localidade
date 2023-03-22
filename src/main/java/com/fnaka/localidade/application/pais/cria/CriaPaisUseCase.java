package com.fnaka.localidade.application.pais.cria;

import com.fnaka.localidade.application.UseCase;
import com.fnaka.localidade.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CriaPaisUseCase
        extends UseCase<CriaPaisCommand, Either<Notification, CriaPaisOutput>> {
}
