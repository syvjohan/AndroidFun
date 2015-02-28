package com.example.johan.assignment4;

/**
 * Created by johan on 2/28/2015.
 */
public class Song {
    private Long id;
    private String title = "";
    private String artist = "";

    Song(long id, String title, String artist) {
        this.id = id;
        this.artist = artist;
        this.title = title;
    }

    Song() {}

    public String getArtist() {
        return artist;
    }

    public void SetArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void SetTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void SetId(Long id) {
        this.id = id;
    }
}
