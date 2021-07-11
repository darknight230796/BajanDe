package com.haryanvifolks.bajando.DatabaseClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.haryanvifolks.bajando.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Song.class, Artist.class, PlayListDatabase.class}, version = 6, exportSchema = false)
public abstract class SongDatabase extends RoomDatabase {

    private static SongDatabase instance;

    public abstract SongDao songDao();

    public static synchronized SongDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SongDatabase.class, "song_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateSongDB(instance).execute();
        }
    };

    public static void insertSong(Song song){
        new InsertDatabase(instance).execute(song);
    }

    public static void insertArtist(Artist artist){
        new InsertAstistDatabase(instance).execute(artist);
    }

    private static class InsertAstistDatabase extends AsyncTask<Artist,Void,Void>{

        private SongDao songDao;

        public InsertAstistDatabase(SongDatabase songDatabase) {
            songDao = songDatabase.songDao();
        }

        @Override
        protected Void doInBackground(Artist... artists) {

            try {
                songDao.insertAtist(artists[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    private static class InsertDatabase extends AsyncTask<Song,Void,Void>{

        private SongDao songDao;

        private  InsertDatabase(SongDatabase songDatabase) { songDao = songDatabase.songDao();}

        @Override
        protected Void doInBackground(Song... songs) {

            try {
                    songDao.insert(songs[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private static class PopulateSongDB extends AsyncTask<Void, Void, Void> {
        private SongDao songDao;

        private PopulateSongDB(SongDatabase songDatabase) {
            songDao = songDatabase.songDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String json = null;
            try {
                InputStream is = MainActivity.getMainContext()
                        .getAssets().open("bajandedata.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (Exception e) {
                Log.e("ErrorInRead", e.toString());
            }

            try {
                JSONArray JA = new JSONArray(json);
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject JO = (JSONObject) JA.get(i);
                    songDao.insert(new Song(JO.getString("songName"),
                            JO.getString("artistName"),
                            JO.getString("genreName"),
                            JO.getString("albumName"),
                            JO.getString("songURL"),
                            JO.getString("songPicURL"),
                            0));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                InputStream is = MainActivity.getMainContext().getAssets()
                        .open("artist.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (Exception e) {
                Log.e("ErrorInRead", e.toString());
            }

            try {
                JSONArray JA = new JSONArray(json);
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject JO = (JSONObject) JA.get(i);
                    songDao.insertAtist(new Artist(JO.getString("artistName"),
                            JO.getString("artistImgURL"),
                            0));
                }
            } catch (JSONException e) {
                Log.e("DATABASE", e.toString());
            }
            return null;
        }
    }
}
