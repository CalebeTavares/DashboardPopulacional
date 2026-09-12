package model;

import java.util.ArrayList;
import java.util.List;

public class Dominio {

    private String nome;
    private List<Estado> estados;

    public Dominio(String nome) {
        this.nome = nome;
        this.estados = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void adicionarEstado(Estado estado) {
        estados.add(estado);
    }
}