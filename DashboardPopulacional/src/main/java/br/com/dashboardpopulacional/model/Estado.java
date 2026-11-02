package br.com.dashboardpopulacional.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estado extends Regiao {

    private String sigla;
    private List<Municipio> municipios;

    public Estado(String sigla, String nome) {
        super(nome);
        setSigla(sigla);
        this.municipios = new ArrayList<>();
    }

    public void adicionarMunicipio(Municipio municipio) {
        if (municipio == null) {
            throw new IllegalArgumentException("O município é obrigatório.");
        }
        municipios.add(municipio);
    }

    public String getSigla() {
        return sigla;
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

    public void setMunicipios(List<Municipio> municipios) {
        if (municipios == null) {
            throw new IllegalArgumentException("A lista de municípios é obrigatória.");
        }
        this.municipios = new ArrayList<>(municipios);
    }

    @Override
    public String getTipo() {
        return "Estado";
    }
}