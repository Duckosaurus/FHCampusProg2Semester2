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
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public WatchlistRepository(Dao<WatchlistMovieEntity, Long> watchlistDao) {
        this.watchlistDao = watchlistDao;
    }
    public static WatchlistRepository getInstance(Dao<WatchlistMovieEntity, Long> watchlistDao) {
        if (instance == null) {
            instance = new WatchlistRepository(watchlistDao);
        }
        return instance;
    }
    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return watchlistDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error getting watchlist: " + e.getMessage(), e);
        }
    }

    public int addToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            // Check if the movie is already in the watchlist
            List<WatchlistMovieEntity> existingMovies = watchlistDao.queryForEq("apiId", movie.getApiId());
            if (existingMovies != null && !existingMovies.isEmpty()) {
                // Movie already exists, don't add again
                return 0;
            }
            return watchlistDao.create(movie);
        } catch (SQLException e) {
            throw new DatabaseException("Error adding to watchlist: " + e.getMessage(), e);
        }
    }

    public int removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = watchlistDao.deleteBuilder();
            deleteBuilder.where().eq("apiId", apiId);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing from watchlist: " + e.getMessage(), e);
        }
    }
    }


