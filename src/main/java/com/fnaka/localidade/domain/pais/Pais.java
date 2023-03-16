package com.fnaka.localidade.domain.pais;

import com.fnaka.localidade.domain.AggregateRoot;
import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.utils.InstantUtils;
import com.fnaka.localidade.domain.validation.ValidationHandler;
import com.fnaka.localidade.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.Objects;

public class Pais extends AggregateRoot<PaisID> {

    private String nome;
    private boolean ativo;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant excluidoEm;

    protected Pais(
            final PaisID umId,
            final String umNome,
            final boolean isAtivo,
            final Instant criadoEm,
            final Instant atualizadoEm,
            final Instant excluidoEm
    ) {
        super(umId);
        this.nome = umNome;
        this.ativo = isAtivo;
        this.criadoEm = Objects.requireNonNull(criadoEm, "'criadoEm' nao deve ser nulo");
        this.atualizadoEm = Objects.requireNonNull(atualizadoEm, "'atualizadoEm' nao deve ser nulo");
        this.excluidoEm = excluidoEm;
        selfValidate();
    }

    public static Pais newPais(final String aNome, final boolean isAtivo) {
        final var umId = PaisID.unique();
        final var agora = InstantUtils.now();
        final var excluidoEm = isAtivo ? null : agora;
        return new Pais(umId, aNome, isAtivo, agora, agora, excluidoEm);
    }

    @Override
    public void validate(ValidationHandler handler) {
        new PaisValidator(this, handler).validate();
    }

    public Pais desativa() {
        if (getExcluidoEm() == null) {
            this.excluidoEm = InstantUtils.now();
        }
        this.ativo = false;
        this.atualizadoEm = InstantUtils.now();
        return this;
    }

    public Pais ativa() {
        this.excluidoEm = null;
        this.ativo = true;
        this.atualizadoEm = InstantUtils.now();
        return this;
    }

    public Pais atualiza(final String umNome, final boolean isAtivo) {
        if (isAtivo) {
            ativa();
        } else {
            desativa();
        }
        this.nome = umNome;
        this.atualizadoEm = InstantUtils.now();
        selfValidate();
        return this;
    }

    public static Pais with(
            final PaisID anId,
            final String nome,
            final boolean ativo,
            final Instant criadoEm,
            final Instant atualizadoEm,
            final Instant excluidoEm
    ) {
        return new Pais(
                anId,
                nome,
                ativo,
                criadoEm,
                atualizadoEm,
                excluidoEm
        );
    }

    public static Pais with(final Pais umPais) {
        return with(
                umPais.id,
                umPais.nome,
                umPais.ativo,
                umPais.criadoEm,
                umPais.atualizadoEm,
                umPais.excluidoEm
        );
    }

    public String getNome() {
        return nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public Instant getExcluidoEm() {
        return excluidoEm;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Falha na criacao do Agregado Pais", notification);
        }
    }
}
