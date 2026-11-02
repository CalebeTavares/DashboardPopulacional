package br.com.dashboardpopulacional.model;

public class CrescimentoPopulacional implements Indicador {

    private double populacaoInicial;
    private double populacaoFinal;

    public CrescimentoPopulacional(double populacaoInicial, double populacaoFinal) {
        if (populacaoInicial <= 0 || populacaoFinal < 0) {
            throw new IllegalArgumentException("Valores de população inválidos.");
        }

        this.populacaoInicial = populacaoInicial;
        this.populacaoFinal = populacaoFinal;
    }

    @Override
    public double calcular() {
        return ((populacaoFinal - populacaoInicial) / populacaoInicial) * 100;
    }

    @Override
    public String formatar() {
        return String.format(
                "Crescimento populacional: %.2f%%",
                calcular()
        );
    }
}