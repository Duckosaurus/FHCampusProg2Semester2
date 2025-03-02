package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController controller;

    @BeforeEach
    void setUp() {
        controller = new HomeController();
        controller.allMovies = List.of(
                new Movie("The Batman", "Dark Knight", List.of(Genre.ACTION)),
                new Movie("The Notebook", "Romantic movie", List.of(Genre.ROMANCE)),
                new Movie("Spider Man", "A web-slinger", List.of(Genre.ACTION))
        );
    }

    @Test
    void testSortMoviesAscending() {
        controller.allMovies = List.of(
                new Movie("B Movie", "Description", List.of(Genre.ACTION)),
                new Movie("A Movie", "Description", List.of(Genre.DRAMA))
        );

        controller.sortMovies(); // should be ascending

        ObservableList<Movie> sortedMovies = controller.getObservableMovies();
        assertEquals("A Movie", sortedMovies.get(0).getTitle());
        assertEquals("B Movie", sortedMovies.get(1).getTitle());
    }

    @Test
    void testSortMoviesDescending() {
        controller.allMovies = List.of(
                new Movie("A Movie", "Description", List.of(Genre.DRAMA)),
                new Movie("B Movie", "Description", List.of(Genre.ACTION))
        );

        controller.sortMovies(); // ascending
        controller.sortMovies(); // descending

        ObservableList<Movie> sortedMovies = controller.getObservableMovies();
        assertEquals("B Movie", sortedMovies.get(0).getTitle());
        assertEquals("A Movie", sortedMovies.get(1).getTitle());
    }

    @Test
    void testFilterMoviesBySearchQuery() {
        controller.searchField.setText("batman");
        controller.resetGenre();

        ObservableList<Movie> filteredMovies = controller.getObservableMovies();
        assertEquals(1, filteredMovies.size());
        assertEquals("The Batman", filteredMovies.get(0).getTitle());
    }

    @Test
    void testFilterMoviesByGenre() {
        controller.genreComboBox.setValue(Genre.ROMANCE);
        controller.searchGenre();

        ObservableList<Movie> filteredMovies = controller.getObservableMovies();
        assertEquals(1, filteredMovies.size());
        assertEquals("The Notebook", filteredMovies.get(0).getTitle());
    }
}