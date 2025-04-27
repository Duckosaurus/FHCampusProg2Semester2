package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.MovieAPI;
import at.ac.fhcampuswien.fhmdb.datalayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    public List<Movie> allMovies = new ArrayList<>();
    public boolean ascending = true;
    private final WatchlistRepository watchlistRepository = WatchlistRepository.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.getSelectionModel().selectFirst();
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
        loadMoviesFromApi();
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
            search();
            sortMovies();
        });


    }

    private void loadMoviesFromApi() {
        ClickEventHandler<Movie> addToWatchlistHandler = movie -> {
            try {
                WatchlistRepository watchlistRepo = new WatchlistRepository();
                watchlistRepo.add(movie);
            } catch (DatabaseException ex) {
                showAlert(Alert.AlertType.ERROR,
                        "Speicherfehler",
                        "Beim Speichern in der Watchlist ist ein Fehler aufgetreten.\n" +
                                "Bitte versuche es später erneut.");
            }
        };
        new Thread(() -> {
            List<Movie> moviesFromApi = null;
            try {
                moviesFromApi = MovieAPI.fetchMovies(searchField.getText(), (Genre) genreComboBox.getValue(), releaseYearComboBox.getValue().toString(), ratingComboBox.getValue().toString());
                if (moviesFromApi != null) {
                    List<Movie> finalMoviesFromApi = moviesFromApi;
                    javafx.application.Platform.runLater(() -> {
                        observableMovies.setAll(finalMoviesFromApi);
                        allMovies.addAll(finalMoviesFromApi);
                        movieListView.setItems(observableMovies);
                        movieListView.setCellFactory(movieListView -> new MovieCell(addToWatchlistHandler, false));
                    });
                }
            } catch (IOException e) {
                observableMovies.clear();
                movieListView.setItems(observableMovies);
            }

        }).start();
    }

//    private void loadMoviesFromWatchlist() {
//        // Hier müsstest du die Filme aus deiner Watchlist-Datenbank laden
//        new Thread(() -> {
//            List<Movie> moviesFromWatchlist = null;
//            try {
//                moviesFromWatchlist = WatchlistRepository.getInstance().getAllWatchlistMovies(); // oder eine ähnliche Methode von dir
//                List<Movie> finalMoviesFromWatchlist = moviesFromWatchlist;
//                javafx.application.Platform.runLater(() -> {
//                    observableMovies.setAll(finalMoviesFromWatchlist);
//                    movieListView.setItems(observableMovies);
//                    movieListView.setCellFactory(movieListView -> new MovieCell()); // oder eine spezialisierte WatchlistCell
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//                observableMovies.clear();
//                movieListView.setItems(observableMovies);
//            }
//        }).start();
//    }

    public void search() {
        List<Movie> allMoviesSearched = searchMoviesWithText();
        if (ratingComboBox.getValue() != "Filter by Rating")
            allMoviesSearched = filterMoviesByRating(allMoviesSearched, (int) ratingComboBox.getValue());
        if (releaseYearComboBox.getValue() != "Filter by Release Year") {
            allMoviesSearched = allMoviesSearched.stream()
                    .filter(x -> x.getReleaseYear() == (int) releaseYearComboBox.getValue()).toList();
        }
        Genre selectedGenre = (Genre) genreComboBox.getValue();
        if (filterAllGenre(selectedGenre, allMoviesSearched)) return;
        filterSpecificGenre(allMoviesSearched, selectedGenre);
    }

    public List<Movie> filterMoviesByRating(List<Movie> allMovies, Number selectedRating) {
        return allMovies.stream()
                .filter(movie -> movie.getRating().doubleValue() >= selectedRating.doubleValue() && movie.getRating().doubleValue() < (selectedRating.doubleValue() + 0.9))
                .collect(Collectors.toList());
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
    private void showAlert(Alert.AlertType type, String title, String text) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(text);
        a.showAndWait();
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

    @FXML
    private void navigateToHome(ActionEvent event) {
        loadMoviesFromApi();
    }

    @FXML
    private void navigateToWatchlist(ActionEvent event) {
        //loadMoviesFromWatchlist();
    }
}