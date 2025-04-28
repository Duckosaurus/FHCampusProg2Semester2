package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.businesslayer.HomeController;
import at.ac.fhcampuswien.fhmdb.datalayer.DatabaseManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) {
        // DB
        try {
            DatabaseManager.createConnectionsSource();
            DatabaseManager.createTables();
        }
        catch (Exception e) {
            HomeController.showAlert(Alert.AlertType.ERROR,
                    "Datenbankfehler",
                    "Es gab ein Problem beim Zugriff auf die lokale Datenbank.\n" +
                            "Bitte prüfe, ob die App die korrekten Rechte für den Ordner besitzt " +
                            "und ob genug freier Speicherplatz vorhanden ist. ");

            Platform.exit();
            return;
        }

        // View
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 905, 620);
            scene.getStylesheets().add(
                    Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm()
            );
            stage.setTitle("FHMDb!");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            HomeController.showAlert(Alert.AlertType.ERROR,
                    "Fehler beim Start",
                    "Die Benutzeroberfläche konnte nicht geladen werden.\n" +
                            "Wenn das Problem weiterhin besteht starte die Anwendung neu oder" +
                            "melde dich bei deinem Administrator.");
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}