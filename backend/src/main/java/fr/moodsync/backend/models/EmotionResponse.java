package fr.moodsync.backend.models;

import java.util.List;

public class EmotionResponse {
    private String emotion;
    private String suggestion;
    private List<Movie> movies;

    public EmotionResponse(String emotion, String suggestion, List<Movie> movies) {
        this.emotion = emotion;
        this.suggestion = suggestion;
        this.movies = movies;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
