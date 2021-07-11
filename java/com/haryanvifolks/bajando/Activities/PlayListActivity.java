package com.haryanvifolks.bajando.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.haryanvifolks.bajando.Adapters.SongListRecyclerAdapter;
import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlayListActivity extends AppCompatActivity {
    RecyclerView currentList;
    Button playListPlay;
    TextView playListName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        final Intent intent = getIntent();
       // playListPlay = findViewById(R.id.playlist_play);
        playListName = findViewById(R.id.current_playlist);
        currentList = findViewById(R.id.current_ListView);
        currentList.setLayoutManager(new LinearLayoutManager(MainActivity.getMainContext()));
        currentList.setHasFixedSize(true);
        //Log.e("ValueIntent",String.valueOf(intent.getIntExtra(MainActivity.PLAYLIST_ID,0)));
       String sonngID ;
        String[] songNumbers = new String[25];
//                sonngID = MainActivity.songViewModel.getSongsFromPlaylist(intent.getIntExtra(MainActivity.PLAYLIST_ID,1));
//sonngID = MainActivity.playLists.get(intent.getIntExtra(MainActivity.PLAYLIST_ID,1)).getSongs();
        final List<Song> songs = new ArrayList<>();
      //  Log.e("ValueMonitor", sonngID[0]);
        final List<Integer> ids = new ArrayList<>();
        try {
             songNumbers = MainActivity.playLists.get(intent.getIntExtra(MainActivity.PLAYLIST_ID,0)-1)
                     .getSongs().split(" ", 0);
        }catch(Exception e){
            Log.e("ValueFind",e.toString());
        }
        for (String ss : songNumbers){
//            Log.e("ValueMonitor",ss);
            if (ss!=null)
            if(!ss.equals(""))
            {
                ids.add(Integer.parseInt(ss));
                Log.e("ValueMonitor",ids.get(0).toString());
            }
        }


        for(int i =0;i<ids.size();i++){
            final int finalI = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    songs.add(MainActivity.songViewModel.getSong(ids.get(finalI)));
                }
            };
            Thread t = new Thread(r);
            t.start();
        }

        final SongListRecyclerAdapter songListAdaper = new SongListRecyclerAdapter(songs,PlayListActivity.this);
        currentList.setAdapter(songListAdaper);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                songs.remove((int)viewHolder.itemView.getTag());
                songListAdaper.notifyDataSetChanged();
            }
        }).attachToRecyclerView(currentList);
    }
}
