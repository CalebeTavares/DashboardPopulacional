package br.com.dashboardpopulacional.util;

import br.com.dashboardpopulacional.model.Entrada;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivo {

    private LeitorArquivo() {
    }

    public static List<Entrada> ler(Path caminho) throws IOException {
        List<Entrada> entradas = new ArrayList<>();
        List<String> linhas = Files.readAllLines(caminho);

        for (int indice = 1; indice < linhas.size(); indice++) {
            String linha = linhas.get(indice).trim();
            if (linha.isBlank()) {
                continue;
            }
            try {
                entradas.add(converter(linha));
            } catch (RuntimeException erro) {
                throw new IllegalArgumentException(
                        "Linha " + (indice + 1) + " inválida: " + linha,
                        erro
                );
            }
        }
        return entradas;
    }

    private static Entrada converter(String linha) {
        String[] campos = linha.split(";");
        if (campos.length != 6) {
            throw new IllegalArgumentException("A linha deve possuir 6 campos.");
        }
        return new Entrada(
                campos[0],
                campos[1],
                Integer.parseInt(campos[2]),
                Integer.parseInt(campos[3]),
                Double.parseDouble(campos[4]),
                Double.parseDouble(campos[5])
        );
    }
}
