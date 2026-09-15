package br.com.dashboardpopulacional.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estado {

    private String sigla;
    private String nome;
    private List<Municipio> municipios;

    public Estado(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
        this.municipios = new ArrayList<>();
    }

    public void adicionarMunicipio(Municipio municipio) {
        municipios.add(municipio);
    }

    public String getSigla() {
        return sigla;
    }

    public String getNome() {
        return nome;
    }

    public List<Municipio> getMunicipios() {
        return Collections.unmodifiableList(municipios);
    }

    public void setSigla(String sigla) {
        if (sigla == null || sigla.isBlank()) {
            throw new IllegalArgumentException("A sigla do estado é obrigatória.");
        }
        this.sigla = sigla.trim();
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do estado é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public void setMunicipios(List<Municipio> municipios) {
        if (municipios == null) {
            throw new IllegalArgumentException("A lista de municípios é obrigatória.");
        }
        this.municipios = new ArrayList<>(municipios);
    }
}
