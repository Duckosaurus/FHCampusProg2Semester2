package at.ac.fhcampuswien.fhmdb.businesslayer;

import at.ac.fhcampuswien.fhmdb.datalayer.MovieEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.MovieRepository;
import at.ac.fhcampuswien.fhmdb.datalayer.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.datalayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class WatchlistController implements Initializable {

    @FXML
    public JFXListView<Movie> watchlistListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            WatchlistRepository watchlistRepo = new WatchlistRepository();
            MovieRepository movieRepo = new MovieRepository();

            List<UUID> watchlistIds = watchlistRepo.getAll().stream()
                    .map(WatchlistMovieEntity::getMovieId)
                    .toList();

            // Alle Filme aus der Datenbank holen
            List<MovieEntity> list = movieRepo.getAllMovies();
            List<Movie> allMovies = MovieEntity.toMovies(list);

            // Nur Filme anzeigen, die in der Watchlist sind
            List<Movie> filtered = allMovies.stream()
                    .filter(m -> watchlistIds.contains(m.getId()))
                    .collect(Collectors.toList());

            // Click Handler fürs Entfernen
            ClickEventHandler<Movie> removeHandler = movie -> {
                try {
                    watchlistRepo.removeFromWatchlist(movie.getId());
                    initialize(null, null);// Liste neu laden nach Entfernen
                }
                catch (DatabaseException e) {
                    HomeController.showAlert(Alert.AlertType.ERROR,
                            "Löschen fehlgeschlagen",
                            "Beim Löschen von „" + movie.getTitle() +
                                    "“ ist ein Fehler aufgetreten.\nBitte versuche es später erneut.");
                }
            };

            // ListeView mit Daten befüllen
            watchlistListView.setItems(FXCollections.observableArrayList(filtered));
            watchlistListView.setCellFactory(view -> new MovieCell(removeHandler, true));

        }
        catch (DatabaseException e) {
            HomeController.showAlert(Alert.AlertType.ERROR,
                    "Watchlist konnte nicht geladen werden",
                    "Es gab ein Problem beim Zugriff auf die lokale Datenbank.\n" +
                            "Bitte prüfe, ob die App die korrekten Rechte für den Ordner besitzt " +
                            "und ob genug freier Speicherplatz vorhanden ist. " +
                            "Wenn das Problem weiterhin besteht starte die Anwendung neu oder" +
                            "melde dich bei deinem Administrator.");
        }
    }
}
