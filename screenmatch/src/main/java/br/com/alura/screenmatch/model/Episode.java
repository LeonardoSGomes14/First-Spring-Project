package br.com.alura.screenmatch.model;



import jakarta.persistence.*;

import java.time.DateTimeException;
import java.time.LocalDate;


@Entity
@Table(name = "episodes")

public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate launchDate;



    @ManyToOne
  private Serie serie;

    public Episode(){}



    public Episode(Integer seasonNumber, EpisodeData episodeDatas) {
        this.season = seasonNumber;
        this.title = episodeDatas.title();
        this.episodeNumber = episodeDatas.number();

        try {
            this.rating = Double.valueOf(episodeDatas.rating());
        } catch (NumberFormatException ex) {
            this.rating = 0.0;
        }

        try {
            this.launchDate = LocalDate.parse(episodeDatas.launchDate());
        } catch (DateTimeException ex) {
            this.launchDate = null;
        }

    }



//    GETTERS


     public Serie getSerie() {
            return serie;
        }

    public Integer getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    // SETTERS

    public void setSerie(Serie serie) {
        this.serie = serie;
    }
    public void setSeason(Integer season) {
        this.season = season;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    @Override
    public String toString() {
        return
                "season = " + season +
                        ", title = '" + title + '\'' +
                        ", episodeNumber = " + episodeNumber +
                        ", rating = " + rating +
                        ", launchDate = " + launchDate;
    }

}