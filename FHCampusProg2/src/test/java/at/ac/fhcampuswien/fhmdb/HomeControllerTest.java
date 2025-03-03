package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController controller;

    @BeforeAll
    static void startup() {
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp() {
        controller = new HomeController();
        controller.sortBtn = new JFXButton("Sort (asc)");
        controller.genreComboBox = new JFXComboBox<>();
        controller.searchField = new TextField();

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
        controller.observableMovies.setAll(controller.allMovies);
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
        controller.observableMovies.setAll(controller.allMovies);

        controller.ascending = true;
        controller.sortMovies();
        controller.sortMovies();

        ObservableList<Movie> sortedMovies = controller.getObservableMovies();
        assertEquals("B Movie", sortedMovies.get(0).getTitle());
        assertEquals("A Movie", sortedMovies.get(1).getTitle());
    }

    @Test
    void testFilterSpecificGenre() {
        controller.filterSpecificGenre(controller.observableMovies, Genre.ACTION);
        assertEquals(2, controller.observableMovies.size());
    }

    @Test
    void testFilterAllGenre() {
        assertTrue(controller.filterAllGenre(Genre.ALL, controller.allMovies));
        assertEquals(3, controller.observableMovies.size());
    }

    @Test
    void testSelectedGenre() {
        assertDoesNotThrow(() -> controller.selectedGenre());
    }

    @Test
    void testSearchMoviesWithText() {
        controller.searchField = new TextField("Batman");
        List<Movie> searchedMovieswithTextList = controller.searchMoviesWithText();
        assertEquals(1, searchedMovieswithTextList.size());
        assertEquals("The Batman", searchedMovieswithTextList.get(0).getTitle());
    }
}