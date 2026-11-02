package br.com.dashboardpopulacional.model;

public class DensidadeDemografica implements Indicador {

    private double populacao;
    private double area;

    public DensidadeDemografica(double populacao, double area) {
        if (populacao < 0 || area <= 0) {
            throw new IllegalArgumentException(
                    "Valores inválidos para calcular a densidade."
            );
        }

        this.populacao = populacao;
        this.area = area;
    }

    @Override
    public double calcular() {
        return populacao / area;
    }

    @Override
    public String formatar() {
        return String.format(
                "Densidade demográfica: %.2f habitantes/km²",
                calcular()
        );
    }
}