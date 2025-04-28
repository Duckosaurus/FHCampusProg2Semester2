package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class WatchlistRepository {
    private final Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository() {
        try {
            dao = DaoManager.createDao(DatabaseManager.getConnectionSource(), WatchlistMovieEntity.class);
        }
        catch (SQLException e) {
            throw new DatabaseException("Fehler beim Erstellen des Watchlist-DAOs", e);
        }
    }

    public int add(Movie movie) {
        try {
            WatchlistMovieEntity existing = dao.queryBuilder()
                    .where().eq("MovieId", movie.getId())
                    .queryForFirst();
            if (existing == null) {
                dao.create(new WatchlistMovieEntity(movie.getId()));
                return 1;
            }
            return 0;
        }
        catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: Fehler beim Hinzufügen des Films '" + movie.getTitle() +
                    "' (API-ID: " + movie.getId() + ") zur Watchlist. Prüfe, ob der Eintrag bereits existiert oder ob " +
                    "die DB-Verbindung aufrecht ist.", e);
        }
    }

    public List<WatchlistMovieEntity> getAll() {
        try {
            return dao.queryForAll();
        }
        catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: Fehler beim Auslesen aller Watchlist-Einträge. " +
                    "Möglicherweise existiert die Tabelle nicht oder die Verbindung zur DB ist unterbrochen.", e);
        }
    }

    public void removeFromWatchlist(UUID apiId) {
        try {
            DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("MovieId", apiId);
            deleteBuilder.delete();
        }
        catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: Fehler beim Entfernen aus der Watchlist", e);
        }
    }
}

