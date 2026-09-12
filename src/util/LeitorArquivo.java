package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorArquivo {

    public static List<String> lerArquivo(String caminho) {

        List<String> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {

            String linha;

            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return linhas;
    }

    public static List<String[]> separarLinhas(List<String> linhas) {

        List<String[]> dados = new ArrayList<>();

        for (String linha : linhas) {
            String[] partes = linha.split(";");
            dados.add(partes);
        }

        return dados;
    }
}