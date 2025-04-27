package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseManager
{
    private static final String DB_URL = "jdbc:h2:./fhmdb"; // Example URL
    private static final String USERNAME = "user"; // Example
    private static final String PASSWORD = "password"; // Example

    private static ConnectionSource connectionSource;
    private Dao<MovieEntity, Long> movieDao;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public DatabaseManager() throws DatabaseException
    {
        try {
            createConnectionSource();
            createTables();
            getMovieDao();
            getWatchlistDao();
        } catch (SQLException e) {
            throw new DatabaseException("Error initializing database: " + e.getMessage(), e);
        }
    }

    public static void createConnectionSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(DB_URL, USERNAME, PASSWORD);
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    public Dao<MovieEntity, Long> getMovieDao() throws SQLException {
        if (movieDao == null) {
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
        }
        return movieDao;
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() throws SQLException {
        if (watchlistDao == null) {
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
        }
        return watchlistDao;
    }

    public void closeConnection() throws Exception
    {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }
}
