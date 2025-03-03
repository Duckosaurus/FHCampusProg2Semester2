package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    public final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    public boolean ascending = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.getSelectionModel().selectFirst();
        sortMovies();

        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                sortMovies();
                sortBtn.setText("Sort (desc)");
            } else {
                sortMovies();
                sortBtn.setText("Sort (asc)");
            }
        });
        searchBtn.setOnAction(actionEvent -> {
            selectedGenre();
        });

    }

    public void selectedGenre() {
        List<Movie> allMoviesSearched = searchMoviesWithText();

        Genre selectedGenre = (Genre) genreComboBox.getValue();
        if (filterAllGenre(selectedGenre, allMoviesSearched)) return;

        filterSpecificGenre(allMoviesSearched, selectedGenre);
    }

    public void filterSpecificGenre(List<Movie> allMoviesSearched, Genre selectedGenre) {
        allMoviesSearched = allMoviesSearched.stream()
                .filter(movie -> selectedGenre == null || movie.getGenres().contains(selectedGenre))
                .toList();
        observableMovies.setAll(allMoviesSearched);
        sortMovies();
    }

    public boolean filterAllGenre(Genre selectedGenre, List<Movie> allMoviesSearched) {
        if (selectedGenre == Genre.ALL) {
            observableMovies.setAll(allMoviesSearched);
            sortMovies();
            return true;
        }
        return false;
    }

    public List<Movie> searchMoviesWithText() {
        String query = searchField.getText().toLowerCase().trim();
        if (!query.isEmpty()) {
            List<Movie> allMoviesSearched = allMovies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(query) ||
                            movie.getDescription().toLowerCase().contains(query)).toList();
            return allMoviesSearched;
        }
        return allMovies;
    }

    public void sortMovies() {
        Comparator<Movie> comparator = Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        observableMovies.sort(comparator);
        ascending = !ascending;
        sortBtn.setText(ascending ? "Sort (asc)" : "Sort (desc)");
    }

    public ObservableList<Movie> getObservableMovies() {
        return observableMovies;
    }
}