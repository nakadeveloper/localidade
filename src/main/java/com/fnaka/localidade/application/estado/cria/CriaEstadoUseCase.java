package com.fnaka.localidade.application.estado.cria;

import com.fnaka.localidade.application.UseCase;
import com.fnaka.localidade.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CriaEstadoUseCase
        extends UseCase<CriaEstadoCommand, Either<Notification, CriaEstadoOutput>> {
}
