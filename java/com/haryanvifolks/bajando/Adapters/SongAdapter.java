package com.haryanvifolks.bajando.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.MediaControls.CurrentPlaylist;
import com.haryanvifolks.bajando.MediaControls.MediaPLayerService;
import com.haryanvifolks.bajando.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    public List<Song> songs = new ArrayList<>();

    @NonNull
    @Override
    public SongAdapter.SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_card, parent, false);
       /* Runnable r = new Runnable() {
            @Override
            public void run() {
                songs = MainActivity.songViewModel.getSongs();
            }
        };
        Thread t = new Thread(r);
        t.start();
        */return new SongHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongHolder holder, final int position) {
        final Song song = songs.get(position);
        holder.songName.setText(song.getSongName());
        holder.artistName.setText(song.getArtistName());

        try {
            final int radius = 15;
            final int margin = 10;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);

            Picasso.get().load(song.getSongPicURL()).fit().transform(transformation).into(holder.songImg);
            //Picasso.get().load(song.getSongPicURL()).fit().into(holder.songImg);
        } catch (Exception e) {
            holder.songImg.setBackgroundResource(R.drawable.ic_musical_note);
            e.printStackTrace();
        }
        holder.songImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrentPlaylist.setCurrentPlayList(songs);
                CurrentPlaylist.setIndex(position);
                MediaPLayerService.playSong();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        private ImageButton songImg;
        private TextView songName;
        private TextView artistName;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            songImg = itemView.findViewById(R.id.songPic);
            songName = itemView.findViewById(R.id.songName);
            artistName = itemView.findViewById(R.id.artistName);
        }
    }
}
