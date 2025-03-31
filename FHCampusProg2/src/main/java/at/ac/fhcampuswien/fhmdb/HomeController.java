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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public JFXComboBox releaseYearComboBox;
    @FXML
    public JFXComboBox ratingComboBox;
    @FXML
    public JFXButton sortBtn;
    public final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    public boolean ascending = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMoviesFromApi();
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.getSelectionModel().selectFirst();
        releaseYearComboBox.getItems().addAll(observableMovies.stream().map(Movie::getReleaseYear)
                .sorted().toList());
        ratingComboBox.getItems().add("Filter by Rating");
        int[] ratingsNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (var item : ratingsNumbers) {
            ratingComboBox.getItems().add(item);
        }
        ratingComboBox.getSelectionModel().selectFirst();

        releaseYearComboBox.getItems().add("Filter by Release Year");
        for (int i = 1950; i <= LocalDate.now().getYear(); i++) {
            releaseYearComboBox.getItems().add(i);
        }

        releaseYearComboBox.getSelectionModel().selectFirst();
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
            loadMoviesFromApi();
        });
    }

    private void loadMoviesFromApi() {
        new Thread(() -> {
            List<Movie> moviesFromApi = null;
            try {
                moviesFromApi = MovieAPI.fetchMovies(searchField.getText(), (Genre) genreComboBox.getValue(), null, null);
                if (moviesFromApi != null) {
                    List<Movie> finalMoviesFromApi = moviesFromApi;
                    javafx.application.Platform.runLater(() -> {
                        observableMovies.setAll(finalMoviesFromApi);
                        movieListView.setItems(observableMovies);
                        movieListView.setCellFactory(movieListView -> new MovieCell());
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    //TODO: rename Method
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
            List<Movie> allMoviesSearched = observableMovies.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(query) ||
                            movie.getDescription().toLowerCase().contains(query)).toList();
            return allMoviesSearched;
        }
        return observableMovies;
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

    public String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> counts = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Optional<Map.Entry<String, Long>> maxEntry = counts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isPresent()) {
            long maxCount = maxEntry.get().getValue();
            long numberOfMax = counts.values().stream()
                    .filter(count -> count.equals(maxCount))
                    .count();
            if (numberOfMax == 1) {
                return maxEntry.get().getKey();
            }
        }
        return "Es gibt keinen häufigsten";
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .map(Movie::getTitle)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream().filter(x -> x.getDirector() == director).count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream().filter(x -> x.getReleaseYear() >= startYear && x.getReleaseYear() <= endYear).collect(Collectors.toList());
    }
}