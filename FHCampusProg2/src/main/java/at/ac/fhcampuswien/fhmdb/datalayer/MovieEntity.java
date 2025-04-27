package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity
{
    @DatabaseField(generatedId = true)
    public long id;
    @DatabaseField
    public String apiId;
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
    public MovieEntity(long id, String apiId, String title, String description, String genres,
                       int releaseYear, String imgUrl, int lengthInMinutes, double rating) {
        this.id = id;
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    public MovieEntity(Movie movie) {
        this.apiId = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.genres = genreToString(movie.getGenres());
        this.releaseYear = movie.getReleaseYear();
        this.imgUrl = movie.getImgUrl();
        this.lengthInMinutes = movie.getLengthInMinutes();
        this.rating = movie.getRating() != null ? movie.getRating().doubleValue() : 0.0; // .doubleValue() sagt Java -> dieses Number Objekt (movie.getRating()) ist ein double
    }
    public MovieEntity() {}
    public String genreToString(List<Genre> genres){
        List<String> stringGenres = new ArrayList<String>();
        for (Genre genre : genres)
            stringGenres.add(genre.toString());
        return String.join(",",stringGenres);
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        return movies.stream()
                .map(movie -> new MovieEntity(movie))
                .collect(Collectors.toList());
    }

    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        return movieEntities.stream()  // Starte einen Stream über die List<MovieEntity>
                .map(entity -> { // Für jede einzelne MovieEntity mache ...

                    List<Genre> genreList = Arrays.stream(entity.genres.split(","))  // "DRAMA,COMEDY" → ["ACTION", "COMEDY"]
                            .map(genre -> genre.trim()) // → entfernt Leerzeichen " DRAMA " → "DRAMA"
                            .map(genre -> Genre.valueOf(genre)) // → konvertiere String zu Enum: "DRAMA" → Genre.DRAMA
                            .collect(Collectors.toList()); // → List<Genre>

                    // Erstelle ein neues Movie-Objekt mit allen Werten
                    return new Movie(
                            entity.title,
                            entity.description,
                            genreList, // Die genreList, die wir gerade erstellt haben
                            entity.apiId,
                            entity.releaseYear,
                            entity.imgUrl,
                            entity.lengthInMinutes,
                            entity.rating // Autoboxing von double -> Double (= Subtyp von Number, deshalb ok)
                    );
                })
                .collect(Collectors.toList());  // Sammle alle zurückgegebenen Movie-Objekte in einer neuen List<Movie>
    }



}
