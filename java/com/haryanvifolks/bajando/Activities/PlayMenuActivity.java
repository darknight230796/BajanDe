package com.haryanvifolks.bajando.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.haryanvifolks.bajando.Adapters.SongListRecyclerAdapter;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.MediaControls.CurrentPlaylist;
import com.haryanvifolks.bajando.R;
import com.haryanvifolks.bajando.UI_Controls.UIControls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.haryanvifolks.bajando.MainActivity.mediaPlayer;
import static com.haryanvifolks.bajando.MainActivity.mediaPlayerController;
import static com.haryanvifolks.bajando.R.id.playListListView;

public class PlayMenuActivity extends AppCompatActivity {

    public static ImageView iv;
    public static Button playPause;
    public static Button next;
    public static Button previous;
    public static Button repeat;
    public static Button shuffle;
    public static TextView songName;
    public static TextView artistName;
    public static RecyclerView songList;
    public static SeekBar playMenuSeekBar;
    public static TextView currentTime;
    public static TextView maxTime;
    public static ProgressBar playMenuProgress;
    public static Button playMenuCast;
    public static Activity activity;

    public static CurrentPlaylist curPlaylist;

    @SuppressLint("StaticFieldLeak")
    public static SongListRecyclerAdapter songListRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        MainActivity.playActivityRunning = true;
        setContentView(R.layout.activity_play_menu);

        RecyclerView recycler_view_1 = findViewById(playListListView);
       // AnimationDrawable animationDrawable = (AnimationDrawable) recycler_view_1.getBackground();
       // animationDrawable.setEnterFadeDuration(2000);
       // animationDrawable.setExitFadeDuration(4000);
//        animationDrawable.start();

        curPlaylist = new CurrentPlaylist(CurrentPlaylist.currentPlayList,CurrentPlaylist.index);

        iv = findViewById(R.id.playMenuImage);
        playPause = findViewById(R.id.playMenuPlay);
        next = findViewById(R.id.playMenuNext);
        previous = findViewById(R.id.playMenuPrevious);
        repeat = findViewById(R.id.playMenuRepeat);
        shuffle = findViewById(R.id.playMenuShuffle);
        songName = findViewById(R.id.playMenuSongName);
        artistName = findViewById(R.id.playMenuArtistName);
        playMenuProgress = findViewById(R.id.playMenuProgressbar);
        playMenuCast = findViewById(R.id.playMenuCast);
        songList = findViewById(playListListView);

        currentTime = findViewById(R.id.currentTime);
        maxTime = findViewById(R.id.maxTime);

        playMenuSeekBar = findViewById(R.id.playMenuSeekBar);

        try {
             songListRecyclerAdapter =
                     new SongListRecyclerAdapter(CurrentPlaylist.currentPlayList,
                             PlayMenuActivity.this);

            songList.setLayoutManager(new LinearLayoutManager(MainActivity.getMainContext()));
            songList.setAdapter(songListRecyclerAdapter);
        }catch (Exception  e){
            Log.e("Error Here",e.toString());
        }


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                CurrentPlaylist.currentPlayList.remove((int)viewHolder.itemView.getTag());
                songListRecyclerAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(songList);

        activity = PlayMenuActivity.this;

        //playMenuProgress.setVisibility(View.VISIBLE);



        playMenuCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings",
                        "com.android.settings.bluetooth.BluetoothSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity( intent);
            }catch (Exception e){
                    Toast.makeText(PlayMenuActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }}
        });


        playPause.setOnClickListener(onClickListener);
        next.setOnClickListener(onClickListener);
        previous.setOnClickListener(onClickListener);
        repeat.setOnClickListener(onClickListener);
        shuffle.setOnClickListener(onClickListener);

        UIControls.runProgress();

    }

    public static Activity getPlayActivity(){
        return activity;
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        UIControls.runProgress();
        UIControls.updatePlayMenu();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MainActivity.playActivityRunning = false;
    }

    private Button.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playMenuPlay:
                    mediaPlayerController.pause();
                    break;
                case R.id.playMenuNext:
                    mediaPlayerController.next();
                    break;
                case R.id.playMenuPrevious:
                    mediaPlayerController.previous();
                    break;
                case R.id.playMenuShuffle:
                    mediaPlayerController.shuffle();
                    break;
                case R.id.playMenuRepeat:
                    mediaPlayerController.repeat();
                    break;
                default:
                    break;
            }
        }
    };
}
