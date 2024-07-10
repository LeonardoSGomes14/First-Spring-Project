package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner read = new Scanner(System.in);
    private ApiConsumption consumption = new ApiConsumption();
    private DataConvert converter = new DataConvert();

    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b740336e";

    public void showMenu() {
        System.out.println("Type the name of the series to search.");
        var seriesName = read.nextLine();
        var json = consumption.getData(ADDRESS + seriesName.replace(" ", "+") + API_KEY);
        SeriesData data = converter.getData(json, SeriesData.class);

        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();

        for(int i = 1; i<=data.totalSeasons(); i++) {
            json = consumption.getData(ADDRESS + seriesName.replace(" ", "+") +"&season=" + i + API_KEY);
            SeasonData seasonData = converter.getData(json, SeasonData.class);
            seasons.add(seasonData);

        }
        seasons.forEach(System.out::println);

//
//        for (int i = 0; i < data.totalSeasons(); i++) {
//            List<EpisodeData> episodeSeason = seasons.get(i).episodes();
//            for (int j = 0; j < episodeSeason.size(); j++) {
//                System.out.println(episodeSeason.get(j).title());
//            }
//        }
        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));
        seasons.forEach(System.out::println);
    }
}

