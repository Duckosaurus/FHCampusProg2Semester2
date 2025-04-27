package at.ac.fhcampuswien.fhmdb.businesslayer;

import at.ac.fhcampuswien.fhmdb.datalayer.MovieEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.MovieRepository;
import at.ac.fhcampuswien.fhmdb.datalayer.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WatchlistController implements Initializable
{

    @FXML
    private ListView<MovieEntity> watchlistListView;

    private ObservableList<MovieEntity> watchlistItems = FXCollections.observableArrayList();

    private WatchlistRepository watchlistRepository;
    private MovieRepository movieRepository;  // Assuming you might need this

    public void setRepositories(WatchlistRepository watchlistRepository, MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.movieRepository = movieRepository;
    }

    @FXML
    public void initialize() {
        watchlistListView.setItems(watchlistItems);
        loadWatchlist();

        // Set the cell factory to display movie information (title)
        watchlistListView.setCellFactory(param -> new MovieListCell(this::handleRemoveFromWatchlistClicked));
    }

    public void loadWatchlist() {
        try {
            List<WatchlistMovieEntity> watchlistEntities = watchlistRepository.getWatchlist();
            watchlistItems.clear();

            for (WatchlistMovieEntity entity : watchlistEntities) {
                // Fetch the full MovieEntity using the apiId
                MovieEntity movie = movieRepository.getMovie(entity.getApiId());
                if (movie != null) {
                    watchlistItems.add(movie);
                }
            }
        } catch (DatabaseException e) {
            showAlert("Database Error", "Could not load watchlist: " + e.getMessage());
        }
    }

    private void handleRemoveFromWatchlistClicked(MovieEntity movie) {
        try {
            watchlistRepository.removeFromWatchlist(movie.getApiId());
            watchlistItems.remove(movie);
            showAlert("Success", movie.getTitle() + " removed from watchlist.");
        } catch (DatabaseException e) {
            showAlert("Database Error", "Could not remove " + movie.getTitle() + " from watchlist: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showConfirmationDialog(MovieEntity movie) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Confirmation");
        alert.setHeaderText("Remove Movie");
        alert.setContentText("Are you sure you want to remove " + movie.getTitle() + " from the watchlist?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                handleRemoveFromWatchlistClicked(movie);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    // Inner class for custom cell rendering with "Remove" button
    private class MovieListCell extends javafx.scene.control.ListCell<MovieEntity> {
        private ClickEventHandler<MovieEntity> removeFromWatchlistClicked;
        private javafx.scene.layout.HBox container;
        private javafx.scene.control.Label titleLabel;
        private javafx.scene.control.Button removeButton;

        public MovieListCell(ClickEventHandler<MovieEntity> removeFromWatchlistClicked) {
            this.removeFromWatchlistClicked = removeFromWatchlistClicked;
            titleLabel = new javafx.scene.control.Label();
            removeButton = new javafx.scene.control.Button("Remove");
            removeButton.setOnAction(event -> {
                if (getItem() != null) {
                    showConfirmationDialog(getItem());
                }
            });

            container = new javafx.scene.layout.HBox(10, titleLabel, removeButton);
            container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        }

        @Override
        protected void updateItem(MovieEntity movie, boolean empty) {
            super.updateItem(movie, empty);
            if (empty || movie == null) {
                setText(null);
                setGraphic(null);
            } else {
                titleLabel.setText(movie.getTitle());
                setGraphic(container);
            }
        }
    }
}
