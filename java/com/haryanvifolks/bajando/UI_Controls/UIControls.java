package com.haryanvifolks.bajando.UI_Controls;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.akaita.android.circularseekbar.CircularSeekBar;
import com.haryanvifolks.bajando.Activities.PlayMenuActivity;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.MediaControls.CurrentPlaylist;
import com.haryanvifolks.bajando.R;
import com.squareup.picasso.Picasso;

import static com.haryanvifolks.bajando.Activities.PlayMenuActivity.currentTime;
import static com.haryanvifolks.bajando.Activities.PlayMenuActivity.maxTime;
import static com.haryanvifolks.bajando.Activities.PlayMenuActivity.playMenuSeekBar;
import static com.haryanvifolks.bajando.MainActivity.circularSeekBar;
import static com.haryanvifolks.bajando.MainActivity.mediaPlayer;

public class UIControls {

    private static int duration;

    private static Thread playMenuThread;

    public static int getDuration() {
        return duration;
    }

    public static void setDuration(int duration) {
        UIControls.duration = duration;
    }

    public static void updatePlayMenu() {
        //to refresh play menu
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                pause(false);
        }
        updateRepeatShuffle();

        try {
            Picasso.get().load(CurrentPlaylist.getCurrentPlayList().get(
                    CurrentPlaylist.getIndex()).getSongPicURL()).fit().into(
                    PlayMenuActivity.iv);
        } catch (Exception e) {
            PlayMenuActivity.iv.setBackgroundResource(R.drawable.ic_musical_note);
        }
        PlayMenuActivity.songName.setText(CurrentPlaylist.getCurrentPlayList().get(
                CurrentPlaylist.getIndex()).getSongName());
        PlayMenuActivity.artistName.setText(CurrentPlaylist.getCurrentPlayList().get(
                CurrentPlaylist.getIndex()).getArtistName());
    }


    public static void runProgress() {
        //to run seekbars

        if (circularSeekBar!=null)
        circularSeekBar.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo((int) circularSeekBar.getProgress());
                } else
                    circularSeekBar.setProgress(0);
            }
        });

        circularSeekBar.setMin(0);
        circularSeekBar.setMax(getDuration());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
              // circular seek bar
                circularSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        };

        Thread circle = new Thread(runnable);
        circle.start();

        if (MainActivity.playActivityRunning) {
            playMenuSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo(playMenuSeekBar.getProgress());
                        try {
                            playMenuThread.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        playMenuSeekBar.setProgress(0);
                }
            });

            Runnable runnable1 = new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    while (mediaPlayer == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    while (!mediaPlayer.isPlaying()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    playMenuSeekBar.setProgress(0);
                    while (mediaPlayer != null) {
                        PlayMenuActivity.getPlayActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    playMenuSeekBar.setMax(duration);
                                } catch (Exception e) {
                                    Log.e("ProblemHere", e.toString());
                                }
                                try {
                                    maxTime.setText(duration / 60000
                                            + ":" + ((duration / 1000) % 60 >= 10
                                            ? (duration / 1000) % 60
                                            : "0" + (duration / 1000) % 60));
                                } catch (Exception e) {
                                    maxTime.setText("00:00");
                                    Log.e("STUCK HERE", "MAX TIME");
                                }
                                try {
                                    playMenuSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                                } catch (Exception e) {

                                    Log.e("STUCK HERE", "progress TIME");
                                }
                                try {
                                    currentTime.setText(mediaPlayer.getCurrentPosition() / 60000
                                            + ":" + ((mediaPlayer.getCurrentPosition() / 1000) % 60 >= 10
                                            ? (mediaPlayer.getCurrentPosition() / 1000) % 60
                                            : "0" + (mediaPlayer.getCurrentPosition() / 1000) % 60));
                                } catch (Exception e) {
                                    currentTime.setText("00:00");

                                    Log.e("STUCK HERE", "current TIME");
                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }

            };

            playMenuThread = new Thread(runnable1);
            try {
                playMenuThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public static void pause(Boolean b) {
        //to update play button
        if (b) {
            //pause

            MainActivity.play_button.setImageResource(R.drawable.ic_fab_play);
            if (MainActivity.playActivityRunning)
                PlayMenuActivity.playPause.setBackgroundResource(R.drawable.ic_play);
        } else {
            //play

            MainActivity.play_button.setImageResource(R.drawable.ic_fab_pause);
            if (MainActivity.playActivityRunning)
                PlayMenuActivity.playPause.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    public static void updateRepeatShuffle() {
        // to update repeat / shuffle ui
        if (MainActivity.playActivityRunning) {
            if (MainActivity.repeatState)
                PlayMenuActivity.repeat.setBackgroundResource(R.drawable.ic_repeat_one);
            else
                PlayMenuActivity.repeat.setBackgroundResource(R.drawable.ic_repeat);

            if (MainActivity.shuffleState)
                PlayMenuActivity.shuffle.setBackgroundResource(R.drawable.ic_shuffle_on);
            else
                PlayMenuActivity.shuffle.setBackgroundResource(R.drawable.ic_shuffle_off);
        }


            if (MainActivity.repeatState)
                MainActivity.repeat_button.setImageResource(R.drawable.ic_repeat_one);
            else
                MainActivity.repeat_button.setImageResource(R.drawable.ic_repeat);

            if (MainActivity.shuffleState)
                MainActivity.shuffle_button.setImageResource(R.drawable.ic_shuffle_on);
            else
                MainActivity.shuffle_button.setImageResource(R.drawable.ic_shuffle_off);

    }

    public static void stopProgressBar() {
        MainActivity.mainProgressbar.setVisibility(View.INVISIBLE);
        MainActivity.play_button.setClickable(true);
        if (MainActivity.playActivityRunning) {
            PlayMenuActivity.playMenuProgress.setVisibility(View.INVISIBLE);
            PlayMenuActivity.playPause.setClickable(true);
        }
    }

    public static void startProgressBar() {
        MainActivity.mainProgressbar.setVisibility(View.VISIBLE);
        MainActivity.play_button.setClickable(false);
        if (MainActivity.playActivityRunning) {
            PlayMenuActivity.playMenuProgress.setVisibility(View.VISIBLE);
            PlayMenuActivity.playPause.setClickable(false);
        }
    }
}
