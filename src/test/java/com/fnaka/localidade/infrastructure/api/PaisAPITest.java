package com.fnaka.localidade.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnaka.localidade.ControllerTest;
import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisCommand;
import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisOutput;
import com.fnaka.localidade.application.pais.atualiza.AtualizaPaisUseCase;
import com.fnaka.localidade.application.pais.consulta.busca.BuscaPaisPorIdUseCase;
import com.fnaka.localidade.application.pais.consulta.busca.PaisOutput;
import com.fnaka.localidade.application.pais.consulta.lista.ListaPaisesOutput;
import com.fnaka.localidade.application.pais.consulta.lista.ListaPaisesUseCase;
import com.fnaka.localidade.application.pais.cria.CriaPaisCommand;
import com.fnaka.localidade.application.pais.cria.CriaPaisOutput;
import com.fnaka.localidade.application.pais.cria.CriaPaisUseCase;
import com.fnaka.localidade.Fixture;
import com.fnaka.localidade.domain.exceptions.NotFoundException;
import com.fnaka.localidade.domain.exceptions.NotificationException;
import com.fnaka.localidade.domain.pagination.Pagination;
import com.fnaka.localidade.domain.pagination.SearchQuery;
import com.fnaka.localidade.domain.pais.Pais;
import com.fnaka.localidade.domain.pais.PaisID;
import com.fnaka.localidade.domain.validation.Error;
import com.fnaka.localidade.domain.validation.handler.Notification;
import com.fnaka.localidade.infrastructure.pais.models.AtualizaPaisRequest;
import com.fnaka.localidade.infrastructure.pais.models.CriaPaisRequest;
import io.vavr.API;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.vavr.API.Right;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = PaisAPI.class)
class PaisAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CriaPaisUseCase criaPaisUseCase;

    @MockBean
    private BuscaPaisPorIdUseCase buscaPaisPorIdUseCase;

    @MockBean
    private AtualizaPaisUseCase atualizaPaisUseCase;

    @MockBean
    private ListaPaisesUseCase listaPaisesUseCase;

    @Test
    void givenAValidCommand_whenCallsCriaPais_shouldReturnPaisId() throws Exception {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedAtivo = true;
        final var expectedId = "123";

        final var aCommand = new CriaPaisRequest(expectedNome, expectedAtivo);

        when(criaPaisUseCase.execute(any()))
                .thenReturn(Right(CriaPaisOutput.from(expectedId)));

        // when
        final var aRequest = post("/paises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/paises/%s".formatted(expectedId)))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        final var captor = ArgumentCaptor.forClass(CriaPaisCommand.class);

        verify(criaPaisUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();
        assertEquals(expectedNome, actualCommand.nome());
        assertEquals(expectedAtivo, actualCommand.ativo());
    }

    @Test
    void givenAnInvalidNome_whenCallsCriaPais_shouldReturnNotification() throws Exception {
        // given
        final String expectedNome = null;
        final var expectedAtivo = true;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var aCommand = new CriaPaisRequest(expectedNome, expectedAtivo);

        when(criaPaisUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = post("/paises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(LOCATION, nullValue()))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        final var captor = ArgumentCaptor.forClass(CriaPaisCommand.class);

        verify(criaPaisUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();
        assertNull(actualCommand.nome());
        assertEquals(expectedAtivo, actualCommand.ativo());
    }

    @Test
    void givenAValidId_whenCallsBuscaPaisPorId_shouldReturnPais() throws Exception {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedAtivo = true;

        final var aPais = Pais.newPais(expectedNome, expectedAtivo);

        final var expectedId = aPais.getId().getValue();

        when(buscaPaisPorIdUseCase.execute(any()))
                .thenReturn(PaisOutput.from(aPais));

        // when
        final var aRequest = get("/paises/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.nome", equalTo(expectedNome)))
                .andExpect(jsonPath("$.ativo", equalTo(expectedAtivo)))
                .andExpect(jsonPath("$.criado_em", equalTo(aPais.getCriadoEm().toString())))
                .andExpect(jsonPath("$.atualizado_em", equalTo(aPais.getAtualizadoEm().toString())))
                .andExpect(jsonPath("$.excluido_em", nullValue()));

        verify(buscaPaisPorIdUseCase).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallsBuscaPaisPorId_shouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Pais with ID 123 was not found";
        final var expectedId = PaisID.from("123");

        when(buscaPaisPorIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Pais.class, expectedId));

        // when
        final var aRequest = get("/paises/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(buscaPaisPorIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    void givenAValidCommand_whenCallsAtualizaPais_shouldReturnPaisId() throws Exception {
        // given
        final var expectedNome = Fixture.Pais.nome();
        final var expectedAtivo = true;

        final var aPais = Pais.newPais(expectedNome, expectedAtivo);
        final var expectedId = aPais.getId().getValue();

        final var aCommand = new AtualizaPaisRequest(expectedNome, expectedAtivo);

        when(atualizaPaisUseCase.execute(any()))
                .thenReturn(AtualizaPaisOutput.from(aPais));

        // when
        final var aRequest = put("/paises/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        final var captor = ArgumentCaptor.forClass(AtualizaPaisCommand.class);

        verify(atualizaPaisUseCase).execute(captor.capture());

        final var actualCommand = captor.getValue();

        assertEquals(expectedId, actualCommand.id());
        assertEquals(expectedNome, actualCommand.nome());
        assertEquals(expectedAtivo, actualCommand.ativo());
    }

    @Test
    void givenAnInvalidNome_whenCallsAtualizaPais_shouldReturnNotification() throws Exception {
        // given
        final String expectedNome = null;
        final var expectedAtivo = true;
        final var expectedErrorMessage = "'nome' nao deve ser nulo";

        final var aPais = Pais.newPais("Bras", expectedAtivo);
        final var expectedId = aPais.getId().getValue();

        final var aCommand = new AtualizaPaisRequest(expectedNome, expectedAtivo);

        when(atualizaPaisUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = put("/paises/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        final var captor = ArgumentCaptor.forClass(AtualizaPaisCommand.class);
        verify(atualizaPaisUseCase).execute(captor.capture());
        final var actualCommand = captor.getValue();

        assertEquals(expectedId, actualCommand.id());
        assertNull(actualCommand.nome());
        assertEquals(expectedAtivo, actualCommand.ativo());
    }

    @Test
    void givenValidParams_whenCallsListGenres_shouldReturnGenres() throws Exception {
        // given
        final var aPais = Pais.newPais("Brasil", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "bra";
        final var expectedSort = "nome";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListaPaisesOutput.from(aPais));

        when(listaPaisesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/paises")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aPais.getId().getValue())))
                .andExpect(jsonPath("$.items[0].nome", equalTo(aPais.getNome())))
                .andExpect(jsonPath("$.items[0].ativo", equalTo(aPais.isAtivo())))
                .andExpect(jsonPath("$.items[0].criado_em", equalTo(aPais.getCriadoEm().toString())))
                .andExpect(jsonPath("$.items[0].excluido_em", equalTo(aPais.getExcluidoEm().toString())));

        final var captor = ArgumentCaptor.forClass(SearchQuery.class);
        verify(listaPaisesUseCase).execute(captor.capture());
        final var actualQuery = captor.getValue();

        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedDirection, actualQuery.direction());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedTerms, actualQuery.terms());
    }
}
