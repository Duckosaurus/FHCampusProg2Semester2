package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {

    @Test
    void testMovieConstructor() {
        // Create a movie with valid parameters
        Movie movie = new Movie(
                "The Grand Budapest Hotel",
                "A concierge and his protégé navigate a series of adventures in a famous hotel.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION)
        );

        // Assert that the movie properties are set correctly
        assertEquals("The Grand Budapest Hotel", movie.getTitle());
        assertEquals("A concierge and his protégé navigate a series of adventures in a famous hotel.", movie.getDescription());
        assertEquals(2, movie.getGenres().size());
        assertTrue(movie.getGenres().contains(Genre.ACTION));
        assertTrue(movie.getGenres().contains(Genre.SCIENCE_FICTION));
    }

    @Test
    void testMovieConstructorWithNullDescription() {
        // Create a movie with null description
        Movie movie = new Movie(
                "Test Movie",
                null,
                List.of(Genre.COMEDY)
        );

        // Assert that the movie properties are set correctly
        assertEquals("Test Movie", movie.getTitle());
        assertNull(movie.getDescription());
        assertEquals(1, movie.getGenres().size());
        assertTrue(movie.getGenres().contains(Genre.COMEDY));
    }

    @Test
    void testInitializeMovies() {
        // Get the list of initialized movies
        List<Movie> movies = Movie.initializeMovies();

        // Assert that the list is not null and contains the expected number of movies
        assertNotNull(movies);
        assertEquals(12, movies.size());

        // Check specific movies from the initialization
        boolean foundInception = false;
        boolean foundBibiTina = false;
        boolean foundTitanic = false;

        for (Movie movie : movies) {
            if ("Inception".equals(movie.getTitle())) {
                foundInception = true;
                assertTrue(movie.getGenres().contains(Genre.SCIENCE_FICTION));
            }
            if ("Bibi und Tina".equals(movie.getTitle())) {
                foundBibiTina = true;
                assertNull(movie.getDescription());
                assertTrue(movie.getGenres().contains(Genre.FAMILY));
            }
            if ("Titanic".equals(movie.getTitle())) {
                foundTitanic = true;
                assertEquals("A love story on the Titanic.", movie.getDescription());
            }
        }

        assertTrue(foundInception, "Inception should be in the initialized movies");
        assertTrue(foundBibiTina, "Bibi und Tina should be in the initialized movies");
        assertTrue(foundTitanic, "Titanic should be in the initialized movies");
    }

    @Test
    void testDuplicateTitlesButDifferentDescriptions() {
        List<Movie> movies = Movie.initializeMovies();

        // Find movies with the title "Inception"
        List<Movie> inceptionMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if ("Inception".equals(movie.getTitle())) {
                inceptionMovies.add(movie);
            }
        }

        // There should be two movies with the title "Inception"
        assertEquals(2, inceptionMovies.size());

        // The descriptions should be different
        assertNotEquals(inceptionMovies.get(0).getDescription(), inceptionMovies.get(1).getDescription());
    }

    @Test
    void testGettersReturnExpectedValues() {
        // Create a movie
        Movie movie = new Movie(
                "Parasite",
                "A poor family schemes to become employed by a wealthy household.",
                List.of(Genre.CRIME, Genre.DRAMA)
        );

        // Test getters
        assertEquals("Parasite", movie.getTitle());
        assertEquals("A poor family schemes to become employed by a wealthy household.", movie.getDescription());
        assertEquals(2, movie.getGenres().size());
        assertTrue(movie.getGenres().contains(Genre.CRIME));
        assertTrue(movie.getGenres().contains(Genre.DRAMA));
    }
}