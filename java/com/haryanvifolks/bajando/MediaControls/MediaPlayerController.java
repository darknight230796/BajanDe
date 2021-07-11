package com.haryanvifolks.bajando.MediaControls;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;


import com.haryanvifolks.bajando.Activities.PlayMenuActivity;
import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.UI_Controls.UIControls;

import java.io.IOException;

import static androidx.core.content.ContextCompat.startActivity;
import static com.haryanvifolks.bajando.MainActivity.current_Playlist;
import static com.haryanvifolks.bajando.MainActivity.mediaPlayer;
import static com.haryanvifolks.bajando.MainActivity.songViewModel;
import static com.haryanvifolks.bajando.MainActivity.songsForArtist;


public class MediaPlayerController {


    public MediaPlayerController() {

    }


    public void play() {

        UIControls.startProgressBar();
        Song song;
        if (current_Playlist !=null)
           song  = CurrentPlaylist.currentPlayList.get(CurrentPlaylist.index);
        else
            song = songsForArtist.get(0);
    //    if (!serviceBound){
            Intent playerIntent = new Intent(MainActivity.getMainContext(),MediaPLayerService.class);
            MainActivity.getMainContext().startService(playerIntent);
            MainActivity.getMainContext().bindService(playerIntent,MainActivity.serviceConnection,Context.BIND_AUTO_CREATE);
    //    }

        MainActivity.mediaPlayerPreparedState = false;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else
            mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song.getSongURL());
            mediaPlayer.prepareAsync();
            MediaPLayerService.onPrepared();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getMainContext(), "Error in Play", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (MainActivity.playActivityRunning)
            UIControls.updatePlayMenu();
        }catch (Exception e){
            e.printStackTrace();
        }
        song.updateCounter();
        songViewModel.update(song);
        MediaPLayerService.onError();
        MediaPLayerService.onCompletion();
        MediaPLayerService.onBuffer();
    }

    public void pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                UIControls.pause(true);
                MediaPLayerService.buildNotification(MediaPLayerService.PlaybackStatus.PAUSED);
            } else {
                mediaPlayer.start();
                UIControls.pause(false);
                MediaPLayerService.buildNotification(MediaPLayerService.PlaybackStatus.PLAYING);
            }
        }
    }

    public void stop() {
        MainActivity.mediaPlayerPreparedState = false;
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    public void next() {
        if (MainActivity.repeatState) {
            MediaPLayerService.playSong();
        }
        else {
            if (MainActivity.shuffleState) {
                MainActivity.current_Playlist.setIndex((int) (Math.random() *
                        (CurrentPlaylist.currentPlayList.size())));
            }
            else
                current_Playlist.nextIndex();
            MediaPLayerService.playSong();
        }
    }

    public void previous() {

        current_Playlist.previousIndex();
        MediaPLayerService.playSong();

    }

    public void shuffle() {
        if (MainActivity.shuffleState)
            MainActivity.shuffleState = false;
        else
            MainActivity.shuffleState = true;
        UIControls.updateRepeatShuffle();
    }

    public void repeat() {

        if (MainActivity.repeatState)
            MainActivity.repeatState = false;
        else

            MainActivity.repeatState = true;
        UIControls.updateRepeatShuffle();
    }

    public static void startPlayMenu() {
        Intent intent = new Intent(MainActivity.getMainContext(),PlayMenuActivity.class);
        startActivity(MainActivity.getMainContext(),intent,null);


    }
}