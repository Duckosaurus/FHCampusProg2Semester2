package at.ac.fhcampuswien.fhmdb.datalayer;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.UUID;


public class DatabaseManager {
    public static String DB_URL = "jdbc:h2:./db/fhmdb";
    public static String username = "sa";
    public static String password = "";
    public static ConnectionSource conn;
    public static Dao<MovieEntity, UUID> movieDao;

    public static void createConnectionsSource() throws SQLException {
        if (conn == null) conn = new JdbcConnectionSource(DB_URL, username, password);
    }

    public static ConnectionSource getConnectionSource() throws SQLException {
        if (conn == null) createConnectionsSource();
        return conn;
    }

    public static void createTables() throws SQLException {
        if (conn == null) createConnectionsSource();
        TableUtils.createTableIfNotExists(conn, MovieEntity.class);
        TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
    }

    public static Dao<MovieEntity, UUID> getMovieDao() throws SQLException {
        if (movieDao == null) {
            createConnectionsSource();
            movieDao = DaoManager.createDao(conn, MovieEntity.class);
        }
        return movieDao;
    }
}
