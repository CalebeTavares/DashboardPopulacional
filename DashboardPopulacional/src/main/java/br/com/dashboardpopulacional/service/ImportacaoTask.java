package br.com.dashboardpopulacional.service;

import br.com.dashboardpopulacional.model.Entrada;
import br.com.dashboardpopulacional.util.LeitorArquivo;

import javafx.concurrent.Task;

import java.nio.file.Path;
import java.util.List;

public class ImportacaoTask extends Task<List<Entrada>> {

    private final Path caminhoArquivo;

    public ImportacaoTask(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    @Override
    protected List<Entrada> call() throws Exception {
        updateProgress(0, 3);
        updateMessage("Lendo arquivo CSV...");

        List<Entrada> entradas = LeitorArquivo.ler(caminhoArquivo);

        updateProgress(1, 3);
        updateMessage("Salvando dados no banco SQLite...");

        RepositorioPopulacional repositorio = new RepositorioPopulacional();
        repositorio.salvarTodos(entradas);

        updateProgress(2, 3);
        updateMessage("Carregando dados do banco...");

        List<Entrada> resultado = repositorio.buscarTodos();

        updateProgress(3, 3);
        updateMessage("Importação concluída!");

        return resultado;
    }
}