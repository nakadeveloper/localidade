package com.fnaka.localidade.application.estado.cria;

public record CriaEstadoCommand(
        String nome,
        String uf,
        String paisId,
        boolean ativo
) {

    public static CriaEstadoCommand with(
            final String umNome, final String umaUf, final String anPaisId, final Boolean isAtivo
    ) {
        return new CriaEstadoCommand(
                umNome,
                umaUf,
                anPaisId,
                isAtivo != null ? isAtivo : true
        );
    }
}
