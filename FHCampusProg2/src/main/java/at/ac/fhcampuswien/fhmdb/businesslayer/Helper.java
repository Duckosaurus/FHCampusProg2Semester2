package at.ac.fhcampuswien.fhmdb.businesslayer;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class Helper {
    public static class MovieAPI {
        private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
        private static final OkHttpClient client = new OkHttpClient();

        public static List<Movie> fetchMovies(String query, Genre genre, String releaseYear, String rating) throws IOException, MovieApiException {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder();
            if (query != null && !query.isEmpty()) urlBuilder.addQueryParameter("query", query);
            if (genre != null && !genre.name().equals("ALL")) urlBuilder.addQueryParameter("genre", genre.name());
            if (releaseYear != null && !releaseYear.equals("Filter by Release Year"))
                urlBuilder.addQueryParameter("releaseYear", releaseYear);
            if (rating != null && !rating.equals("Filter by Rating"))
                urlBuilder.addQueryParameter("ratingFrom", rating);

            Request request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .addHeader("User-Agent", "http.agent")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new MovieApiException("API-Fehler: HTTP " + response.code());
                String jsonResponse = response.body().string();
                System.out.println(jsonResponse);
                return MovieParser.parseMovies(jsonResponse);
            }
        }
    }

    public static class MovieParser {
        private static final Gson gson = new Gson();

        public static List<Movie> parseMovies(String jsonResponse) {
            Type listType = new TypeToken<List<Movie>>() {
            }.getType();
            return gson.fromJson(jsonResponse, listType);
        }
    }
}
