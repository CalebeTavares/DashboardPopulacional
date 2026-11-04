package br.com.dashboardpopulacional.service;

import br.com.dashboardpopulacional.model.Entrada;
import br.com.dashboardpopulacional.model.Estado;
import br.com.dashboardpopulacional.model.Municipio;
import br.com.dashboardpopulacional.model.Populacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogoPopulacional {

    private final Map<String, Estado> estados;
    private final Map<String, Municipio> municipios;

    public CatalogoPopulacional(List<Entrada> entradas) {
        if (entradas == null || entradas.isEmpty()) {
            throw new IllegalArgumentException(
                    "A lista de entradas não pode ser nula ou vazia."
            );
        }

        estados = new HashMap<>();
        municipios = new HashMap<>();

        carregar(entradas);
    }

    private void carregar(List<Entrada> entradas) {
        for (Entrada entrada : entradas) {

            if (entrada == null) {
                throw new IllegalArgumentException(
                        "A lista contém uma entrada nula."
                );
            }

            Estado estado = estados.computeIfAbsent(
                    entrada.getSiglaEstado(),
                    sigla -> new Estado(sigla, nomeDoEstado(sigla))
            );

            String chave = entrada.getSiglaEstado()
                    + ":" + entrada.getNomeMunicipio();

            Municipio municipio = municipios.get(chave);

            if (municipio == null) {
                municipio = new Municipio(
                        entrada.getNomeMunicipio(),
                        entrada.getSiglaEstado(),
                        entrada.getRendaPerCapita(),
                        entrada.getIdh()
                );

                municipios.put(chave, municipio);
                estado.adicionarMunicipio(municipio);
            }

            municipio.adicionarPopulacao(
                    new Populacao(
                            entrada.getAno(),
                            entrada.getPopulacao()
                    )
            );
        }
    }

    public List<Estado> listarEstados() {
        return Collections.unmodifiableList(
                new ArrayList<>(estados.values())
        );
    }

    public List<Municipio> listarMunicipiosPorPopulacao() {
        List<Municipio> resultado =
                new ArrayList<>(municipios.values());

        resultado.sort(
                Comparator.comparingInt(
                        Municipio::obterPopulacaoMaisRecente
                ).reversed()
        );

        return resultado;
    }

    private String nomeDoEstado(String sigla) {
        return switch (sigla) {
            case "GO" -> "Goiás";
            case "SP" -> "São Paulo";
            default -> sigla;
        };
    }
}