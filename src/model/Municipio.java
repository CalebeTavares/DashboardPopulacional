package model;

import java.util.ArrayList;
import java.util.List;

public class Municipio {

    private String nome;
    private String estado;
    private double area;
    private List<Populacao> populacoes;

    public Municipio(String nome, String estado, double area) {
        this.nome = nome;
        this.estado = estado;
        this.area = area;
        this.populacoes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public List<Populacao> getPopulacoes() {
        return populacoes;
    }

    public void setPopulacoes(List<Populacao> populacoes) {
        this.populacoes = populacoes;
    }

    public void adicionarPopulacao(int ano, int quantidade) {
        Populacao populacao = new Populacao();
        populacao.setAno(ano);
        populacao.setQuantidade(quantidade);
        populacoes.add(populacao);
    }
}