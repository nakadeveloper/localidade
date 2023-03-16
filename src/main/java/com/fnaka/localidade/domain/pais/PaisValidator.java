package com.fnaka.localidade.domain.pais;

import com.fnaka.localidade.domain.validation.Error;
import com.fnaka.localidade.domain.validation.ValidationHandler;
import com.fnaka.localidade.domain.validation.Validator;

public class PaisValidator extends Validator {

    private static final int NOME_MAX_LENGTH = 255;
    private static final int NOME_MIN_LENGTH = 3;

    private final Pais pais;

    protected PaisValidator(final Pais aPais, final ValidationHandler aHandler) {
        super(aHandler);
        this.pais = aPais;
    }

    @Override
    public void validate() {
        checkNomeConstraint();
    }

    private void checkNomeConstraint() {
        final var nome = this.pais.getNome();
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
}
