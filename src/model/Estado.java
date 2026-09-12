package model;

import java.util.ArrayList;
import java.util.List;

public class Estado {

    private String nome;
    private String sigla;
    private double area;
    private List<Municipio> municipios;

    public Estado(String nome, String sigla, double area) {
        this.nome = nome;
        this.sigla = sigla;
        this.area = area;
        this.municipios = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void adicionarMunicipio(Municipio municipio) {
        municipios.add(municipio);
    }
}