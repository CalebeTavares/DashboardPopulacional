package br.com.dashboardpopulacional.exception;

public class ErroAplicacaoException extends Exception {

    public ErroAplicacaoException(String mensagem) {
        super(mensagem);
    }

    public ErroAplicacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}