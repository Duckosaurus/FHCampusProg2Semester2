package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MovieRepository {
    private final Dao<MovieEntity, UUID> dao;

    public MovieRepository() {
        try {
            this.dao = DatabaseManager.getMovieDao();
        }
        catch (SQLException e) {
            throw new DatabaseException("MovieRepository: DAO für Filme konnte nicht initialisiert werden. " +
                    "Bitte überprüfe deine Datenbankverbindung und ORMLite-Einstellungen.", e);
        }
    }

    public List<MovieEntity> getAllMovies() {
        try {
            return dao.queryForAll();
        }
        catch (SQLException e) {
            throw new DatabaseException("MovieRepository: Fehler beim Lesen aller Filme aus der Datenbank. " +
                    "Möglicherweise ist die Tabelle nicht erstellt oder die DB-Verbindung unterbrochen.", e);
        }
    }

    public MovieEntity findById(UUID movieId) {
        try {
            return dao.queryForId(movieId);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null; // Rückgabe von null, falls der Film nicht gefunden wird
        }
    }

    public void save(Movie movie) throws SQLException {
        if (movie != null) {
            MovieEntity movieEntity = new MovieEntity(movie);
            dao.createOrUpdate(movieEntity);
        }
    }
}
