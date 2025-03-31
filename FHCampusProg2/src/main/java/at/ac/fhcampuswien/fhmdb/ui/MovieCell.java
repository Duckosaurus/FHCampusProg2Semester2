package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label();
    private final Label rating = new Label();
    private final Label releaseYear = new Label();
    private final VBox layout = new VBox(title, detail, genres);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            // genre
            if (!movie.getGenres().isEmpty()) {
                String formattedGenres = movie.getGenres().stream()
                        .map(Genre::name)
                        .collect(Collectors.joining(", "));

                genres.setText(formattedGenres);

            } else {
                genres.setText("No genres available");
            }
            rating.setText("⭐ " + movie.getRating());
            releaseYear.setText("📅 " + movie.getReleaseYear());

            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genres.getStyleClass().add("text-gray");
            rating.getStyleClass().add("text-green");
            releaseYear.getStyleClass().add("text-blue");

            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            genres.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: #D3D3D3;");
            rating.setStyle("-fx-font-size: 14px; -fx-text-fill: #00FF00;");
            releaseYear.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFD700;");

            detail.setWrapText(true);
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            layout.setPadding(new Insets(10));
            layout.setSpacing(5);

            HBox extraInfo = new HBox(10, releaseYear, rating);
            extraInfo.setSpacing(20);

            layout.getChildren().setAll(title, genres, extraInfo, detail);
            setGraphic(layout);
        }
    }
}

