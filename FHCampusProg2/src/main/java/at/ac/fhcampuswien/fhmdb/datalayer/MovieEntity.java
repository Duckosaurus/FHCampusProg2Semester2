package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(id = true)
    public UUID id;
    @DatabaseField
    public String title;
    @DatabaseField
    public String description;
    @DatabaseField
    public String genres;
    @DatabaseField
    public int releaseYear;
    @DatabaseField
    public String imgUrl;
    @DatabaseField
    public int lengthInMinutes;
    @DatabaseField
    public double rating;

    public MovieEntity(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.genres = genreToString(movie.getGenres());
        this.releaseYear = movie.getReleaseYear();
        this.imgUrl = movie.getImgUrl();
        this.lengthInMinutes = movie.getLengthInMinutes();
        this.rating = movie.getRating() != null ? movie.getRating().doubleValue() : 0.0;
    }
    // Leerer Konstruktor (wichtig für ORMLite)
    public MovieEntity() {
    }

    public String genreToString(List<Genre> genres) {
        List<String> stringGenres = new ArrayList<String>();
        for (Genre genre : genres)
            stringGenres.add(genre.toString());
        return String.join(",", stringGenres);
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        return movies.stream()
                .map(movie -> new MovieEntity(movie))
                .collect(Collectors.toList());
    }

    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        return movieEntities.stream()
                .map(entity -> {
                    List<Genre> genreList = Arrays.stream(entity.genres.split(","))
                            .map(genre -> genre.trim())
                            .map(genre -> Genre.valueOf(genre))
                            .collect(Collectors.toList());
                    return new Movie(
                            entity.title,
                            entity.description,
                            genreList,
                            entity.id,
                            entity.releaseYear,
                            entity.imgUrl,
                            entity.lengthInMinutes,
                            entity.rating
                    );
                })
                .collect(Collectors.toList());
    }
}
