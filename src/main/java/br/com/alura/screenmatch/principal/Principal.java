package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3860ec38";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca:");
        var nomeSerie = leitura.nextLine();

        String fullAddress =
                this.ENDERECO + nomeSerie.replace(" ", "+") + this.API_KEY;

        System.out.println("Full Address: " + fullAddress);

        var json = this.consumoApi.obterDados(fullAddress);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            String fullAddressWithSeason = this.ENDERECO +
                    nomeSerie.replace(" ", "+") +
                    "&season=" + i +
                    this.API_KEY;

            json = consumoApi.obterDados(fullAddressWithSeason);
            DadosTemporada dadosTemporada = conversor.obterDados(json,
                    DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);

        // Lambdas (funções anônimas que são chamadas uma única vez)
        temporadas.forEach(temporada ->
                temporada.episodios().forEach(episodio ->
                        System.out.println(episodio.titulo())));

//        List<String> nomes = Arrays.asList("Vitor", "João", "Heitor",
//                "Guilherme", "Yuri");
//
//        // Streams (fluxo de dados)
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("G"))
//                .map(String::toUpperCase)
//                .forEach(System.out::println);

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                        .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .collect(Collectors.toList())
                .forEach(System.out::println);

        System.out.println("EPISÓDIOS: ");

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))).collect(Collectors.toList());

        episodios.forEach(System.out::println);
    }
}
