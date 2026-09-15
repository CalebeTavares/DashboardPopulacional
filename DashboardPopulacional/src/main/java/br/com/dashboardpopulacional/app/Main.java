package br.com.dashboardpopulacional.app;

import br.com.dashboardpopulacional.model.Entrada;
import br.com.dashboardpopulacional.model.Estado;
import br.com.dashboardpopulacional.model.Indicador;
import br.com.dashboardpopulacional.model.IndicadorPopulacao;
import br.com.dashboardpopulacional.model.IndicadorSocioeconomico;
import br.com.dashboardpopulacional.model.Municipio;
import br.com.dashboardpopulacional.service.CatalogoPopulacional;
import br.com.dashboardpopulacional.util.LeitorArquivo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Path caminho = Path.of("dados", "populacao.csv");

        try {
            List<Entrada> entradas = LeitorArquivo.ler(caminho);
            CatalogoPopulacional catalogo = new CatalogoPopulacional(entradas);
            executarMenu(catalogo);
        } catch (IOException | IllegalArgumentException erro) {
            System.err.println("Não foi possível carregar os dados: " + erro.getMessage());
            System.exit(1);
        }
    }

    private static void executarMenu(CatalogoPopulacional catalogo) {
        LinkedList<Municipio> municipios = new LinkedList<>(catalogo.listarMunicipiosPorPopulacao());

        try (Scanner scanner = new Scanner(System.in)) {
            int opcao;
            do {
                exibirMenu(municipios);
                opcao = lerOpcao(scanner);

                switch (opcao) {
                    case 0 -> System.out.println("\nConsulta encerrada.");
                    case 6 -> exibirTodosDados(catalogo.listarEstados());
                    default -> consultarMunicipio(opcao, municipios);
                }
            } while (opcao != 0);
        }
    }

    private static void exibirMenu(List<Municipio> municipios) {
        System.out.println("\n=== Consulta Populacional ===");
        System.out.println("Escolha uma cidade pelo número:");

        for (int indice = 0; indice < municipios.size(); indice++) {
            System.out.println((indice + 1) + " - " + municipios.get(indice).getNome());
        }

        System.out.println("6 - Apresentar todos os dados");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
    }

    private static int lerOpcao(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Digite somente o número de uma opção.");
            scanner.next();
            System.out.print("Opção: ");
        }
        return scanner.nextInt();
    }

    private static void consultarMunicipio(int opcao, List<Municipio> municipios) {
        if (opcao < 1 || opcao > municipios.size()) {
            System.out.println("Opção inválida. Escolha um número da lista.");
            return;
        }

        Municipio municipio = municipios.get(opcao - 1);
        System.out.println("\n=== Dados de " + municipio.getNome() + " ===");
        exibirDadosMunicipio(municipio);
    }

    private static void exibirTodosDados(List<Estado> estados) {
        System.out.println("\n=== Todos os dados ===");
        for (Estado estado : estados) {
            System.out.println("\nEstado: " + estado.getNome() + " (" + estado.getSigla() + ")");
            for (Municipio municipio : estado.getMunicipios()) {
                exibirDadosMunicipio(municipio);
            }
        }
    }

    private static void exibirDadosMunicipio(Municipio municipio) {
        System.out.println("- Município: " + municipio.getNome());
        System.out.println("  População mais recente: " + municipio.obterPopulacaoMaisRecente());
        System.out.println("  Renda per capita: R$ " + municipio.getRendaPerCapita());
        System.out.println("  IDH: " + municipio.getIdh());

        List<Indicador> indicadores = new ArrayList<>();
        indicadores.add(new IndicadorPopulacao(municipio.obterPopulacaoMaisRecente()));
        indicadores.add(new IndicadorSocioeconomico(
                "IDH de " + municipio.getNome(),
                municipio.getIdh()
        ));

        System.out.println("  Indicadores:");
        for (Indicador indicador : indicadores) {
            System.out.println("  * " + indicador.formatar());
        }

        System.out.println("  Histórico populacional:");
        for (var populacao : municipio.getPopulacoes()) {
            System.out.println("  * " + populacao.getAno() + ": " + populacao.getQuantidade());
        }
    }
}
