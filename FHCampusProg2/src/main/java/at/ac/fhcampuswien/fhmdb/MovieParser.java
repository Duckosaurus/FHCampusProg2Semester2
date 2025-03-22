package at.ac.fhcampuswien.fhmdb;

import com.google.gson.Gson;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class MovieParser
{
    private static final Gson gson = new Gson();

    public static List<Movie> parseMovies(String jsonResponse) {
        Type listType = new TypeToken<List<Movie>>() {}.getType();
        return gson.fromJson(jsonResponse, listType);
    }
}
