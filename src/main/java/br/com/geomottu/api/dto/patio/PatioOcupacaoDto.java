package br.com.geomottu.api.dto.patio;

public record PatioOcupacaoDto(
        String nome,
        int ocupacaoAtual,
        int capacidadeTotal,
        double percentualOcupacao
) {
}