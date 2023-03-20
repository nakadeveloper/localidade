package com.fnaka.localidade.infrastructure.api;

import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisOutput;
import com.fnaka.localidade.application.pais.cria.CriaPaisOutput;
import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.infrastructure.pais.models.AtualizaPaisRequest;
import com.fnaka.localidade.infrastructure.pais.models.CriaPaisRequest;
import com.fnaka.localidade.infrastructure.pais.models.ListaPaisesResponse;
import com.fnaka.localidade.infrastructure.pais.models.PaisResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "paises")
@Tag(name = "Paises")
public interface PaisAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Cria um novo Pais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Um erro de validacao foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor"),
    })
    ResponseEntity<CriaPaisOutput> criaPais(@RequestBody CriaPaisRequest input);

    @GetMapping
    @Operation(summary = "Lista todos paises paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Filtro invalido"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor"),
    })
    Pagination<ListaPaisesResponse> listaPaises(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "nome") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Busca um pais pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pais consultado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pais nao foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor"),
    })
    PaisResponse buscaPorId(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Atualiza um pais pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pais atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pais nao encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor"),
    })
    ResponseEntity<AtualizaPaisOutput> atualiza(@PathVariable(name = "id") String id, @RequestBody AtualizaPaisRequest input);
}
