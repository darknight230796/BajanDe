package com.haryanvifolks.bajando.Adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SongListRecyclerAdapter extends RecyclerView.Adapter<SongListRecyclerAdapter.SongListHolder> {
    public List<Song> songs = new ArrayList<>();
    private Context context;
    private DownloadManager downloadManager;

    public SongListRecyclerAdapter(List<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public SongListRecyclerAdapter.SongListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(MainActivity.getMainContext())
                .inflate(R.layout.list_item,parent,false);
        return new SongListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongListRecyclerAdapter.SongListHolder holder, int position) {
        final Song s = songs.get(position);
        try{final int radius = 15;
            final int margin = 10;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);

            Picasso.get().load(s.getSongPicURL())
                    .fit().transform(transformation).into(holder.songImg);
           // Picasso.get().load(s.getSongPicURL()).fit().into(holder.songImg);
        }catch (Exception e){
            holder.songImg.setBackgroundResource(R.drawable.ic_musical_note);
        }
        if (CurrentPlaylist.index==position){
            holder.songName.setText(s.getSongName());
             holder.artistName.setText(s.getArtistName());
            }

        holder.songName.setText(s.getSongName());
        holder.artistName.setText(s.getArtistName());

        holder.add.setBackgroundResource(R.drawable.ic_add_black_24dp);
        holder.add.setClickable(true);
        holder.itemView.setTag((int)position);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPlaylist.addSongInPlaylist(s);
                CurrentPlaylist.setIndex(CurrentPlaylist.currentPlayList.size() - 1);
                MediaPLayerService.playSong();
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPlaylist.addSongInPlaylist(s);
                notifyDataSetChanged();
                v.findViewById(R.id.list_item_addNext).setBackgroundResource(R.drawable.ic_done_black_24dp);
                v.findViewById(R.id.list_item_addNext).setClickable(false);
                MainActivity.addToPlaylist = s.getId();
                PlaylistAdd playlistAdd = new PlaylistAdd((Activity) context);
                playlistAdd.show();
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s.getDownload() == 0) {
                    Runnable runnable= new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.songViewModel.downloadSong(s.getId());
                            downloadManager = (DownloadManager)MainActivity.getMainContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(s.getSongURL());
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            Long reference = downloadManager.enqueue(request);
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

        }
    }
}
