package com.fnaka.localidade.infrastructure.pais.persistence;

import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity(name = "Pais")
@Table(name = "paises")
public class PaisJpaEntity {

    @Id
    private String id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Column(name = "criado_em", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant atualizadoEm;

    @Column(name = "excluido_em", columnDefinition = "DATETIME(6)")
    private Instant excluidoEm;

    public PaisJpaEntity(){}

    private PaisJpaEntity(
            final String id,
            final String nome,
            final boolean ativo,
            final Instant criadoEm,
            final Instant atualizadoEm,
            final Instant excluidoEm
    ) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.excluidoEm = excluidoEm;
    }

    public static PaisJpaEntity from(final Pais umPais) {
        return new PaisJpaEntity(
                umPais.getId().getValue(),
                umPais.getNome(),
                umPais.isAtivo(),
                umPais.getCriadoEm(),
                umPais.getAtualizadoEm(),
                umPais.getExcluidoEm()
        );
    }

    public Pais toAggregate() {
        return Pais.with(
                PaisID.from(getId()),
                getNome(),
                isAtivo(),
                getCriadoEm(),
                getAtualizadoEm(),
                getExcluidoEm()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(Instant atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Instant getExcluidoEm() {
        return excluidoEm;
    }

    public void setExcluidoEm(Instant excluidoEm) {
        this.excluidoEm = excluidoEm;
    }
}
