package com.haryanvifolks.bajando.DatabaseClasses;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

public class SongViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private LiveData<List<Song>> allsongs;
    private LiveData<List<Song>> topsongs;
    private LiveData<List<Artist>> allArtist;
    private LiveData<List<Artist>> topArtist;

    public SongViewModel(@NonNull Application application) {
        super(application);
        songRepository = new SongRepository(application);
        allsongs = songRepository.getAllSongs();
        topsongs = songRepository.getTopSongs();
        allArtist = songRepository.getAllArtist();
        topArtist = songRepository.getTopArtist();
    }

    public int maxID(){return songRepository.maxID();}
    public int maxIDArtist(){return songRepository.maxIDArtist();}

    public List<Song> getSongs(){return songRepository.getSongs();}

    public List<Artist> getArtists(){return songRepository.getArtists();}

    public Song getSong(int id){return songRepository.getSong(id);}

    public void insert(Song song){
        songRepository.insert(song);
    }

    public void update(Song song){
        songRepository.update(song);
    }

    public LiveData<List<Song>> getAllsongs(){
        return allsongs;
    }

    public LiveData<List<Song>> getTopsongs(){
        return topsongs;
    }

    public void downloadSong(int id){
        songRepository.downloadSong(id);
    }

    public int getDownloadStatus(int id){
        return songRepository.getDownloadStatus(id);
    }

    public void insertArtist(Artist artist){songRepository.insertArtist(artist);}

    public void updateArtist(Artist artist){songRepository.updateArtist(artist);}

    public List<Song> searchSong(String songName){return songRepository.searchSong(songName);}
    public List<Artist> searchArtist(String artistName){return songRepository.searchArtist(artistName);}

    public LiveData<List<Artist>> getAllArtist() {
        return allArtist;
    }

    public LiveData<List<Artist>> getTopArtist() {
        return topArtist;
    }

    public List<Song> searchSongByName(SimpleSQLiteQuery query){
        return songRepository.searchSongByName(query);
    }

    public List<Artist> searchArtistByName(SimpleSQLiteQuery query){
        return songRepository.searchArtistByName(query);
    }

    public void insertPlaylist(PlayListDatabase playListDatabase){
        songRepository.insertPlaylist(playListDatabase);
    }

    public void updatePlaylist(PlayListDatabase playListDatabase){
        songRepository.updatePlaylist(playListDatabase);
    }

    public PlayListDatabase getPlaylist(int id){
       return songRepository.getPlaylist(id);
    }

    public String getSongsFromPlaylist(int id){
        return songRepository.getSongsFromPlaylist(id);
    }
    public LiveData<List<PlayListDatabase>> getAllPlaylist(){return songRepository.getAllPlaylist();}

    public void deletePlaylist(PlayListDatabase playListDatabase){
        songRepository.deletePlaylist(playListDatabase);
    }
}
