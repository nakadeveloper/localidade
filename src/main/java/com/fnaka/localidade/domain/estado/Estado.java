package com.fnaka.localidade.domain.estado;

import com.fnaka.localidade.domain.AggregateRoot;
import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pais.PaisID;
import com.fnaka.localidade.domain.utils.InstantUtils;
import com.fnaka.localidade.domain.validation.ValidationHandler;
import com.fnaka.localidade.domain.validation.handler.Notification;

import java.time.Instant;

public class Estado extends AggregateRoot<EstadoID> {

    private String nome;
    private String uf;
    private boolean ativo;
    private PaisID paisId;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant excluidoEm;

    protected Estado(
            final EstadoID estadoID,
            final String aNome,
            final String aUf,
            final boolean isAtivo,
            final PaisID aPaisId,
            final Instant criadoEm,
            final Instant atualizadoEm,
            final Instant excluidoEm
    ) {
        super(estadoID);
        this.nome = aNome;
        this.uf = aUf;
        this.ativo = isAtivo;
        this.paisId = aPaisId;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.excluidoEm = excluidoEm;
        selfValidate();
    }

    public static Estado newEstado(
            final String aNome,
            final String aUf,
            final PaisID aPaisId,
            final boolean isAtivo
    ) {
        final var anId = EstadoID.unique();
        final var now = InstantUtils.now();
        final var excluidoEm = isAtivo ? null : now;
        return new Estado(anId, aNome, aUf, isAtivo, aPaisId, now, now, excluidoEm);
    }

    public static Estado with(
            final EstadoID anId,
            final String umNome,
            final String umaUf,
            final boolean isAtivo,
            final PaisID aPaisId,
            final Instant criadoEm,
            final Instant atualizadoEm,
            final Instant excluidoEm
    ) {
        return new Estado(anId, umNome, umaUf, isAtivo, aPaisId, criadoEm, atualizadoEm, excluidoEm);
    }

    public static Estado with(final Estado umEstado) {
        return new Estado(
                umEstado.id,
                umEstado.nome,
                umEstado.uf,
                umEstado.ativo,
                umEstado.paisId,
                umEstado.criadoEm,
                umEstado.atualizadoEm,
                umEstado.excluidoEm
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new EstadoValidator(this, handler).validate();
    }

    public Estado desativa() {
        if (getExcluidoEm() == null) {
            this.excluidoEm = InstantUtils.now();
        }
        this.ativo = false;
        this.atualizadoEm = InstantUtils.now();
        return this;
    }

    public Estado ativa() {
        this.excluidoEm = null;
        this.ativo = true;
        this.atualizadoEm = InstantUtils.now();
        return this;
    }

    public String getNome() {
        return nome;
    }

    public String getUf() {
        return uf;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public PaisID getPaisId() {
        return paisId;
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
            throw new NotificationException("Falha na criacao do Agregado Estado", notification);
        }
    }

    public Estado atualiza(String umNome, String umaUf, PaisID umPaisId, boolean isAtivo) {
        if (isAtivo) {
            ativa();
        } else {
            desativa();
        }
        this.nome = umNome;
        this.uf = umaUf;
        this.paisId = umPaisId;
        this.atualizadoEm = InstantUtils.now();
        selfValidate();
        return this;
    }
}
