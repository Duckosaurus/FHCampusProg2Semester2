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

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    private boolean ascending = true;

    /**
     * Initialisiert die Liste der Filme und die anderen FXML Elemente
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.getSelectionModel().selectFirst();

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

    //TODO: trycatch wenn es Descrition oder Titel Null ist
    private void selectedGenre() {
        String query = searchField.getText().toLowerCase().trim();
        List<Movie> allMoviesSearched = allMovies;
        if (!query.isEmpty()) {
            allMoviesSearched = allMovies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(query) ||
                            movie.getDescription().toLowerCase().contains(query)).toList();
        }

        Genre selectedGenre = (Genre) genreComboBox.getValue();
        if (selectedGenre == Genre.ALL) {
            observableMovies.setAll(allMoviesSearched);
            sortMovies();
            return;
        }

        allMoviesSearched = allMoviesSearched.stream()
                .filter(movie -> selectedGenre == null || movie.getGenres().contains(selectedGenre))
                .toList();
        observableMovies.setAll(allMoviesSearched);
        sortMovies();
    }


    /**
     * Sortiert die Filme auf- oder absteigend nach Titel und dreht den Text im Button um
     */
    private void sortMovies() {
        Comparator<Movie> comparator = Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        observableMovies.sort(comparator);
        ascending = !ascending;
        sortBtn.setText(ascending ? "Sort (asc)" : "Sort (desc)");
    }
}