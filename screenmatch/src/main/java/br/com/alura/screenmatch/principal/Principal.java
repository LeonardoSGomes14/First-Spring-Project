package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.DataConvert;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner read = new Scanner(System.in);
    private ApiConsumption consumption = new ApiConsumption();
    private DataConvert converter = new DataConvert();

    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b740336e";

    private List<SeriesData> seriesData = new ArrayList<>();


    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }


    public void showMenu() {
        var option = -1;

        while (option != 0) {
            var menu = """
                    1 - Searh series
                    2 - Search episodes
                    3 - List of Searched series
                    4 - Search series by title
                    5 - Search series by actor
                    6 - Top 5 series
                    7 - Searh series by category
                    8 - Filter series by number of seasons

                    0 - Exit
                    """;

            System.out.println(menu);
            option = read.nextInt();
            read.nextLine();

            switch (option) {
                case 1:
                    searchWebSerie();
                    break;
                case 2:
                    searchEpisodeBySerie();
                    break;
                case 3:
                    listSearchedSeries();
                    break;
                case 4:
                    seacrhSeriesByTitle();
                    break;
                case 5:
                    searchSeriesByActor();
                    break;
                case 6:
                    searchTop5Series();
                    break;
                case 7:
                    searchSeriesByCategory();
                    break;
                case 8:
                    searchSeriesByTotalSeasons();
                    break;
                case 0:
                    System.out.println("leaving...");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }


    private void searchWebSerie() {
        SeriesData data = getSeriesData();
        Serie serie = new Serie(data);
        repository.save(serie);
        System.out.println(data);
    }



    private SeriesData getSeriesData() {
        System.out.println("Type the name of the series to search.");
        var seriesName = read.nextLine();
        var json = consumption.getData(ADDRESS + seriesName.replace(" ", "+") + API_KEY);
        SeriesData data = converter.getData(json, SeriesData.class);
        return data;
    }


    private void searchEpisodeBySerie() {
        listSearchedSeries();
        System.out.println("Choose a Series by name ");
        var seriesName = read.nextLine();

        Optional<Serie> serie = repository.findByTitleContainingIgnoreCase(seriesName);


        if (serie.isPresent()) {

            var foundedSerie = serie.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= foundedSerie.getTotalSeasons(); i++) {
                var  json = consumption.getData(ADDRESS + foundedSerie.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                SeasonData seasonData = converter.getData(json, SeasonData.class);
                seasons.add(seasonData);

            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodes().stream()
                            .map(e -> new Episode(d.number(), e)))
                    .collect(Collectors.toList());

            foundedSerie.setEpisodes(episodes);
            repository.save(foundedSerie);
        } else {
            System.out.println("Serie not found!");
        }
    }




    private void listSearchedSeries() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);

    }


    private void seacrhSeriesByTitle() {
        System.out.println("Choose a series by name: ");
        var seriesName = read.nextLine();
        Optional<Serie> searchedSerie = repository.findByTitleContainingIgnoreCase(seriesName);

        if (searchedSerie.isPresent()) {
            System.out.println("Series data: " + searchedSerie.get());
        } else {
            System.out.println("Series not found!");
        }
    }



    private void searchSeriesByActor() {
        System.out.println("What is the name to search?");
        var actorName = read.nextLine();
        System.out.println("Ratings starting at what value? ");
        var rating = read.nextDouble();
        List<Serie> foundedSeries = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);
        System.out.println("Series that " + actorName + " worked on: ");
        foundedSeries.forEach(s ->
                System.out.println(s.getTitle() + " rating: " + s.getRating()));
    }




    private void searchTop5Series() {
        List<Serie> serieTop = repository.findTop5ByOrderByRatingDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitle() + " rating: " + s.getRating()));
    }


    private void searchSeriesByCategory() {
        System.out.println("What category/genre do you want to search for series? ");
        var genreName = read.nextLine();
        Categoria categoria = Categoria.fromPortugues(genreName);
        List<Serie> seriesByCategoria = repository.findByGenre(categoria);
        System.out.println("series from the category of " + genreName );
        seriesByCategoria.forEach(System.out::println);
    }

    private void  searchSeriesByTotalSeasons(){
        System.out.println("How many seasons should your series have?");
        var totalSeasons = read.nextInt();
        read.nextLine();
        System.out.println("Ratings starting at what value? ");
        var rating = read.nextDouble();
        read.nextLine();
        List<Serie> foundedSeries = repository.findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(totalSeasons, rating);
        System.out.println("Series with " + totalSeasons + " seasons: " );
        foundedSeries.forEach(s ->
                System.out.println(s.getTitle() + " rating: " + s.getRating()));
    }


}










//
//
//        for (int i = 0; i < data.totalSeasons(); i++) {
//            List<EpisodeData> episodeSeason = seasons.get(i).episodes();
//            for (int j = 0; j < episodeSeason.size(); j++) {
//                System.out.println(episodeSeason.get(j).title());
//            }
//        }
//        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));
//
//        List<EpisodeData> episodeData = seasons.stream()
//                .flatMap(t -> t.episodes().stream())
//                .collect(Collectors.toList());
//
////        System.out.println("\nTop 10 episodes");
////        episodeData.stream()
////                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
////                .peek(e -> System.out.println("First filter(N/A) " + e))
////                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
////                .peek(e -> System.out.println("ordernation " + e))
////                .limit(10)
////                .peek(e -> System.out.println("Limit " + e))
////                .map(e -> e.title().toUpperCase())
////                .peek(e -> System.out.println("Mapping " + e))
////                .forEach(System.out::println);
//
//        List<Episode> episodes =  seasons.stream()
//                .flatMap(t -> t.episodes().stream()
//                        .map(d -> new Episode(t.number(), d))
//                ).collect(Collectors.toList());
//
//        episodes.forEach(System.out::println);
//
//        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));
//
////        System.out.println("Type an excerpt from an episode title ");
////        var excerptTitle = read.nextLine();
////        Optional<Episode> searchedEpisode = episodes.stream()
////                .filter(e -> e.getTitle().toUpperCase().contains(excerptTitle.toUpperCase()))
////                .findFirst();
////        if(searchedEpisode.isPresent()){
////            System.out.println("Episode found!");
////            System.out.println("Season: " + searchedEpisode.get().getSeason());
////        } else {
////            System.out.println("Episode not found!");
////        }
//
////        System.out.println("What year do you want to watch the episodes from?");
////        var year = read.nextInt();
////        read.nextLine();
////
////        LocalDate searhDate = LocalDate.of(year, 1, 1);
////
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////
////        episodes.stream()
////                .filter(e -> e.getLaunchDate() != null && e.getLaunchDate() .isAfter(searhDate))
////                .forEach(e -> System.out.println(
////                        "Season: " + e.getSeason() +
////                                " Episode: " + e.getTitle() +
////                                " Release Date: " + e.getLaunchDate().format(formatter)
////                ));
//
//
//        Map<Integer, Double> ratingPerSeason = episodes.stream()
//                .filter(e -> e.getRating() > 0.0)
//                .collect(Collectors.groupingBy(Episode::getSeason,
//                        Collectors.averagingDouble(Episode::getRating)));
//        System.out.println(ratingPerSeason);
//
//        DoubleSummaryStatistics est = episodes.stream()
//                .filter(e -> e.getRating() > 0.0)
//                .collect(Collectors.summarizingDouble(Episode::getRating));
//        System.out.println("Avarage: " + est.getAverage());
//        System.out.println("Best episode: " + est.getMax());
//        System.out.println("worst episode: " + est.getMin());
//        System.out.println("Amount: " + est.getCount());
//    }
//
//
//
//
//}

