package at.ac.fhcampuswien.fhmdb.businesslayer;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.StringJoiner;

public class MovieCell extends ListCell<Movie> {

    // Text Box
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label();
    private final Label rating = new Label();
    private final Label releaseYear = new Label();
    private final VBox textLayout = new VBox(title, detail, genres, rating, releaseYear);

    // Button Box
    private final Button watchlistButton = new Button("To Watchlist");
    private final HBox buttonLayout = new HBox(watchlistButton);

    private final HBox mainLayout = new HBox();

    public MovieCell() {
    }

    // MovieCell Konstruktor
    public MovieCell(ClickEventHandler<Movie> onWatchlistClicked, boolean isInWatchlistView) {
        // Text Box Layout
        textLayout.setSpacing(10);
        textLayout.setPadding(new Insets(10));
        textLayout.setAlignment(Pos.CENTER_LEFT);

        // Button Box Layout
        buttonLayout.setSpacing(10);
        buttonLayout.setAlignment(Pos.CENTER_RIGHT);
        buttonLayout.setPadding(new Insets(10));

        // Main Layout
        HBox.setHgrow(textLayout, Priority.ALWAYS);
        mainLayout.getChildren().addAll(textLayout, buttonLayout);
        mainLayout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
        mainLayout.setAlignment(Pos.CENTER_LEFT);
        mainLayout.setSpacing(20);

        // Button-Styles
        watchlistButton.getStyleClass().add("button-cell");

        watchlistButton.setText(isInWatchlistView ? "Remove" : "To Watchlist");

        watchlistButton.setOnAction(event -> {
            if (getItem() != null && onWatchlistClicked != null) {
                onWatchlistClicked.onClick(getItem());
            }
        });
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            // Titel
            title.setText(movie.getTitle());

            // Beschreibung
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            detail.setWrapText(true);
            detail.setMaxWidth(500);

            // Genres
            if (movie.getGenres().isEmpty()) {
                genres.setText("");
            }
            else {
                StringJoiner joiner = new StringJoiner(", ");
                for (Genre genre : movie.getGenres()) {
                    joiner.add(genre.toString());
                }
                genres.setText(joiner.toString());
            }

            // Styles
            title.getStyleClass().add("title-cell");
            detail.getStyleClass().add("text-white");
            rating.setText("⭐ " + movie.getRating());
            releaseYear.setText("📅 " + movie.getReleaseYear());

            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genres.getStyleClass().add("text-gray");
            rating.getStyleClass().add("text-green");
            releaseYear.getStyleClass().add("text-blue");

            textLayout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            genres.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: #D3D3D3;");
            rating.setStyle("-fx-font-size: 14px; -fx-text-fill: #00FF00;");
            releaseYear.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFD700;");
            // Layout als Cell-Grafik anzeigen
            setGraphic(mainLayout);
        }
    }
}