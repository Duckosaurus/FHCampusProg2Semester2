package at.ac.fhcampuswien.fhmdb.businesslayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MainController {

    public BorderPane mainLayout;
    @FXML
    private StackPane mainContent;
    @FXML
    private VBox sidebar;
    @FXML
    private Label hamburgerIcon;
    private boolean isSidebarVisible = false;

    @FXML
    public void initialize() {
        loadHomeView();
    }

    @FXML
    public void toggleSidebar() {
        isSidebarVisible = !isSidebarVisible;
        sidebar.setVisible(isSidebarVisible);
        sidebar.setManaged(isSidebarVisible);
        hamburgerIcon.setText(isSidebarVisible ? "✖" : "☰");
    }

    @FXML
    public void loadHomeView() {
        loadView("home-view.fxml");
        hideSidebar();
    }

    @FXML
    public void loadWatchlistView() {
        loadView("watchlist-view.fxml");
        hideSidebar();
    }

    private void hideSidebar() {
        isSidebarVisible = false;
        sidebar.setVisible(false);
        sidebar.setManaged(false);
        hamburgerIcon.setText("☰");
    }

    private void loadView(String fxmlFile) {
        try {
            Pane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/at/ac/fhcampuswien/fhmdb/" + fxmlFile)));
            mainContent.getChildren().setAll(view);
        }
        catch (Exception e) {
            HomeController.showAlert(Alert.AlertType.ERROR,
                    "Fehler beim Laden der Ansicht",
                    "Beim Laden der Ansicht ist ein Fehler aufgetreten. Bitte starte die Anwendung neu.");
        }
    }
}