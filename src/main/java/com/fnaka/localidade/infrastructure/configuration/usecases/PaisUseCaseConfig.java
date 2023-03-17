package com.fnaka.localidade.infrastructure.configuration.usecases;

import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisUseCase;
import com.fnaka.localidade.application.pais.atualiza.DefaultAtualizaPaisUseCase;
import com.fnaka.localidade.application.pais.consulta.busca.BuscaPaisPorIdUseCase;
import com.fnaka.localidade.application.pais.consulta.busca.DefaultBuscaPaisPorIdUseCase;
import com.fnaka.localidade.application.pais.consulta.lista.DefaultListaPaisesUseCase;
import com.fnaka.localidade.application.pais.consulta.lista.ListaPaisesUseCase;
import com.fnaka.localidade.application.pais.cria.CriaPaisUseCase;
import com.fnaka.localidade.application.pais.cria.DefaultCriaPaisUseCase;
import com.fnaka.localidade.domain.pais.PaisGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class PaisUseCaseConfig {

    private final PaisGateway paisGateway;

    public PaisUseCaseConfig(final PaisGateway paisGateway) {
        this.paisGateway = Objects.requireNonNull(paisGateway);
    }

    @Bean
    public CriaPaisUseCase criaPaisUseCase() {
        return new DefaultCriaPaisUseCase(paisGateway);
    }

    @Bean
    public AtualizaPaisUseCase atualizaPaisUseCase() {
        return new DefaultAtualizaPaisUseCase(paisGateway);
    }

    @Bean
    public BuscaPaisPorIdUseCase buscaPaisPorIdUseCase() {
        return new DefaultBuscaPaisPorIdUseCase(paisGateway);
    }

    @Bean
    public ListaPaisesUseCase listaPaisesUseCase() {
        return new DefaultListaPaisesUseCase(paisGateway);
    }
}
