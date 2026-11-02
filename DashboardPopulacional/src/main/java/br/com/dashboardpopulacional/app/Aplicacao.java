package br.com.dashboardpopulacional.app;

import br.com.dashboardpopulacional.model.Entrada;
import br.com.dashboardpopulacional.model.Indicador;
import br.com.dashboardpopulacional.model.IndicadorPopulacao;
import br.com.dashboardpopulacional.model.IndicadorSocioeconomico;
import br.com.dashboardpopulacional.model.Municipio;
import br.com.dashboardpopulacional.model.Regiao;
import br.com.dashboardpopulacional.service.CatalogoPopulacional;
import br.com.dashboardpopulacional.service.ImportacaoTask;
import br.com.dashboardpopulacional.service.RepositorioPopulacional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Aplicacao extends Application {

    private List<Entrada> dados = new ArrayList<>();
    private Stage stage;

    private ProgressBar barraProcessamento;
    private Label statusProcessamento;
    private Label resultadoProcessamento;
    private Button botaoSequencial;
    private Button botaoParalelo;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        try {
            RepositorioPopulacional repositorio =
                    new RepositorioPopulacional();

            dados = repositorio.buscarTodos();

        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados", e.getMessage());
        }

        exibirTelaInicial();
    }

    private void exibirTelaInicial() {
        Label titulo = new Label("Dashboard Populacional");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitulo = new Label(
                "Sistema de análise de dados populacionais"
        );

        Button botaoDashboard = new Button("Visualizar Dashboard");
        Button botaoCarregar = new Button("Carregar Dados");
        Button botaoSair = new Button("Sair");

        botaoDashboard.setPrefWidth(220);
        botaoCarregar.setPrefWidth(220);
        botaoSair.setPrefWidth(220);

        botaoDashboard.setOnAction(e -> abrirDashboard());
        botaoCarregar.setOnAction(e -> selecionarArquivo());
        botaoSair.setOnAction(e -> stage.close());

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(
                titulo,
                subtitulo,
                botaoDashboard,
                botaoCarregar,
                botaoSair
        );

        Scene cena = new Scene(layout, 800, 550);

        stage.setTitle("Dashboard Populacional");
        stage.setScene(cena);
        stage.show();
    }

    private void selecionarArquivo() {
        FileChooser seletor = new FileChooser();
        seletor.setTitle("Selecionar arquivo CSV");

        seletor.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Arquivos CSV",
                        "*.csv"
                )
        );

        File arquivo = seletor.showOpenDialog(stage);

        if (arquivo != null) {
            carregarDados(arquivo.toPath());
        }
    }

    private void carregarDados(Path caminho) {
        ImportacaoTask tarefa = new ImportacaoTask(caminho);

        ProgressBar barra = new ProgressBar();
        barra.progressProperty().bind(tarefa.progressProperty());

        Label mensagem = new Label();
        mensagem.textProperty().bind(tarefa.messageProperty());

        VBox layout = new VBox(15, mensagem, barra);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Stage janela = new Stage();
        janela.setTitle("Importando dados");
        janela.setScene(new Scene(layout, 400, 150));
        janela.show();

        tarefa.setOnSucceeded(e -> {
            dados = tarefa.getValue();
            janela.close();

            mostrarMensagem(
                    "Importação concluída",
                    "Os dados foram importados e salvos no banco de dados."
            );
        });

        tarefa.setOnFailed(e -> {
            janela.close();

            Throwable erro = tarefa.getException();

            mostrarErro(
                    "Erro na importação",
                    erro == null
                            ? "Não foi possível importar os dados."
                            : erro.getMessage()
            );
        });

        Thread thread = new Thread(tarefa);
        thread.setDaemon(true);
        thread.setName("Thread-Importacao");
        thread.start();
    }

    private void abrirDashboard() {
        if (dados == null || dados.isEmpty()) {
            mostrarMensagem(
                    "Sem dados",
                    "Carregue um arquivo CSV antes de visualizar o dashboard."
            );
            return;
        }

        CatalogoPopulacional catalogo =
                new CatalogoPopulacional(dados);

        List<Municipio> municipios =
                catalogo.listarMunicipiosPorPopulacao();

        List<Regiao> regioes = new ArrayList<>();
        regioes.addAll(catalogo.listarEstados());
        regioes.addAll(municipios);

        int totalPopulacao = municipios.stream()
                .mapToInt(Municipio::obterPopulacaoMaisRecente)
                .sum();

        double mediaIdh = municipios.stream()
                .mapToDouble(Municipio::getIdh)
                .average()
                .orElse(0);

        String maiorMunicipio = municipios.isEmpty()
                ? "Não disponível"
                : municipios.get(0).getNome();

        Entrada maiorRenda = dados.stream()
                .max((a, b) -> Double.compare(
                        a.getRendaPerCapita(),
                        b.getRendaPerCapita()
                ))
                .orElse(null);

        String municipioMaiorRenda = maiorRenda == null
                ? "Não disponível"
                : maiorRenda.getNomeMunicipio();

        List<Indicador> indicadoresCalculados =
                new ArrayList<>();

        indicadoresCalculados.add(
                new IndicadorPopulacao(totalPopulacao)
        );

        indicadoresCalculados.add(
                new IndicadorSocioeconomico(
                        "IDH médio",
                        mediaIdh
                )
        );

        for (Municipio municipio : municipios) {
            Indicador crescimento =
                    municipio.calcularCrescimentoPopulacional();

            if (crescimento != null) {
                indicadoresCalculados.add(crescimento);
            }
        }

        VBox indicadores = new VBox(10);
        indicadores.setPadding(new Insets(15));

        for (Indicador indicador : indicadoresCalculados) {
            indicadores.getChildren().add(
                    criarIndicador(
                            "Indicador",
                            indicador.formatar()
                    )
            );
        }

        indicadores.getChildren().addAll(
                criarIndicador("Maior população", maiorMunicipio),
                criarIndicador(
                        "Maior renda per capita",
                        municipioMaiorRenda
                ),
                criarIndicador(
                        "Regiões cadastradas",
                        String.valueOf(regioes.size())
                )
        );

        TabPane abas = new TabPane();

        Tab abaPopulacao = new Tab(
                "População",
                criarGrafico(
                        "População por município",
                        "Município",
                        "Habitantes",
                        municipios,
                        Municipio::obterPopulacaoMaisRecente
                )
        );

        Tab abaRenda = new Tab(
                "Renda",
                criarGraficoEntradas(
                        "Renda per capita por município",
                        "Município",
                        "Renda",
                        Entrada::getRendaPerCapita
                )
        );

        Tab abaIdh = new Tab(
                "IDH",
                criarGraficoEntradas(
                        "IDH por município",
                        "Município",
                        "IDH",
                        Entrada::getIdh
                )
        );

        abaPopulacao.setClosable(false);
        abaRenda.setClosable(false);
        abaIdh.setClosable(false);

        abas.getTabs().addAll(
                abaPopulacao,
                abaRenda,
                abaIdh
        );

        Button voltar = new Button("Voltar");
        voltar.setOnAction(e -> exibirTelaInicial());

        VBox centro = new VBox(10);
        centro.setPadding(new Insets(10));
        centro.getChildren().addAll(
                abas,
                criarPainelProcessamento()
        );

        BorderPane layout = new BorderPane();
        layout.setTop(indicadores);
        layout.setCenter(centro);
        layout.setBottom(new HBox(voltar));

        BorderPane.setAlignment(layout.getBottom(), Pos.CENTER);
        BorderPane.setMargin(
                layout.getBottom(),
                new Insets(15)
        );

        Scene cena = new Scene(layout, 1100, 800);

        stage.setTitle("Dashboard Populacional");
        stage.setScene(cena);
        stage.show();
    }

    private VBox criarPainelProcessamento() {
        Label titulo = new Label("Demonstração de Concorrência");
        titulo.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;"
        );

        botaoSequencial =
                new Button("Executar sem concorrência");

        botaoParalelo =
                new Button("Executar com paralelismo");

        barraProcessamento = new ProgressBar(0);
        barraProcessamento.setMaxWidth(Double.MAX_VALUE);

        statusProcessamento =
                new Label("Aguardando execução...");

        resultadoProcessamento = new Label("");

        botaoSequencial.setOnAction(
                e -> executarProcessamento(false)
        );

        botaoParalelo.setOnAction(
                e -> executarProcessamento(true)
        );

        HBox botoes = new HBox(
                10,
                botaoSequencial,
                botaoParalelo
        );

        botoes.setAlignment(Pos.CENTER_LEFT);

        VBox painel = new VBox(
                10,
                titulo,
                botoes,
                statusProcessamento,
                barraProcessamento,
                resultadoProcessamento
        );

        painel.setPadding(new Insets(15));
        painel.setStyle(
                "-fx-background-color: #eeeeee; " +
                        "-fx-background-radius: 8;"
        );

        return painel;
    }

    private void executarProcessamento(boolean paralelo) {
        botaoSequencial.setDisable(true);
        botaoParalelo.setDisable(true);

        barraProcessamento.setProgress(0);
        resultadoProcessamento.setText("");

        statusProcessamento.setText(
                paralelo
                        ? "Executando tarefas em paralelo..."
                        : "Executando tarefas sequencialmente..."
        );

        Thread coordenadora = new Thread(() -> {
            final int quantidadeTarefas = 5;
            final int[] contadorCompartilhado = {0};

            long inicio = System.currentTimeMillis();

            try {
                if (paralelo) {
                    CountDownLatch finalizacao =
                            new CountDownLatch(quantidadeTarefas);

                    for (int i = 1; i <= quantidadeTarefas; i++) {
                        final int numeroTarefa = i;

                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);

                                int concluidas;

                                synchronized (contadorCompartilhado) {
                                    contadorCompartilhado[0]++;
                                    concluidas =
                                            contadorCompartilhado[0];
                                }

                                Platform.runLater(() -> {
                                    barraProcessamento.setProgress(
                                            (double) concluidas
                                                    / quantidadeTarefas
                                    );

                                    statusProcessamento.setText(
                                            "Tarefas concluídas: "
                                                    + concluidas
                                                    + "/"
                                                    + quantidadeTarefas
                                    );
                                });

                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            } finally {
                                finalizacao.countDown();
                            }
                        });

                        thread.setName(
                                "Thread-Tarefa-" + numeroTarefa
                        );

                        thread.setDaemon(true);
                        thread.start();
                    }

                    finalizacao.await();

                } else {
                    for (int i = 1; i <= quantidadeTarefas; i++) {
                        Thread.sleep(1000);

                        int concluidas;

                        synchronized (contadorCompartilhado) {
                            contadorCompartilhado[0]++;
                            concluidas = contadorCompartilhado[0];
                        }

                        Platform.runLater(() -> {
                            barraProcessamento.setProgress(
                                    (double) concluidas
                                            / quantidadeTarefas
                            );

                            statusProcessamento.setText(
                                    "Tarefas concluídas: "
                                            + concluidas
                                            + "/"
                                            + quantidadeTarefas
                            );
                        });
                    }
                }

                long duracao =
                        System.currentTimeMillis() - inicio;

                Platform.runLater(() -> {
                    resultadoProcessamento.setText(
                            "Concluído! Tarefas: "
                                    + contadorCompartilhado[0]
                                    + " | Tempo: "
                                    + duracao + " ms"
                    );

                    statusProcessamento.setText(
                            "Processamento finalizado com sucesso."
                    );

                    barraProcessamento.setProgress(1);

                    botaoSequencial.setDisable(false);
                    botaoParalelo.setDisable(false);
                });

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();

                Platform.runLater(() -> {
                    statusProcessamento.setText(
                            "Processamento interrompido."
                    );

                    botaoSequencial.setDisable(false);
                    botaoParalelo.setDisable(false);
                });
            }
        });

        coordenadora.setName("Thread-Coordenadora");
        coordenadora.setDaemon(true);
        coordenadora.start();
    }

    private BarChart<String, Number> criarGrafico(
            String titulo,
            String eixoX,
            String eixoY,
            List<Municipio> municipios,
            java.util.function.ToDoubleFunction<Municipio> funcao
    ) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(eixoX);
        yAxis.setLabel(eixoY);

        BarChart<String, Number> grafico =
                new BarChart<>(xAxis, yAxis);

        grafico.setTitle(titulo);
        grafico.setLegendVisible(false);

        XYChart.Series<String, Number> serie =
                new XYChart.Series<>();

        for (Municipio municipio : municipios) {
            serie.getData().add(
                    new XYChart.Data<>(
                            municipio.getNome(),
                            funcao.applyAsDouble(municipio)
                    )
            );
        }

        grafico.getData().add(serie);

        return grafico;
    }

    private BarChart<String, Number> criarGraficoEntradas(
            String titulo,
            String eixoX,
            String eixoY,
            java.util.function.ToDoubleFunction<Entrada> funcao
    ) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(eixoX);
        yAxis.setLabel(eixoY);

        BarChart<String, Number> grafico =
                new BarChart<>(xAxis, yAxis);

        grafico.setTitle(titulo);
        grafico.setLegendVisible(false);

        XYChart.Series<String, Number> serie =
                new XYChart.Series<>();

        for (Entrada entrada : dados) {
            serie.getData().add(
                    new XYChart.Data<>(
                            entrada.getNomeMunicipio(),
                            funcao.applyAsDouble(entrada)
                    )
            );
        }

        grafico.getData().add(serie);

        return grafico;
    }

    private Label criarIndicador(String titulo, String valor) {
        Label label = new Label(titulo + ": " + valor);

        label.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-padding: 10px; -fx-background-color: #eeeeee;"
        );

        return label;
    }

    private void mostrarMensagem(
            String titulo,
            String mensagem
    ) {
        Alert alerta =
                new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarErro(
            String titulo,
            String mensagem
    ) {
        Alert alerta =
                new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}