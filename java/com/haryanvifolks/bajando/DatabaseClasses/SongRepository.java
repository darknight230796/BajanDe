package com.haryanvifolks.bajando.DatabaseClasses;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

public class SongRepository {
    private SongDao songDao;
    private LiveData<List<Song>> allSongs;
    private LiveData<List<Song>> topSongs;
    private LiveData<List<Artist>> allArtist;
    private LiveData<List<Artist>> topArtist;

    public SongRepository(Application application){
        SongDatabase database = SongDatabase.getInstance(application);
        songDao = database.songDao();
        allSongs = songDao.getAllSongs();
        topSongs = songDao.getTopSongs();
        allArtist = songDao.getAllArtist();
        topArtist = songDao.getTopArtist();
    }

    public List<Song> getSongs(){return songDao.getSongs();}
    public Song getSong(int id){return songDao.getSong(id);}
public List<Artist> getArtists(){return songDao.getArtists();}
    public void insert(Song song){
        new InsertSongAsyncTask(songDao).execute(song);
    }

    public LiveData<List<Artist>> getAllArtist() {
        return allArtist;
    }

    public LiveData<List<Artist>> getTopArtist() {
        return topArtist;
    }

    public void update(Song song){
        new UpdateSongAsyncTask(songDao).execute(song);
    }

    public LiveData<List<Song>> getAllSongs() {
        return allSongs;
    }

    public void downloadSong(int id){songDao.downloadSong(id);}

    public int getDownloadStatus(int id){
        return songDao.getDownloadStatus(id);
    }

    public LiveData<List<Song>> getTopSongs() {
        return topSongs;
    }

    public void insertArtist(Artist artist){ new InsertArtistAsyncTask(songDao).execute(artist);}

    public void updateArtist(Artist artist){ new UpdateArtistAsyncTask(songDao).execute(artist);}

    public List<Song> searchSong(String songName){return songDao.searchSong(songName);}
    public List<Artist> searchArtist(String artistName){return songDao.searchArtist(artistName);}

    public int maxID(){return songDao.maxID();}
    public int maxIDArtist(){return songDao.maxIDArtist();}

    public List<Song> searchSongByName(SimpleSQLiteQuery query){
        return songDao.searchSongByName(query);
    }

    public List<Artist> searchArtistByName(SimpleSQLiteQuery query){
        return songDao.searchArtistByName(query);
    }

    public void insertPlaylist(PlayListDatabase playListDatabase){
        songDao.insertPlaylist(playListDatabase);
    }

    public void updatePlaylist(PlayListDatabase playListDatabase){
        songDao.updatePlaylist(playListDatabase);
    }

    public PlayListDatabase getPlaylist(int id){
        return songDao.getPlaylist(id);
    }

    public LiveData<List<PlayListDatabase>> getAllPlaylist(){return songDao.getAllPlaylist();}

    public String getSongsFromPlaylist(int id){return songDao.getSongsFromPlaylist(id);}

    private static class InsertSongAsyncTask extends AsyncTask<Song,Void,Void>{
        private SongDao songDao;

        private InsertSongAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            songDao.insert(songs[0]);
            Log.e("TAG","REACHED");
            return null;
        }
    }
    private static class InsertArtistAsyncTask extends AsyncTask<Artist,Void,Void>{
        private SongDao songDao;

        private InsertArtistAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }
        @Override
        protected Void doInBackground(Artist... artists) {
            songDao.insertAtist(artists[0]);
            return null;
        }
    }
    private static class UpdateArtistAsyncTask extends AsyncTask<Artist,Void,Void>{
        private SongDao songDao;

        private UpdateArtistAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }
        @Override
        protected Void doInBackground(Artist... artists) {
            songDao.updateArtist(artists[0]);
            return null;
        }
    }
    private static class UpdateSongAsyncTask extends AsyncTask<Song,Void,Void>{
        private SongDao songDao;

        private UpdateSongAsyncTask(SongDao songDao) {
            this.songDao = songDao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            songDao.update(songs[0]);
            return null;
        }
    }

    public void deletePlaylist(PlayListDatabase playListDatabase){
        songDao.deletePlaylist(playListDatabase);
    }
}
