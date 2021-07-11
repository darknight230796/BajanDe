package com.haryanvifolks.bajando.DatabaseClasses;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface SongDao {
    @Insert
    void insert(Song song);

    @Update
    void update(Song song);

    @Query("SELECT * FROM song_table WHERE id LIKE :id")
    Song getSong(int id);


    @Query("SELECT id FROM song_table ORDER BY id DESC LIMIT 1")
    int maxID();

    @Query("SELECT id FROM artist_table ORDER BY id DESC LIMIT 1")
    int maxIDArtist();


    @Query("SELECT * FROM song_table ORDER BY counter DESC LIMIT 50")
    LiveData<List<Song>> getTopSongs();

    @Query("SELECT * FROM song_table LIMIT 50")
    LiveData<List<Song>> getAllSongs();

    @Query("SELECT * FROM song_table")
    List<Song> getSongs();

    @Query("SELECT * FROM artist_table LIMIT 50")
    List<Artist> getArtists();

    @Insert
    void insertAtist(Artist artist);

    @Update
    void updateArtist(Artist artist);

    @Query("SELECT * FROM artist_table ORDER BY id DESC LIMIT 20")
    LiveData<List<Artist>> getAllArtist();

    @Query("SELECT * FROM artist_table ORDER BY popularity DESC LIMIT 10")
    LiveData<List<Artist>> getTopArtist();

    @Query("SELECT * FROM song_table WHERE songName LIKE :songName")
    public abstract List<Song> searchSong(String songName);

    @Query("SELECT * FROM artist_table WHERE artistName LIKE :artistName")
    public abstract List<Artist> searchArtist(String artistName);

    @RawQuery(observedEntities = Song.class)
    public abstract List<Song> searchSongByName(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Artist.class)
    public abstract List<Artist> searchArtistByName(SupportSQLiteQuery query);

    @Insert
    void insertPlaylist(PlayListDatabase playListDatabase);

    @Update
    void updatePlaylist(PlayListDatabase playListDatabase);

    @Query("SELECT * FROM playlist_table WHERE id LIKE :id")
    PlayListDatabase getPlaylist(int id);

    @Query("SELECT * FROM playlist_table")
    LiveData<List<PlayListDatabase>> getAllPlaylist();

    @Query("UPDATE song_table SET download = 1 WHERE id LIKE :id")
    void downloadSong(int id);

    @Query("SELECT download FROM song_table WHERE id LIKE :id")
    int getDownloadStatus(int id);

    @Query("SELECT songs FROM playlist_table WHERE id LIKE :id")
    String getSongsFromPlaylist(int id);

    @Delete
    void deletePlaylist(PlayListDatabase playListDatabase);

}
