package com.haryanvifolks.bajando.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.MediaControls.CurrentPlaylist;
import com.haryanvifolks.bajando.MediaControls.MediaPLayerService;
import com.haryanvifolks.bajando.MessageBox.PlaylistAdd;
import com.haryanvifolks.bajando.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SongListAdaper extends ArrayAdapter<Song> {
    private Context context;
    private int resource;
    private List<Song> songs = new ArrayList<>();
    public SongListAdaper(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.songs = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v==null){
            LayoutInflater vi = LayoutInflater.from(context);
            v=vi.inflate(resource,null);
        }

        final Song song = songs.get(position);
        if(song!=null){
            ImageView imageView = v.findViewById(R.id.list_item_image);
            TextView songName = v.findViewById(R.id.list_item_songName);
            TextView artistName = v.findViewById(R.id.list_item_artistName);
            LinearLayout linearLayout = v.findViewById(R.id.list_item_layout);
            final Button add = v.findViewById(R.id.list_item_addNext);
            Button download = v.findViewById(R.id.list_item_download);

            try{
                Picasso.get().load(song.getSongPicURL()).fit().into(imageView);
            }catch (Exception e){
                imageView.setBackgroundResource(R.drawable.ic_musical_note);
            }

            songName.setText(song.getSongName());
            artistName.setText(song.getArtistName());
            add.setBackgroundResource(R.drawable.ic_add_black_24dp);
            add.setClickable(true);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CurrentPlaylist.addSongInPlaylist(song);
                    CurrentPlaylist.setIndex(CurrentPlaylist.currentPlayList.size() - 1);
                    MediaPLayerService.playSong();
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CurrentPlaylist.addSongInPlaylist(song);
                    v.findViewById(R.id.list_item_addNext).setBackgroundResource(R.drawable.ic_done_black_24dp);
                    v.findViewById(R.id.list_item_addNext).setClickable(false);
                    MainActivity.addToPlaylist = song.getId();
                    PlaylistAdd playlistAdd = new PlaylistAdd((Activity)context);
                    playlistAdd.show();
                }
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(song.getDownload() == 0) {
                        Runnable runnable= new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.getMainContext(),
                                        "Will be introduced shortly", Toast.LENGTH_SHORT).show();
                                }
                        };
                        Thread the = new Thread(runnable);
                        the.start();
                    }
                    else
                        Toast.makeText(context,"Song Already Dowloaded",Toast.LENGTH_SHORT).show();
                }
            });
        }
        return v;
    }
}
