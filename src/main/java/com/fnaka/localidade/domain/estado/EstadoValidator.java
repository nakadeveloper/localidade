package com.fnaka.localidade.domain.estado;

import com.fnaka.localidade.domain.validation.Error;
import com.fnaka.localidade.domain.validation.ValidationHandler;
import com.fnaka.localidade.domain.validation.Validator;

public class EstadoValidator extends Validator {

    private static final int NOME_MAX_LENGTH = 255;
    private static final int NOME_MIN_LENGTH = 3;
    private static final int UF_LENGTH = 2;
    private static final int PAIS_ID_LENGTH = 32;

    private final Estado estado;

    public EstadoValidator(Estado umEstado, ValidationHandler handler) {
        super(handler);
        this.estado = umEstado;
    }

    @Override
    public void validate() {
        checkNomeConstraints();
        checkUfConstraints();
        checkPaisIdConstraints();
    }

    private void checkPaisIdConstraints() {
        final var paisId = this.estado.getPaisId();
        if (paisId == null) {
            this.validationHandler().append(new Error("'paisId' nao deve ser nulo"));
            return;
        }

        final int length = paisId.getValue().length();
        if (length != PAIS_ID_LENGTH) {
            this.validationHandler().append(new Error("'paisId' deve ter 32 caracteres"));
        }
    }

    private void checkNomeConstraints() {
        final var nome = this.estado.getNome();
        if (nome == null) {
            this.validationHandler().append(new Error("'nome' nao deve ser nulo"));
            return;
        }

        if (nome.isBlank()) {
            this.validationHandler().append(new Error("'nome' nao deve ser vazio"));
            return;
        }

        final int length = nome.length();
        if (length > NOME_MAX_LENGTH || length < NOME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'nome' deve ter entre 3 a 255 caracteres"));
        }
    }

    private void checkUfConstraints() {
        final var uf = this.estado.getUf();
        if (uf == null) {
            this.validationHandler().append(new Error("'uf' nao deve ser nulo"));
            return;
        }

        if (uf.isBlank()) {
            this.validationHandler().append(new Error("'uf' nao deve ser vazio"));
            return;
        }

        final int length = uf.length();
        if (length != UF_LENGTH) {
            this.validationHandler().append(new Error("'uf' deve ter 2 caracteres"));
        }
    }
}
