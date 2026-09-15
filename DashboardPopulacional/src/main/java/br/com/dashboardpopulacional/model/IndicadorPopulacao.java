package br.com.dashboardpopulacional.model;

public class IndicadorPopulacao extends Indicador {

    public IndicadorPopulacao(int quantidade) {
        super("População", quantidade);
    }

    @Override
    public String formatar() {
        return getNome() + ": " + String.format("%.0f habitantes", getValor());
    }
}
