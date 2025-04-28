package at.ac.fhcampuswien.fhmdb.datalayer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "watchlist_movies")
public class WatchlistMovieEntity {
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

}
