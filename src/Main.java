import model.Entrada;
import model.Estado;
import model.Municipio;
import model.Populacao;
import util.LeitorArquivo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> municipios = new ArrayList<>();

        municipios.add("Morrinhos");
        municipios.add("Goiânia");
        municipios.add("Anápolis");
        municipios.add("Rio Verde");
        municipios.add("Catalão");

        System.out.println("Municípios cadastrados:");

        for (String municipio : municipios) {
            System.out.println(municipio);
        }

        System.out.println("\nUsando for tradicional:");

        for (int i = 0; i < municipios.size(); i++) {
            System.out.println(municipios.get(i));
        }

        Collections.sort(municipios);

        System.out.println("\nMunicípios em ordem alfabética:");

        for (String municipio : municipios) {
            System.out.println(municipio);
        }

        int quantidade = municipios.size();

        System.out.println("\nQuantidade de municípios: " + quantidade);

        if (quantidade > 3) {
            System.out.println("Existem mais de 3 municípios.");
        }

        if (quantidade > 0) {
            System.out.println("A lista possui municípios.");
        } else {
            System.out.println("A lista está vazia.");
        }

        int contador = 0;

        while (contador < 3) {
            System.out.println("While: " + contador);
            contador++;
        }

        int numero = 0;

        do {
            System.out.println("Do while executado.");
            numero++;
        } while (numero < 1);

        int opcao = 1;

        switch (opcao) {
            case 1:
                System.out.println("Opção escolhida: visualizar municípios.");
                break;

            case 2:
                System.out.println("Opção escolhida: cadastrar município.");
                break;

            case 3:
                System.out.println("Opção escolhida: sair.");
                break;

            default:
                System.out.println("Opção inválida.");
        }

        LinkedList<String> cidades = new LinkedList<>();

        cidades.add("Morrinhos");
        cidades.add("Goiânia");
        cidades.add("Anápolis");

        System.out.println("\nLinkedList:");

        for (String cidade : cidades) {
            System.out.println(cidade);
        }

        HashMap<String, Integer> populacoes = new HashMap<>();

        populacoes.put("Morrinhos", 47200);
        populacoes.put("Goiânia", 1565836);
        populacoes.put("Anápolis", 398869);

        System.out.println("\nHashMap:");

        for (String cidade : populacoes.keySet()) {
            System.out.println(cidade + ": " + populacoes.get(cidade));
        }

        System.out.println("\nDados do arquivo:");

        List<String> linhas = LeitorArquivo.lerArquivo("dados/populacao.txt");

        for (String linha : linhas) {
            System.out.println(linha);
        }

        List<String[]> dados = LeitorArquivo.separarLinhas(linhas);

        System.out.println("\nDados separados:");

        for (String[] linha : dados) {
            System.out.println(
                    "Estado: " + linha[0] +
                            " | Município: " + linha[1] +
                            " | Ano: " + linha[2] +
                            " | População: " + linha[3]
            );
        }

        List<Entrada> entradas = new ArrayList<>();

        for (String[] linha : dados) {

            String estado = linha[0];
            String municipio = linha[1];
            int ano = Integer.parseInt(linha[2]);
            int populacao = Integer.parseInt(linha[3]);

            Entrada entrada = new Entrada(
                    estado,
                    municipio,
                    ano,
                    populacao,
                    0
            );

            entradas.add(entrada);
        }

        System.out.println("\nObjetos Entrada:");

        for (Entrada entrada : entradas) {
            System.out.println(
                    entrada.getEstado() +
                            " | " +
                            entrada.getMunicipio() +
                            " | " +
                            entrada.getAno() +
                            " | " +
                            entrada.getPopulacao()
            );
        }

        Estado goias = new Estado("Goiás", "GO", 340086);

        Municipio morrinhos = new Municipio(
                "Morrinhos",
                "GO",
                2846
        );

        goias.adicionarMunicipio(morrinhos);

        System.out.println("\nAssociação:");

        System.out.println("Estado: " + goias.getNome());
        System.out.println("Município: " + morrinhos.getNome());

        System.out.println("\nAgregação:");

        for (Municipio municipio : goias.getMunicipios()) {
            System.out.println(
                    "Município pertencente ao estado: " +
                            municipio.getNome()
            );
        }

        morrinhos.adicionarPopulacao(2020, 46600);
        morrinhos.adicionarPopulacao(2021, 46900);
        morrinhos.adicionarPopulacao(2022, 47200);

        System.out.println("\nComposição:");

        for (Populacao populacao : morrinhos.getPopulacoes()) {
            System.out.println(
                    "Ano: " + populacao.getAno() +
                            " | População: " + populacao.getQuantidade()
            );
        }
    }
}