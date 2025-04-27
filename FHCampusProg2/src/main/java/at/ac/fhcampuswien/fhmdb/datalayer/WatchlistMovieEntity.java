package at.ac.fhcampuswien.fhmdb.datalayer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "watchlist_movies")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private UUID movieId;

    // Leerer Konstruktor (wichtig für ORMLite)
    public WatchlistMovieEntity() {
    }

    public WatchlistMovieEntity(UUID apiId) {
        this.movieId = apiId;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

}
