package com.fnaka.localidade.application.pais.atualiza;

public record AtualizaPaisCommand(
        String id,
        String nome,
        boolean ativo
) {

    public static AtualizaPaisCommand with(
            final String id,
            final String nome,
            final Boolean isAtivo
    ) {
        return new AtualizaPaisCommand(id, nome, isAtivo != null ? isAtivo : false);
    }
}
