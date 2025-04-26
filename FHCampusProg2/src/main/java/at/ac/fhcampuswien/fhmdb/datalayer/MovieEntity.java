package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
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
    public MovieEntity() {}
    public String genreToString(List<Genre> genres){
        List<String> stringGenres = new ArrayList<String>();
        for (Genre genre : genres)
            stringGenres.add(genre.toString());
        return String.join(",",stringGenres);
    }

    public List<MovieEntity> fromMovies(List<Movie> movies){
        List<MovieEntity> entities = new ArrayList<>();
        for (Movie movie : movies) {
            MovieEntity entity = new MovieEntity(
                    movie.getId(),
                    movie.apiId,
                    movie.getTitle(),
                    movie.getDescription(),
                    genreToString(movie.getGenres()),
                    movie.getReleaseYear(),
                    movie.imgUrl,
                    movie.lengthInMinutes,
                    movie.getRating()
            );
            entities.add(entity);
        }
        return entities;
    }

    public List<Movie> toMovies(List<MovieEntity> movieEntities){
        List<Movie> movies = new ArrayList<>();

        for (MovieEntity entity : movieEntities) {
            List<Genre> genreList = Arrays.stream(entity.genres.split(","))
                    .map(genre -> genre.trim())
                    .map(genre -> Genre.valueOf(genre)) // → konvertiere String zu Enum: "DRAMA" → Genre.DRAMA
                    .collect(Collectors.toList()); // → List<Genre>
            Movie movie = new Movie(
                    entity.id,
                    entity.apiId,
                    entity.title,
                    entity.description,
                    entity.releaseYear,
                    entity.rating,
                    genreList,
                    entity.imgUrl,
                    entity.lengthInMinutes
            );
            movies.add(movie);
        }
        return movies;
    }

    public static Movie findByApiId(long apiId) {
        try {
            Dao<MovieEntity, Long> dao = DaoManager.createDao(DatabaseManager.getConnectionSource(), MovieEntity.class);
            MovieEntity entity = dao.queryBuilder()
                    .where()
                    .eq("apiId", apiId)
                    .queryForFirst();

            if (entity != null) {
                List<Genre> genreList = Arrays.stream(entity.genres.split(","))
                        .map(genre -> genre.trim())
                        .map(genre -> Genre.valueOf(genre)) // → konvertiere String zu Enum: "DRAMA" → Genre.DRAMA
                        .collect(Collectors.toList()); // → List<Genre>
                Movie movie = new Movie(
                        entity.id,
                        entity.apiId,
                        entity.title,
                        entity.description,
                        entity.releaseYear,
                        entity.rating,
                        genreList,
                        entity.imgUrl,
                        entity.lengthInMinutes);
                return movie;
            } else {
                return null; // oder Optional<Movie> verwenden
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Suchen des Films mit API-ID: " + apiId, e);
        }
    }

}
