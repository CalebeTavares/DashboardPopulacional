package br.com.dashboardpopulacional.model;

public class IndicadorPopulacao implements Indicador {

    private String nome;
    private double valor;

    public IndicadorPopulacao(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("A população não pode ser negativa.");
        }

        this.nome = "População";
        this.valor = quantidade;
    }

    @Override
    public double calcular() {
        return valor;
    }

    public String getNome() {
        return nome;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String formatar() {
        return nome + ": " + String.format("%.0f habitantes", calcular());
    }
}