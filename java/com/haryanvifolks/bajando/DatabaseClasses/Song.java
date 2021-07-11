package com.haryanvifolks.bajando.DatabaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "song_table")
public class Song {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String songName;
    private String artistName;
    private String genreName;
    private String albumName;
    private String songURL;
    private String songPicURL;
    private int counter;
    private int download;

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

    public void setSongPicURL(String songPicURL) {
        this.songPicURL = songPicURL;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Song() {
    }

    public Song(String songName, String artistName, String genreName,
                String albumName, String songURL, String songPicURL, int counter) {
        this.songName = songName;
        this.artistName = artistName;
        this.genreName = genreName;
        this.albumName = albumName;
        this.songURL = songURL;
        this.songPicURL = songPicURL;
        this.counter = counter;
        this.download = 0;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void updateCounter(){
        counter++;
    }

    public int getId() {
        return id;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getSongURL() {
        return songURL;
    }

    public String getSongPicURL() {
        return songPicURL;
    }

    public int getCounter() {
        return counter;
    }

    public void setDownload(int i){
        this.download = i;
    }
    public int getDownload(){
        return download;
    }
}
