package model;

public class Entrada {

    private String estado;
    private String municipio;
    private int ano;
    private int populacao;
    private double area;

    public Entrada(String estado, String municipio, int ano,
                   int populacao, double area) {

        this.estado = estado;
        this.municipio = municipio;
        this.ano = ano;
        this.populacao = populacao;
        this.area = area;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getPopulacao() {
        return populacao;
    }

    public void setPopulacao(int populacao) {
        this.populacao = populacao;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}