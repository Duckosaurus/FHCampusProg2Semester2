package at.ac.fhcampuswien.fhmdb.datalayer;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String apiId;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private String genres;

    @DatabaseField(canBeNull = false)
    private int releaseYear;

    @DatabaseField
    private String imgUrl;

    @DatabaseField
    private int lengthMinutes;

    @DatabaseField
    private double rating;

    // Default constructor (necessary for ORMLite)
    public MovieEntity() {
    }

    // Constructor with parameters (optional, for convenience)
    public MovieEntity(String apiId, String title, String description, String genres, int releaseYear, String imgUrl, int lengthMinutes, double rating) {
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthMinutes = lengthMinutes;
        this.rating = rating;
    }


    // Converts a List<Genre> or EnumSet<Genre> to a comma-separated String
    public static String genresToString(List<Genre> genres) { // Adapt type if needed (e.g., EnumSet<Genre>)
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        return genres.stream()
                .map(Genre::name)  // Get the name of each enum value
                .collect(Collectors.joining(","));
    }

    // Converts a comma-separated String to a List<Genre>
    public static List<Genre> stringToGenres(String genresString) {
        if (genresString == null || genresString.isEmpty()) {
            return List.of(); // Or Collections.emptyList() for immutability
        }
        return Arrays.stream(genresString.split(","))
                .map(String::trim)  // Remove extra spaces
                .map(Genre::valueOf) // Convert string to Enum value
                .collect(Collectors.toList());
    }


    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getApiId() { return apiId; }
    public void setApiId(String apiId) { this.apiId = apiId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public int getLengthMinutes() { return lengthMinutes; }
    public void setLengthMinutes(int lengthMinutes) { this.lengthMinutes = lengthMinutes; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }




    // Converts a List<Movie> to a List<MovieEntity>
    public static List<MovieEntity> fromMovies(List<Movie> movies) {  // Assuming you have a Movie class
        // Implementation depends on your Movie class structure
        return null; // Replace with actual implementation
    }

    // Converts a List<MovieEntity> to a List<Movie>
    public static List<Movie> toMovies(List<MovieEntity> movieEntities) { // Assuming you have a Movie class
        // Implementation depends on your Movie class structure
        return null; // Replace with actual implementation
    }
}