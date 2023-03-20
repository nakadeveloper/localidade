package com.fnaka.localidade.infrastructure.api.controllers;

import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisCommand;
import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisOutput;
import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisUseCase;
import com.fnaka.localidade.application.pais.consulta.busca.BuscaPaisPorIdUseCase;
import com.fnaka.localidade.application.pais.consulta.lista.ListaPaisesUseCase;
import com.fnaka.localidade.application.pais.cria.CriaPaisCommand;
import com.fnaka.localidade.application.pais.cria.CriaPaisOutput;
import com.fnaka.localidade.application.pais.cria.CriaPaisUseCase;
import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;
import com.fnaka.localidade.infrastructure.api.PaisAPI;
import com.fnaka.localidade.infrastructure.pais.models.AtualizaPaisRequest;
import com.fnaka.localidade.infrastructure.pais.models.CriaPaisRequest;
import com.fnaka.localidade.infrastructure.pais.models.ListaPaisesResponse;
import com.fnaka.localidade.infrastructure.pais.models.PaisResponse;
import com.fnaka.localidade.infrastructure.pais.presenters.PaisApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PaisController implements PaisAPI {

    private final CriaPaisUseCase criaPaisUseCase;
    private final BuscaPaisPorIdUseCase buscaPaisPorIdUseCase;
    private final AtualizaPaisUseCase atualizaPaisUseCase;
    private final ListaPaisesUseCase listaPaisesUseCase;

    public PaisController(
            final CriaPaisUseCase criaPaisUseCase,
            final BuscaPaisPorIdUseCase buscaPaisPorIdUseCase,
            final AtualizaPaisUseCase atualizaPaisUseCase,
            final ListaPaisesUseCase listaPaisesUseCase
    ) {
        this.criaPaisUseCase = Objects.requireNonNull(criaPaisUseCase);
        this.buscaPaisPorIdUseCase = Objects.requireNonNull(buscaPaisPorIdUseCase);
        this.atualizaPaisUseCase = Objects.requireNonNull(atualizaPaisUseCase);
        this.listaPaisesUseCase = Objects.requireNonNull(listaPaisesUseCase);
    }

    @Override
    public ResponseEntity<CriaPaisOutput> criaPais(final CriaPaisRequest input) {
        final var aCommand = CriaPaisCommand.with(input.nome(), input.ativo());

        final var output = this.criaPaisUseCase.execute(aCommand);

        return ResponseEntity
                .created(URI.create("/paises/" + output.id()))
                .body(output);
    }

    @Override
    public Pagination<ListaPaisesResponse> listaPaises(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listaPaisesUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(PaisApiPresenter::present);
    }

    @Override
    public PaisResponse buscaPorId(final String anId) {
        return PaisApiPresenter.present(this.buscaPaisPorIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<AtualizaPaisOutput> atualiza(final String id, final AtualizaPaisRequest input) {
        final var aCommand = AtualizaPaisCommand.with(
                id,
                input.nome(),
                input.ativo()
        );

        final var output = this.atualizaPaisUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }
}
