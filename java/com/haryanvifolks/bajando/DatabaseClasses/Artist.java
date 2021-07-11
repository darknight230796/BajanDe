package com.haryanvifolks.bajando.DatabaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artist_table")
public class Artist{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String artistName;


    public void setArtistImgURL(String artistImgURL) {
        this.artistImgURL = artistImgURL;
    }

    private String artistImgURL;
    private int popularity;

    public Artist(String artistName, String artistPicUrl, int popularity) {
        this.artistName = artistName;
        this.artistImgURL = artistPicUrl;
        this.popularity = popularity;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtistPicUrl(String artistPicUrl) {
        this.artistImgURL = artistPicUrl;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public Artist() {
    }

    public void updatePopularity(){
        popularity++;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistImgURL() {
        return artistImgURL;
    }

    public int getPopularity() {
        return popularity;
    }
}
