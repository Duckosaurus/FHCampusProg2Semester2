package at.ac.fhcampuswien.fhmdb.businesslayer;

import at.ac.fhcampuswien.fhmdb.datalayer.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
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

    private Stage primaryStage;
    private DatabaseManager dbManager;
    private MovieRepository movieRepository;
    private WatchlistRepository watchlistRepository;

    public MainController(Stage stage) {
        this.primaryStage = stage;
        initialize();
    }

    private void initialize() {
        try {
            initializeDatabase();
            initializeRepositories();
            loadHomeView();

        } catch (InitializationException e) {
            showErrorAlert("Initialization Error", "Failed to initialize application: " + e.getMessage());
            e.printStackTrace(); // Log the full error
        }
    }

    private void initializeDatabase() throws InitializationException {
        try {
            dbManager = new DatabaseManager();
        } catch (DatabaseException e) {
            throw new InitializationException("Database initialization failed", e);
        }
    }

    private void initializeRepositories() throws InitializationException {
        try {
            Dao<MovieEntity, Long> movieDao = dbManager.getMovieDao();
            Dao<WatchlistMovieEntity, Long> watchlistDao = dbManager.getWatchlistDao();
            movieRepository = new MovieRepository(movieDao);
            watchlistRepository = new WatchlistRepository(watchlistDao);
        } catch (SQLException e) {
            throw new InitializationException("Repository initialization failed", e);
        }
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
        loadViewWithController("/at/ac/fhcampuswien/fhmdb/home-view.fxml", (HomeController controller) ->
                controller.setRepositories(watchlistRepository, movieRepository)
        );
        hideSidebar();
    }

    @FXML
    public void loadWatchlistView() {
        loadView("watchlist-view.fxml");
        hideSidebar();
    }

    private Pane loadView(String fxmlFile) {
        return loadViewWithController(fxmlFile, null);
    }

    private <T> Pane loadViewWithController(String fxmlFile, ControllerInitializer<T> initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/at/ac/fhcampuswien/fhmdb/" + fxmlFile)));
            Pane view = loader.load();
            if (initializer != null) {
                T controller = loader.getController();
                initializer.initialize(controller);
            }
            mainContent.getChildren().setAll(view);
            return view;
        } catch (IOException e) {
            showErrorAlert("View Load Error", "Could not load view: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void hideSidebar() {
        isSidebarVisible = false;
        sidebar.setVisible(false);
        sidebar.setManaged(false);
        hamburgerIcon.setText("☰");
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Functional Interface for Controller Initialization
    private interface ControllerInitializer<T> {
        void initialize(T controller);
    }

    // Custom Exception for Initialization Errors
    private static class InitializationException extends Exception {
        public InitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(App.class, args);
    }

    public static class App extends javafx.application.Application {
        @Override
        public void start(Stage stage) throws Exception {
            new MainController(stage);
        }
    }
}