package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class MovieAPI
{
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private static final OkHttpClient client = new OkHttpClient();

    public static List<Movie> fetchMovies(String query, Genre genre, Integer releaseYear, Double ratingFrom) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        if (query != null && !query.isEmpty()) urlBuilder.addQueryParameter("query", query);
        if (genre != null && !genre.name().equals("ALL")) urlBuilder.addQueryParameter("genre", genre.name());
        if (releaseYear != null) urlBuilder.addQueryParameter("releaseYear", releaseYear.toString());
        if (ratingFrom != null) urlBuilder.addQueryParameter("ratingFrom", ratingFrom.toString());

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .addHeader("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("API-Request fehlgeschlagen: " + response);

            String jsonResponse = response.body().string();
            return MovieParser.parseMovies(jsonResponse);
        }
    }
}
