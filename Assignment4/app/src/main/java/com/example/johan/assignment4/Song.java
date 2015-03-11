package com.example.johan.assignment4;

public class Song {
    private Long id;
    private String title = " ...";
    private String artist = "No track choosen!";
    private String uri = "";

    Song(long id, String title, String artist, String uri) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.uri = uri;
    }

    Song() {}

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String album) {
        this.uri = uri;
    }
}
