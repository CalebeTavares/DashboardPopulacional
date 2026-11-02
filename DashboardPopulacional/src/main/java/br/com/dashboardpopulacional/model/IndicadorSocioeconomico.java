package br.com.dashboardpopulacional.model;

public class IndicadorSocioeconomico implements Indicador {

    private String nome;
    private double valor;

    public IndicadorSocioeconomico(String nome, double valor) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do indicador é obrigatório.");
        }

        if (!Double.isFinite(valor) || valor < 0) {
            throw new IllegalArgumentException("O valor do indicador é inválido.");
        }

        this.nome = nome.trim();
        this.valor = valor;
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
        return nome + ": " + String.format("%.3f", calcular());
    }
}