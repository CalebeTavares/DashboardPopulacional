package br.com.dashboardpopulacional.exception;

public class ArquivoInvalidoException extends Exception {

    public ArquivoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public ArquivoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}