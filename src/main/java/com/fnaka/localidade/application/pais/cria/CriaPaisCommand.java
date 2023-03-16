package com.fnaka.localidade.application.pais.cria;

public record CriaPaisCommand(
        String nome,
        boolean ativo
) {

    public static CriaPaisCommand with(final String umNome, final Boolean isAtivo) {
        return new CriaPaisCommand(
                umNome,
                isAtivo != null ? isAtivo : true
        );
    }
}
