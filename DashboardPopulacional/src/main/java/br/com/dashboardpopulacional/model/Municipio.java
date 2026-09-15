package br.com.dashboardpopulacional.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Municipio {

    private String nome;
    private String siglaEstado;
    private double rendaPerCapita;
    private double idh;
    private List<Populacao> populacoes;

    public Municipio(String nome, String siglaEstado, double rendaPerCapita, double idh) {
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.rendaPerCapita = rendaPerCapita;
        this.idh = idh;
        this.populacoes = new ArrayList<>();
    }

    public void adicionarPopulacao(Populacao populacao) {
        populacoes.add(populacao);
    }

    public int obterPopulacaoMaisRecente() {
        if (populacoes.isEmpty()) {
            return 0;
        }
        Populacao maisRecente = Collections.max(
                populacoes,
                java.util.Comparator.comparingInt(Populacao::getAno)
        );
        return maisRecente.getQuantidade();
    }

    public String getNome() {
        return nome;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public double getRendaPerCapita() {
        return rendaPerCapita;
    }

    public double getIdh() {
        return idh;
    }

    public List<Populacao> getPopulacoes() {
        return Collections.unmodifiableList(populacoes);
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do município é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public void setSiglaEstado(String siglaEstado) {
        if (siglaEstado == null || siglaEstado.isBlank()) {
            throw new IllegalArgumentException("A sigla do estado é obrigatória.");
        }
        this.siglaEstado = siglaEstado.trim();
    }

    public void setRendaPerCapita(double rendaPerCapita) {
        if (rendaPerCapita < 0) {
            throw new IllegalArgumentException("A renda per capita não pode ser negativa.");
        }
        this.rendaPerCapita = rendaPerCapita;
    }

    public void setIdh(double idh) {
        if (idh < 0 || idh > 1) {
            throw new IllegalArgumentException("O IDH deve estar entre 0 e 1.");
        }
        this.idh = idh;
    }

    public void setPopulacoes(List<Populacao> populacoes) {
        if (populacoes == null) {
            throw new IllegalArgumentException("A lista de populações é obrigatória.");
        }
        this.populacoes = new ArrayList<>(populacoes);
    }
}
