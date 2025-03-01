package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
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
    void testMovieConstructorWithEmptyGenres() {
        // Create a movie with empty genres list
        Movie movie = new Movie(
                "No Genres Movie",
                "A movie without any genres",
                Collections.emptyList()
        );

        // Assert that the movie properties are set correctly
        assertEquals("No Genres Movie", movie.getTitle());
        assertEquals("A movie without any genres", movie.getDescription());
        assertEquals(0, movie.getGenres().size());
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
    void testAllInitializedMoviesHaveValidTitles() {
        List<Movie> movies = Movie.initializeMovies();

        for (Movie movie : movies) {
            assertNotNull(movie.getTitle(), "Movie title should not be null");
            assertFalse(movie.getTitle().isEmpty(), "Movie title should not be empty");
        }
    }

    @Test
    void testMoviesWithSpecificGenres() {
        List<Movie> movies = Movie.initializeMovies();

        // Count movies with DRAMA genre
        long dramaCount = movies.stream()
                .filter(movie -> movie.getGenres().contains(Genre.DRAMA))
                .count();

        // Count movies with COMEDY genre
        long comedyCount = movies.stream()
                .filter(movie -> movie.getGenres().contains(Genre.COMEDY))
                .count();

        // Verify that we have at least one movie of each genre
        assertTrue(dramaCount > 0, "There should be at least one DRAMA movie");
        assertTrue(comedyCount > 0, "There should be at least one COMEDY movie");
    }

    @Test
    void testMovieGenresAreImmutable() {
        Movie movie = new Movie(
                "Test Movie",
                "Test Description",
                List.of(Genre.ACTION, Genre.ADVENTURE)
        );

        // Try to modify the genres list - should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> {
            movie.getGenres().add(Genre.COMEDY);
        });
    }

    @Test
    void testInitializedMoviesAreUnique() {
        List<Movie> movies = Movie.initializeMovies();

        // Check that no two movies are completely identical (excluding known duplicates like Inception)
        for (int i = 0; i < movies.size(); i++) {
            for (int j = i + 1; j < movies.size(); j++) {
                Movie movie1 = movies.get(i);
                Movie movie2 = movies.get(j);

                // If titles are the same, descriptions should be different (as we know from the duplicate test)
                if (movie1.getTitle().equals(movie2.getTitle())) {
                    assertNotEquals(movie1.getDescription(), movie2.getDescription(),
                            "Movies with same title should have different descriptions");
                }
            }
        }
    }

    @Test
    void testMoviesHaveAtLeastOneGenre() {
        List<Movie> movies = Movie.initializeMovies();

        for (Movie movie : movies) {
            assertFalse(movie.getGenres().isEmpty(),
                    "Movie '" + movie.getTitle() + "' should have at least one genre");
        }
    }
}