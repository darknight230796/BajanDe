package com.haryanvifolks.bajando.MessageBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.haryanvifolks.bajando.DatabaseClasses.PlayListDatabase;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlaylistAdd extends Dialog{
    public Activity c;
    public Button addPlaylist,doneButton;
    public ListView playlistListView;

    public PlaylistAdd(@NonNull Activity activity) {
        super(activity);
        this.c = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.playlist_add_msg_box);
        addPlaylist = findViewById(R.id.new_playlist_button);
        doneButton = findViewById(R.id.done_button);
        playlistListView = findViewById(R.id.playListListView);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistName playlistName = new PlaylistName(c);
                playlistName.show();
                dismiss();
        }
        });
        playlistListView.setAdapter(new PlayListAdapter(MainActivity.getMainContext(),R.layout.playlist_list_item,MainActivity.playLists));

    }

    public class PlayListAdapter extends ArrayAdapter<PlayListDatabase>{

        TextView playListName;
        Context context;
        int resource;
        List<PlayListDatabase> playlists;

        public PlayListAdapter(@NonNull Context context, int resource, @NonNull List<PlayListDatabase> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.playlists = objects;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v==null){
                LayoutInflater vi = LayoutInflater.from(context);
                v=vi.inflate(resource,null);
            }
            final PlayListDatabase playlist = playlists.get(position);
            if (playlist!=null){
                playListName=v.findViewById(R.id.playlist_name);
                playListName.setText(playlist.getPlaylistname());
                playListName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable runnable= new Runnable() {
                            @Override
                            public void run() {
                                playlist.setSongs(playlist.getSongs()+" "+String.valueOf(MainActivity.addToPlaylist));
                                MainActivity.songViewModel.updatePlaylist(playlist);
                            }
                        };
                        Thread the = new Thread(runnable);
                        the.start();
                        dismiss();
                    }
                });
            }
            return v;
        }
    }
}
