package at.ac.fhcampuswien.fhmdb.datalayer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist_movies")
public class WatchlistMovieEntity {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private long apiId;

    // Leerer Konstruktor (wichtig für ORMLite)
    public WatchlistMovieEntity() {}

    public WatchlistMovieEntity(long apiId) {
        this.apiId = apiId;
    }
    public long getApiId() {
        return apiId;
    }

    public void setApiId(long apiId) {
        this.apiId = apiId;
    }
}
