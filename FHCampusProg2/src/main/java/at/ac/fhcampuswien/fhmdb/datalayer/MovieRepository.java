package at.ac.fhcampuswien.fhmdb.datalayer;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository
{
    private Dao<MovieEntity, Long> movieDao;

    public MovieRepository(Dao<MovieEntity, Long> movieDao) {
        this.movieDao = movieDao;
    }

    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return movieDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all movies: " + e.getMessage(), e);
        }
    }

    public MovieEntity getMovie(String apiId) throws DatabaseException {
        try {
            List<MovieEntity> results = movieDao.queryForEq("apiId", apiId);
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting movie by apiId: " + e.getMessage(), e);
        }
    }

    public int addAllMovies(List<MovieEntity> movies) throws DatabaseException {
        int rowsCreated = 0;
        try {
            for (MovieEntity movie : movies) {
                movieDao.create(movie);
                rowsCreated++;
            }
            return rowsCreated;
        } catch (SQLException e) {
            throw new DatabaseException("Error adding movies: " + e.getMessage(), e);
        }
    }

    public int removeAll() throws DatabaseException {
        try {
            return movieDao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing all movies: " + e.getMessage(), e);
        }
    }

    public int removeMovie(String apiId) throws DatabaseException {
        try {
            DeleteBuilder<MovieEntity, Long> deleteBuilder = movieDao.deleteBuilder();
            deleteBuilder.where().eq("apiId", apiId);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing movie: " + e.getMessage(), e);
        }
    }

}
