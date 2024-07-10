package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodeData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SeriesData;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConvert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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


        for (int i = 0; i < data.totalSeasons(); i++) {
            List<EpisodeData> episodeSeason = seasons.get(i).episodes();
            for (int j = 0; j < episodeSeason.size(); j++) {
                System.out.println(episodeSeason.get(j).title());
            }
        }
        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodeData = seasons.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

//        System.out.println("\nTop 10 episodes");
//        episodeData.stream()
//                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("First filter(N/A) " + e))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .peek(e -> System.out.println("ordernation " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limit " + e))
//                .map(e -> e.title().toUpperCase())
//                .peek(e -> System.out.println("Mapping " + e))
//                .forEach(System.out::println);

        List<Episode> episodes =  seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episode(t.number(), d))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

//        System.out.println("Type an excerpt from an episode title ");
//        var excerptTitle = read.nextLine();
//        Optional<Episode> searchedEpisode = episodes.stream()
//                .filter(e -> e.getTitle().toUpperCase().contains(excerptTitle.toUpperCase()))
//                .findFirst();
//        if(searchedEpisode.isPresent()){
//            System.out.println("Episode found!");
//            System.out.println("Season: " + searchedEpisode.get().getSeason());
//        } else {
//            System.out.println("Episode not found!");
//        }

//        System.out.println("What year do you want to watch the episodes from?");
//        var year = read.nextInt();
//        read.nextLine();
//
//        LocalDate searhDate = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getLaunchDate() != null && e.getLaunchDate() .isAfter(searhDate))
//                .forEach(e -> System.out.println(
//                        "Season: " + e.getSeason() +
//                                " Episode: " + e.getTitle() +
//                                " Release Date: " + e.getLaunchDate().format(formatter)
//                ));


        Map<Integer, Double> ratingPerSeason = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));
        System.out.println(ratingPerSeason);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("Avarage: " + est.getAverage());
        System.out.println("Best episode: " + est.getMax());
        System.out.println("worst episode: " + est.getMin());
        System.out.println("Amount: " + est.getCount());
    }
}

