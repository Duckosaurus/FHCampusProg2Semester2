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

import java.io.IOException;
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

        ObservableList<Movie> sortedMovies = controller.observableMovies;
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

        ObservableList<Movie> sortedMovies = controller.observableMovies;
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
    void testSearch() {
        assertDoesNotThrow(() -> controller.search());
    }

    @Test
    void testSearchALL() {
        controller.genreComboBox.setValue(Genre.ALL);
        assertDoesNotThrow(() -> controller.search());
    }

    @Test
    void testSearchMoviesWithText() {
        controller.searchField = new TextField("The Batman");
        List<Movie> searchedMovieswithTextList = controller.searchMoviesWithText();
        assertEquals(1, searchedMovieswithTextList.size());
        assertEquals("The Batman", searchedMovieswithTextList.get(0).getTitle());
    }

    @Test
    void testSearchMoviesWithPartialText() {
        controller.searchField = new TextField("bat");
        List<Movie> searchedMovieswithTextList = controller.searchMoviesWithText();
        assertEquals(1, searchedMovieswithTextList.size());
        assertEquals("The Batman", searchedMovieswithTextList.get(0).getTitle());
    }

    @Test
    void testSearchMoviesWithNonExistingText() {
        controller.searchField = new TextField("tzujz");
        List<Movie> searchedMovieswithTextList = controller.searchMoviesWithText();
        assertEquals(0, searchedMovieswithTextList.size());
    }

    @Test
    void testSearchMoviesWithPartialDescription() {
        controller.searchField = new TextField("knight");
        List<Movie> searchedMovieswithTextList = controller.searchMoviesWithText();
        assertEquals(1, searchedMovieswithTextList.size());
        assertEquals("The Batman", searchedMovieswithTextList.get(0).getTitle());
    }

    @Test
    void testfilterSpecificGenreAndText() {
        controller.searchField = new TextField("bat");
        controller.genreComboBox.setValue(Genre.ACTION);
        List<Movie> searchedMovieswithTextList = controller.searchMoviesWithText();
        assertEquals(1, searchedMovieswithTextList.size());
        assertEquals("The Batman", searchedMovieswithTextList.get(0).getTitle());
    }

    @Test
    void testgetMostPopularActor() throws IOException {
        List<Movie> movieList = List.of(
                new Movie("Movie A", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2009, 8),
                new Movie("Movie C", "Description", List.of(Genre.DRAMA),
                        List.of("Cast3", "Cast4"), "Super", 2010, 8),
                new Movie("Movie D", "Description", List.of(Genre.DRAMA),
                        List.of("Cast5", "Cast1"), "NichtSuper", 2020, 8),
                new Movie("B Movie", "Description", List.of(Genre.ACTION),
                        List.of("Cast2", "Cast1"), "NichtSuper", 2008, 8),
                new Movie("B Movie", "Description", List.of(Genre.ACTION),
                        List.of("Cast9", "Cast8"), "NichtSuper", 2011, 8)
        );
        String erg = controller.getMostPopularActor(movieList);
        System.out.println(erg);
        assertEquals("Cast1", erg);
    }

    @Test
    void testNoResultgetMostPopularActor() throws IOException {
        List<Movie> movieList = List.of(
                new Movie("Movie A", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2009, 8),
                new Movie("Movie C", "Description", List.of(Genre.DRAMA),
                        List.of("Cast3", "Cast4"), "Super", 2010, 8)
        );
        String erg = controller.getMostPopularActor(movieList);
        System.out.println(erg);
        assertEquals("Es gibt keinen häufigsten", erg);
    }

    @Test
    void testNoResultManygetMostPopularActor() throws IOException {
        List<Movie> movieList = List.of(
                new Movie("Movie A", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2009, 8),
                new Movie("Movie C", "Description", List.of(Genre.DRAMA),
                        List.of("Cast3", "Cast4"), "Super", 2010, 8),
                new Movie("Movie C", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2010, 8),
                new Movie("Movie C", "Description", List.of(Genre.DRAMA),
                        List.of("Cast3", "Cast4"), "Super", 2010, 8)
        );
        String erg = controller.getMostPopularActor(movieList);
        System.out.println(erg);
        assertEquals("Es gibt keinen häufigsten", erg);
    }

    @Test
    void testTrueGetLongestMovieTitle() {
        List<Movie> movieList = List.of(
                new Movie("LangerMovie", "Description", List.of(Genre.DRAMA)),
                new Movie("B Movie", "Description", List.of(Genre.ACTION))
        );
        int erg = controller.getLongestMovieTitle(movieList);
        assertEquals(11, erg);
    }

    @Test
    void testTruecountMoviesFrom() {
        List<Movie> movieList = List.of(
                new Movie("Movie A", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2010, 8),
                new Movie("Movie C", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2010, 8),
                new Movie("Movie D", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "NichtSuper", 2010, 8),
                new Movie("B Movie", "Description", List.of(Genre.ACTION))
        );
        long erg = controller.countMoviesFrom(movieList, "Super");
        assertEquals(2, erg);
    }


    @Test
    void testGetMoviesBetweenYears() {
        List<Movie> movieList = List.of(
                new Movie("Movie A", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2009, 8),
                new Movie("Movie C", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "Super", 2010, 8),
                new Movie("Movie D", "Description", List.of(Genre.DRAMA),
                        List.of("Cast1", "Cast2"), "NichtSuper", 2020, 8),
                new Movie("B Movie", "Description", List.of(Genre.ACTION),
                        List.of("Cast1", "Cast2"), "NichtSuper", 2008, 8),
                new Movie("B Movie", "Description", List.of(Genre.ACTION),
                        List.of("Cast1", "Cast2"), "NichtSuper", 2011, 8),
                new Movie("B Movie", "Description", List.of(Genre.ACTION))
        );
        List<Movie> erg = controller.getMoviesBetweenYears(movieList, 2009, 2010);

        assertEquals(2, erg.size());


    }
}