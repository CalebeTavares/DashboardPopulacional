package br.com.dashboardpopulacional.model;

public class Populacao {

    private int ano;
    private int quantidade;

    public Populacao(int ano, int quantidade) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser positivo.");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("A população não pode ser negativa.");
        }
        this.ano = ano;
        this.quantidade = quantidade;
    }

    public int getAno() {
        return ano;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setAno(int ano) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser positivo.");
        }
        this.ano = ano;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("A população não pode ser negativa.");
        }
        this.quantidade = quantidade;
    }
}
