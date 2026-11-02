package br.com.dashboardpopulacional.model;

public abstract class Regiao {

    private String nome;

    public Regiao(String nome) {
        setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da região é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public abstract String getTipo();
}