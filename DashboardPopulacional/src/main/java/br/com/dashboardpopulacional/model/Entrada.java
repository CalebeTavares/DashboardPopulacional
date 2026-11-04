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

        validarAno(ano);
        validarPopulacao(populacao);
        validarRenda(rendaPerCapita);
        validarIdh(idh);

        this.ano = ano;
        this.populacao = populacao;
        this.rendaPerCapita = rendaPerCapita;
        this.idh = idh;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "O campo " + campo + " é obrigatório."
            );
        }

        return valor.trim();
    }

    private static void validarAno(int ano) {
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser positivo.");
        }
    }

    private static void validarPopulacao(int populacao) {
        if (populacao < 0) {
            throw new IllegalArgumentException(
                    "A população não pode ser negativa."
            );
        }
    }

    private static void validarRenda(double renda) {
        if (!Double.isFinite(renda) || renda < 0) {
            throw new IllegalArgumentException(
                    "A renda per capita deve ser válida e não negativa."
            );
        }
    }

    private static void validarIdh(double idh) {
        if (!Double.isFinite(idh) || idh < 0 || idh > 1) {
            throw new IllegalArgumentException(
                    "O IDH deve estar entre 0 e 1."
            );
        }
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
        validarAno(ano);
        this.ano = ano;
    }

    public void setPopulacao(int populacao) {
        validarPopulacao(populacao);
        this.populacao = populacao;
    }

    public void setRendaPerCapita(double rendaPerCapita) {
        validarRenda(rendaPerCapita);
        this.rendaPerCapita = rendaPerCapita;
    }

    public void setIdh(double idh) {
        validarIdh(idh);
        this.idh = idh;
    }
}