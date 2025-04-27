package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistRepository
{
    private static WatchlistRepository instance;
    private final Dao<WatchlistMovieEntity, Long> dao;

    private WatchlistRepository()
    {
        try
        {
            dao = DaoManager.createDao(DatabaseManager.getConnectionSource(), WatchlistMovieEntity.class);
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fehler beim Erstellen des Watchlist-DAOs", e);
        }
    }

    public static WatchlistRepository getInstance()
    {
        if (instance == null)
        {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    // Alle Filme aus der Watchlist laden
    public List<Movie> getAllWatchlistMovies()
    {
        try
        {
            List<WatchlistMovieEntity> watchlist = dao.queryForAll();
            return watchlist.stream()
                    .map(entity -> MovieEntity.findByApiId(entity.getApiId()))
                    .collect(Collectors.toList());
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fehler beim Laden der Watchlist", e);
        }
    }

    // Film zur Watchlist hinzufügen
    public void addToWatchlist(Movie movie)
    {
        try
        {
            WatchlistMovieEntity entity = new WatchlistMovieEntity(movie.getId());
            dao.createIfNotExists(entity);
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fehler beim Hinzufügen zur Watchlist", e);
        }
    }

    // Film aus der Watchlist entfernen
    public void removeFromWatchlist(String apiId)
    {
        try
        {
            DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("apiId", apiId);
            deleteBuilder.delete();
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fehler beim Entfernen aus der Watchlist", e);
        }
    }
}

