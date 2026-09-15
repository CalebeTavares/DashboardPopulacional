package br.com.dashboardpopulacional.model;

public class Entrada {

    private String siglaEstado;
    private String nomeMunicipio;
    private int ano;
    private int populacao;
    private double rendaPerCapita;
    private double idh;

    public Entrada(String siglaEstado, String nomeMunicipio, int ano,
                   int populacao, double rendaPerCapita, double idh) {
        this.siglaEstado = validarTexto(siglaEstado, "estado");
        this.nomeMunicipio = validarTexto(nomeMunicipio, "município");
        if (ano <= 0 || populacao < 0 || rendaPerCapita < 0 || idh < 0 || idh > 1) {
            throw new IllegalArgumentException("Valores numéricos inválidos para a entrada.");
        }
        this.ano = ano;
        this.populacao = populacao;
        this.rendaPerCapita = rendaPerCapita;
        this.idh = idh;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O campo " + campo + " é obrigatório.");
        }
        return valor.trim();
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public int getAno() {
        return ano;
    }

    public int getPopulacao() {
        return populacao;
    }

    public double getRendaPerCapita() {
        return rendaPerCapita;
    }

    public double getIdh() {
        return idh;
    }

    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = validarTexto(siglaEstado, "estado");
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = validarTexto(nomeMunicipio, "município");
    }

    public void setAno(int ano) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser positivo.");
        }
        this.ano = ano;
    }

    public void setPopulacao(int populacao) {
        if (populacao < 0) {
            throw new IllegalArgumentException("A população não pode ser negativa.");
        }
        this.populacao = populacao;
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
}
