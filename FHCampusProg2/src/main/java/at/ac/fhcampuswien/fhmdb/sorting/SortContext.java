package at.ac.fhcampuswien.fhmdb.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class SortContext {
    private SortState state;

    public void setState(SortState state) {
        this.state = state;
    }

    public List<Movie> sort(List<Movie> movies) {
        return state.sort(movies);
    }
}
