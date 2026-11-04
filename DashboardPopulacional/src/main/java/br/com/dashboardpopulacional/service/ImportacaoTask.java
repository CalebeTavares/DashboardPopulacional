package br.com.dashboardpopulacional.service;

import br.com.dashboardpopulacional.model.Entrada;
import br.com.dashboardpopulacional.util.LeitorArquivo;

import javafx.concurrent.Task;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ImportacaoTask extends Task<List<Entrada>> {

    private final Path caminhoArquivo;

    public ImportacaoTask(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    @Override
    protected List<Entrada> call() throws Exception {

        try {
            // 1. Leitura do arquivo CSV
            updateProgress(0, 4);
            updateMessage("Lendo arquivo CSV...");

            verificarCancelamento();

            List<Entrada> entradas =
                    LeitorArquivo.ler(caminhoArquivo);

            if (entradas.isEmpty()) {
                throw new IllegalArgumentException(
                        "O arquivo não possui dados para importar."
                );
            }

            pausar(1000);
            verificarCancelamento();

            // 2. Validação paralela dos dados
            updateMessage("Validando dados em paralelo...");

            CompletableFuture<Void> validacao =
                    CompletableFuture.runAsync(() ->
                            entradas.parallelStream().forEach(entrada -> {

                                if (entrada == null) {
                                    throw new IllegalArgumentException(
                                            "Registro nulo encontrado."
                                    );
                                }

                                if (entrada.getNomeMunicipio() == null
                                        || entrada.getNomeMunicipio().isBlank()) {
                                    throw new IllegalArgumentException(
                                            "Município inválido."
                                    );
                                }

                                if (entrada.getSiglaEstado() == null
                                        || entrada.getSiglaEstado().isBlank()) {
                                    throw new IllegalArgumentException(
                                            "Estado inválido."
                                    );
                                }

                                if (entrada.getPopulacao() < 0) {
                                    throw new IllegalArgumentException(
                                            "População negativa."
                                    );
                                }

                                if (entrada.getIdh() < 0
                                        || entrada.getIdh() > 1) {
                                    throw new IllegalArgumentException(
                                            "IDH inválido."
                                    );
                                }
                            })
                    );

            try {
                validacao.get();

            } catch (ExecutionException e) {
                throw new IllegalArgumentException(
                        "Erro durante a validação paralela.",
                        e.getCause()
                );
            }

            updateProgress(1, 4);

            pausar(1000);
            verificarCancelamento();

            // 3. Salvamento no banco de dados
            updateMessage("Salvando dados no banco...");

            RepositorioPopulacional repositorio =
                    new RepositorioPopulacional();

            repositorio.salvarTodos(entradas);

            updateProgress(2, 4);

            pausar(1000);
            verificarCancelamento();

            // 4. Consulta dos dados salvos
            updateMessage("Consultando dados...");

            List<Entrada> resultado =
                    repositorio.buscarTodos();

            updateProgress(4, 4);
            updateMessage("Importação concluída!");

            return resultado;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new Exception(
                    "A importação foi interrompida.",
                    e
            );

        } catch (Exception e) {

            updateMessage(
                    "Erro na importação: " + e.getMessage()
            );

            throw e;
        }
    }

    private void pausar(long milissegundos)
            throws InterruptedException {

        Thread.sleep(milissegundos);
    }

    private void verificarCancelamento()
            throws InterruptedException {

        if (isCancelled()
                || Thread.currentThread().isInterrupted()) {

            throw new InterruptedException(
                    "A importação foi cancelada."
            );
        }
    }
}