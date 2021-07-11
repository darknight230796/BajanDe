package com.haryanvifolks.bajando.DatabaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_table")
public class PlayListDatabase {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String playlistname;
    private String songs;

    public PlayListDatabase(String playlistname, String songs) {
        this.playlistname = playlistname;
        this.songs = songs;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSongs(String songs) {
        this.songs = songs;
    }

    public int getId() {
        return id;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public String getSongs() {
        return songs;
    }
}
