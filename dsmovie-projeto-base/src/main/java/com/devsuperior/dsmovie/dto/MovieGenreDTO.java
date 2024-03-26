package com.devsuperior.dsmovie.dto;

import com.devsuperior.dsmovie.entities.MovieEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MovieGenreDTO {

    private static final DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    @Schema(description = "Database generated movie ID")
    private Long id;

    @NotBlank(message = "Required field")
    @Size(min = 5, max = 80, message = "Title must be between 5 and 80 characters")
    @Schema(description = "Movie title")
    private String title;

    @PositiveOrZero(message = "Score should be greater than or equal to zero")
    private Double score;

    @PositiveOrZero(message = "Count should be greater than or equal to zero")
    private Integer count;

    @NotBlank(message = "Required field")
    @URL(message = "Field must be a valid url")
    private String image;
    private String genre;

    public MovieGenreDTO(Long id, String title, Double score, Integer count, String image, String genre) {
        this.id = id;
        this.title = title;
        this.score = Double.valueOf(df.format(score));
        this.count = count;
        this.image = image;
        this.genre = genre;
    }

    public MovieGenreDTO(MovieEntity movie) {
        this(movie.getId(),
                movie.getTitle(),
                movie.getScore(),
                movie.getCount(),
                movie.getImage(),
                movie.getGenreEntity().getName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "MovieDTO [id=" + id + ", title=" + title + ", score=" + score + ", count=" + count + ", image=" + image
                + "]";
    }
}
