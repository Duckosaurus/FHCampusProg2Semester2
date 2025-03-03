package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
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
        controller.sortBtn = new JFXButton("Sort (asc)");
        controller.genreComboBox = new JFXComboBox<>();
        controller.searchField = new javafx.scene.control.TextField();

        controller.genreComboBox.setItems(FXCollections.observableArrayList(Genre.values()));

        controller.allMovies = List.of(
                new Movie("The Batman", "Dark Knight", List.of(Genre.ACTION)),
                new Movie("The Notebook", "Romantic movie", List.of(Genre.ROMANCE)),
                new Movie("Spider-Man", "A web-slinger", List.of(Genre.ACTION))
        );

        controller.observableMovies.setAll(controller.allMovies);
    }

    @Test
    void testSortMoviesAscending() {
        controller.allMovies = List.of(
                new Movie("B Movie", "Description", List.of(Genre.ACTION)),
                new Movie("A Movie", "Description", List.of(Genre.DRAMA))
        );

        controller.ascending = true;
        controller.sortMovies();

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

        controller.ascending = true;
        controller.sortMovies();
        controller.sortMovies();

        ObservableList<Movie> sortedMovies = controller.getObservableMovies();
        assertEquals("B Movie", sortedMovies.get(0).getTitle());
        assertEquals("A Movie", sortedMovies.get(1).getTitle());
    }

    @Test
    void testFilterMoviesBySearchQuery() {
        controller.searchField.setText("batman");

        List<Movie> filteredMovies = controller.searchMoviesWithText("batman");

        assertEquals(1, filteredMovies.size());
        assertEquals("The Batman", filteredMovies.get(0).getTitle());
    }

    @Test
    void testFilterMoviesByGenre() {
        controller.genreComboBox.setValue(Genre.ROMANCE);
        controller.selectedGenre();

        ObservableList<Movie> filteredMovies = controller.getObservableMovies();
        assertEquals(1, filteredMovies.size());
        assertEquals("The Notebook", filteredMovies.get(0).getTitle());
    }

    @Test
    void testFilterMoviesBySearchQueryAndGenre() {
        controller.allMovies = List.of(
                new Movie("The Batman", "Dark Knight", List.of(Genre.ACTION, Genre.DRAMA)),
                new Movie("Batman Begins", "First movie", List.of(Genre.ACTION)),
                new Movie("Notebook", "Romantic movie", List.of(Genre.ROMANCE))
        );

        controller.genreComboBox.setValue(Genre.ACTION);
        List<Movie> filteredMovies = controller.searchMoviesWithText("batman");

        assertEquals(2, filteredMovies.size());
        assertTrue(filteredMovies.stream().anyMatch(m -> m.getTitle().equals("The Batman")));
        assertTrue(filteredMovies.stream().anyMatch(m -> m.getTitle().equals("Batman Begins")));
    }
}