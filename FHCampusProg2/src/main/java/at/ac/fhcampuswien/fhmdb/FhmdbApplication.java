package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.businesslayer.HomeController;
import at.ac.fhcampuswien.fhmdb.datalayer.*;
import com.j256.ormlite.dao.Dao;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws SQLException
    {
        // 1) Datenbank initialisieren
        DatabaseManager dbManager = new DatabaseManager();
        Dao<MovieEntity, Long> movieDao = dbManager.getMovieDao();
        Dao<WatchlistMovieEntity, Long> watchlistDao = dbManager.getWatchlistDao();
        MovieRepository movieRepository = new MovieRepository(movieDao);
        WatchlistRepository watchlistRepository = new WatchlistRepository(watchlistDao);

        // 2) GUI laden
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 620);
            scene.getStylesheets().add(
                    Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm()
            );
            stage.setTitle("FHMDb!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Startfehler",
                    "Die Benutzeroberfläche konnte nicht geladen werden.\n" +
                            "Bitte starte die App neu oder kontaktiere den Support, falls das Problem besteht.");
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    // Utility‑Methode JFXAlerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}