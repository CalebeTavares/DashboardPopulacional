package br.com.dashboardpopulacional.util;

import br.com.dashboardpopulacional.exception.ArquivoInvalidoException;
import br.com.dashboardpopulacional.model.Entrada;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivo {

    private LeitorArquivo() {
    }

    public static List<Entrada> ler(Path caminho)
            throws IOException, ArquivoInvalidoException {

        if (caminho == null || !Files.exists(caminho)) {
            throw new ArquivoInvalidoException("O arquivo não foi encontrado.");
        }

        if (!Files.isRegularFile(caminho)) {
            throw new ArquivoInvalidoException("O caminho informado não é um arquivo válido.");
        }

        List<String> linhas = Files.readAllLines(caminho);

        if (linhas.isEmpty()) {
            throw new ArquivoInvalidoException("O arquivo está vazio.");
        }

        List<Entrada> entradas = new ArrayList<>();

        for (int indice = 1; indice < linhas.size(); indice++) {

            String linha = linhas.get(indice).trim();

            if (linha.isBlank()) {
                continue;
            }

            try {
                entradas.add(converter(linha));
            } catch (RuntimeException erro) {
                throw new ArquivoInvalidoException(
                        "Linha " + (indice + 1) + " inválida: " + erro.getMessage()
                );
            }
        }

        if (entradas.isEmpty()) {
            throw new ArquivoInvalidoException(
                    "O arquivo não possui registros de população."
            );
        }

        return entradas;
    }

    private static Entrada converter(String linha) {

        String[] campos = linha.split(";", -1);

        if (campos.length != 6) {
            throw new IllegalArgumentException(
                    "A linha deve possuir exatamente 6 campos."
            );
        }

        for (String campo : campos) {
            if (campo.isBlank()) {
                throw new IllegalArgumentException(
                        "Existem campos vazios na linha."
                );
            }
        }

        double renda = Double.parseDouble(
                campos[4].trim().replace(",", ".")
        );

        double idh = Double.parseDouble(
                campos[5].trim().replace(",", ".")
        );

        return new Entrada(
                campos[0].trim(),
                campos[1].trim(),
                Integer.parseInt(campos[2].trim()),
                Integer.parseInt(campos[3].trim()),
                renda,
                idh
        );
    }
}