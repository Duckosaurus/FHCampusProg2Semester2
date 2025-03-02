package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres;


    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * Erstellt statische Testdaten in Form von Filmen die in der GUI angezeigt werden
     *
     * @return Liste mit Film Objekten
     */
    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Inception", "A mind-bending thriller.", List.of(Genre.SCIENCE_FICTION, Genre.THRILLER)));
        movies.add(new Movie("Bibi und Tina", "notnull", List.of(Genre.FAMILY, Genre.ANIMATION)));
        movies.add(new Movie("Titanic", "A love story on the Titanic.", List.of(Genre.ROMANCE, Genre.DRAMA)));
        movies.add(new Movie("Inception", "A thief enters dreams to steal secrets but faces unexpected challenges.",
                List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.THRILLER)));
        movies.add(new Movie("The Godfather", "The aging patriarch of an organized crime dynasty transfers control to his reluctant son.",
                List.of(Genre.CRIME, Genre.DRAMA)));
        movies.add(new Movie("The Dark Knight", "Batman battles the Joker, who plunges Gotham into chaos.",
                List.of(Genre.ACTION, Genre.CRIME, Genre.DRAMA)));
        movies.add(new Movie("Forrest Gump", "A slow-witted but kind-hearted man witnesses key historical moments in America.",
                List.of(Genre.DRAMA, Genre.ROMANCE)));
        movies.add(new Movie("Interstellar", "A team of astronauts travels through a wormhole to find a new home for humanity.",
                List.of(Genre.SCIENCE_FICTION, Genre.ADVENTURE, Genre.DRAMA)));
        movies.add(new Movie("Parasite", "A poor family schemes to become employed by a wealthy household.", List.of(Genre.DRAMA, Genre.THRILLER, Genre.COMEDY)));
        movies.add(new Movie("The Grand Budapest Hotel", "A concierge and his protégé navigate a series of adventures in a famous hotel.",
                List.of(Genre.COMEDY, Genre.DRAMA, Genre.CRIME)));
        movies.add(new Movie("Spirited Away", "A young girl enters a mysterious world ruled by spirits and gods.",
                List.of(Genre.ANIMATION, Genre.FANTASY, Genre.ADVENTURE)));
        movies.add(new Movie("Mad Max: Fury Road", "A post-apocalyptic warrior joins forces with a rebel to overthrow a tyrant.",
                List.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION)));
        return movies;
    }
}
