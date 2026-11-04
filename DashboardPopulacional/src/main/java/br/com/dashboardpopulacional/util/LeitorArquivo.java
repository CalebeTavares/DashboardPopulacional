package br.com.dashboardpopulacional.util;

import br.com.dashboardpopulacional.exception.ArquivoInvalidoException;
import br.com.dashboardpopulacional.model.Entrada;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class LeitorArquivo {

    private LeitorArquivo() {
    }

    public static List<Entrada> ler(Path caminho)
            throws IOException, ArquivoInvalidoException {

        if (caminho == null
                || !Files.isRegularFile(caminho)
                || !Files.isReadable(caminho)) {

            throw new ArquivoInvalidoException(
                    "Arquivo inexistente ou sem permissão de leitura."
            );
        }

        List<String> linhas = Files.readAllLines(caminho);

        if (linhas.size() < 2) {
            throw new ArquivoInvalidoException(
                    "O CSV está vazio ou não possui registros."
            );
        }

        List<Entrada> entradas = new ArrayList<>();

        for (int i = 1; i < linhas.size(); i++) {

            String linha = linhas.get(i).trim();

            if (linha.isEmpty()) {
                continue;
            }

            try {

                String[] campos = linha.split(";", -1);

                if (campos.length != 6) {
                    throw new IllegalArgumentException(
                            "Esperados 6 campos separados por ponto e vírgula."
                    );
                }

                for (String campo : campos) {
                    if (campo.isBlank()) {
                        throw new IllegalArgumentException(
                                "Existem campos vazios."
                        );
                    }
                }

                int ano = Integer.parseInt(campos[2].trim());
                int populacao = Integer.parseInt(campos[3].trim());

                double renda = Double.parseDouble(
                        campos[4].trim().replace(",", ".")
                );

                double idh = Double.parseDouble(
                        campos[5].trim().replace(",", ".")
                );

                if (ano < 1800 || ano > 2100) {
                    throw new IllegalArgumentException(
                            "Ano fora do intervalo permitido."
                    );
                }

                if (populacao < 0) {
                    throw new IllegalArgumentException(
                            "A população não pode ser negativa."
                    );
                }

                if (!Double.isFinite(renda) || renda < 0) {
                    throw new IllegalArgumentException(
                            "Renda inválida."
                    );
                }

                if (!Double.isFinite(idh) || idh < 0 || idh > 1) {
                    throw new IllegalArgumentException(
                            "O IDH deve estar entre 0 e 1."
                    );
                }

                entradas.add(new Entrada(
                        campos[0].trim(),
                        campos[1].trim(),
                        ano,
                        populacao,
                        renda,
                        idh
                ));

            } catch (RuntimeException e) {

                throw new ArquivoInvalidoException(
                        "Erro na linha " + (i + 1) + ": " + e.getMessage(),
                        e
                );
            }
        }

        if (entradas.isEmpty()) {
            throw new ArquivoInvalidoException(
                    "O arquivo não possui registros válidos."
            );
        }

        return entradas;
    }
}