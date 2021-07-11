package com.haryanvifolks.bajando.DatabaseClasses;

public class GenreFormat {
    private String genreName;
    private String genreUrl;

    public GenreFormat(String genreName, String genreUrl) {
        this.genreName = genreName;
        this.genreUrl = genreUrl;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getGenreUrl() {
        return genreUrl;
    }
}
