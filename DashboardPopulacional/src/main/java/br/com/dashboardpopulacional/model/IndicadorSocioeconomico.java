package br.com.dashboardpopulacional.model;

public class IndicadorSocioeconomico extends Indicador {

    public IndicadorSocioeconomico(String nome, double valor) {
        super(nome, valor);
    }

    @Override
    public String formatar() {
        return getNome() + ": " + String.format("%.3f", getValor());
    }
}
