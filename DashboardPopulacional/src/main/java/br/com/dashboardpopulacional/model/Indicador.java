package br.com.dashboardpopulacional.model;

public abstract class Indicador {

    private String nome;
    private double valor;

    protected Indicador(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public double getValor() {
        return valor;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do indicador é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public abstract String formatar();
}
