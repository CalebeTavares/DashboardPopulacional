package br.com.dashboardpopulacional.service;

import br.com.dashboardpopulacional.exception.PersistenciaException;
import br.com.dashboardpopulacional.model.Entrada;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPopulacional {

    private static final String URL = "jdbc:sqlite:dados/populacao.db";

    public void salvarTodos(List<Entrada> entradas)
            throws PersistenciaException {

        try {
            Files.createDirectories(Path.of("dados"));

            try (Connection conexao = DriverManager.getConnection(URL)) {
                criarTabela(conexao);

                String sql = """
                        INSERT INTO populacao (
                            sigla_estado,
                            nome_municipio,
                            ano,
                            populacao,
                            renda_per_capita,
                            idh
                        ) VALUES (?, ?, ?, ?, ?, ?)
                        ON CONFLICT(sigla_estado, nome_municipio, ano)
                        DO UPDATE SET
                            populacao = excluded.populacao,
                            renda_per_capita = excluded.renda_per_capita,
                            idh = excluded.idh
                        """;

                conexao.setAutoCommit(false);

                try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                    for (Entrada entrada : entradas) {
                        comando.setString(1, entrada.getSiglaEstado());
                        comando.setString(2, entrada.getNomeMunicipio());
                        comando.setInt(3, entrada.getAno());
                        comando.setInt(4, entrada.getPopulacao());
                        comando.setDouble(5, entrada.getRendaPerCapita());
                        comando.setDouble(6, entrada.getIdh());
                        comando.addBatch();
                    }

                    comando.executeBatch();
                    conexao.commit();

                } catch (SQLException erro) {
                    conexao.rollback();
                    throw erro;
                }
            }

        } catch (SQLException | IOException erro) {
            throw new PersistenciaException(
                    "Não foi possível salvar os dados no banco.",
                    erro
            );
        }
    }

    public List<Entrada> buscarTodos() throws PersistenciaException {
        List<Entrada> entradas = new ArrayList<>();

        String sql = """
                SELECT sigla_estado, nome_municipio, ano,
                       populacao, renda_per_capita, idh
                FROM populacao
                ORDER BY nome_municipio
                """;

        try (Connection conexao = DriverManager.getConnection(URL)) {
            criarTabela(conexao);

            try (Statement comando = conexao.createStatement();
                 ResultSet resultado = comando.executeQuery(sql)) {

                while (resultado.next()) {
                    Entrada entrada = new Entrada(
                            resultado.getString("sigla_estado"),
                            resultado.getString("nome_municipio"),
                            resultado.getInt("ano"),
                            resultado.getInt("populacao"),
                            resultado.getDouble("renda_per_capita"),
                            resultado.getDouble("idh")
                    );

                    entradas.add(entrada);
                }
            }

            return entradas;

        } catch (SQLException erro) {
            throw new PersistenciaException(
                    "Não foi possível buscar os dados no banco.",
                    erro
            );
        }
    }

    private void criarTabela(Connection conexao) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS populacao (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sigla_estado TEXT NOT NULL,
                    nome_municipio TEXT NOT NULL,
                    ano INTEGER NOT NULL,
                    populacao INTEGER NOT NULL,
                    renda_per_capita REAL NOT NULL,
                    idh REAL NOT NULL,
                    UNIQUE(sigla_estado, nome_municipio, ano)
                )
                """;

        try (Statement comando = conexao.createStatement()) {
            comando.execute(sql);
        }
    }
}