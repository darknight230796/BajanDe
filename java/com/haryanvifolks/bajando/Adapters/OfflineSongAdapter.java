package com.haryanvifolks.bajando.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.MediaControls.CurrentPlaylist;
import com.haryanvifolks.bajando.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfflineSongAdapter extends RecyclerView.Adapter<OfflineSongAdapter.SongListHolder> {
    public List<Song> songs = new ArrayList<>();
    private Context context;

    public OfflineSongAdapter(List<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public OfflineSongAdapter.SongListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(MainActivity.getMainContext())
                .inflate(R.layout.list_item,parent,false);
        return new SongListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OfflineSongAdapter.SongListHolder holder, final int position) {
        final Song s = songs.get(position);
        try{
            Picasso.get().load(s.getSongPicURL()).fit().into(holder.songImg);
        }catch (Exception e){
            holder.songImg.setBackgroundResource(R.drawable.ic_musical_note);
        }

        holder.songName.setText(s.getSongName());
        holder.artistName.setText(s.getArtistName());

        holder.add.setBackgroundResource(R.drawable.ic_add_black_24dp);
        holder.add.setClickable(true);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPlaylist.setCurrentPlayList(songs);
                CurrentPlaylist.setIndex(position);
                MainActivity.mediaPlayerController.play();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (songs==null)
            return 0;
        return songs.size();
    }

    public class SongListHolder extends RecyclerView.ViewHolder {
        ImageView songImg;
        TextView songName;
        TextView artistName;
        LinearLayout linearLayout;
        Button add;
        Button download;
        public SongListHolder(@NonNull View v) {
            super(v);
            songImg = v.findViewById(R.id.list_item_image);
            songName = v.findViewById(R.id.list_item_songName);
            artistName = v.findViewById(R.id.list_item_artistName);
            linearLayout = v.findViewById(R.id.list_item_layout);
            add = v.findViewById(R.id.list_item_addNext);
            download = v.findViewById(R.id.list_item_download);

            add.setVisibility(View.INVISIBLE);
            download.setVisibility(View.INVISIBLE);
        }
    }
}
