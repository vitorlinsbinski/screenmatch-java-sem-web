package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        //temporadas.forEach(System.out::println);

        temporadas.forEach(temporada ->
                temporada.episodios().forEach(episodio ->
                        System.out.println(episodio.titulo())));
    }
}
