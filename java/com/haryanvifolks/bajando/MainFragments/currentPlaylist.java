package com.haryanvifolks.bajando.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.haryanvifolks.bajando.Adapters.SongListAdaper;
import com.haryanvifolks.bajando.Adapters.SongListRecyclerAdapter;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class currentPlaylist extends Fragment {

    Button playButton;
    RecyclerView listView;
    ImageView imageView;
    List<Song> customSongs = new ArrayList<>();
    public int i;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_playlist_fragment,container,
                false);

        imageView = view.findViewById(R.id.mainImage);
        listView = view.findViewById(R.id.randomList);
        playButton = view.findViewById(R.id.playArtistPlayList);


                if (MainActivity.getArtistGenre()!=null){
                for ( i = 0; i<MainActivity.songsForArtist.size(); i++){

                    if (MainActivity.songsForArtist.get(i).getArtistName().
                            contains(MainActivity.getArtistGenre().getArtistName())
                            || MainActivity.songsForArtist.get(i).getGenreName().
                            contains(MainActivity.getArtistGenre().getArtistName()))
                    {
                        customSongs.add(MainActivity.songsForArtist.get(i));
                    }

                }}
                else
                    customSongs = CurrentPlaylist.currentPlayList;
        listView.setLayoutManager(new LinearLayoutManager(MainActivity.getMainContext(),LinearLayoutManager.VERTICAL,false));
        listView.setHasFixedSize(true);
                listView.setAdapter(new SongListRecyclerAdapter(customSongs,MainActivity.getMainContext()));

        try{

            final int radius = 25;
            final int margin = 10;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);

            Picasso.get().load(MainActivity.getArtistGenre().getArtistImgURL())
                    .fit().transform(transformation).into(imageView);

            //Picasso.get().load(MainActivity.getArtistGenre().getArtistImgURL()).fit()
              //      .into(imageView);
        }catch (Exception e){
            imageView.setBackgroundResource(R.drawable.ic_profile);
        }
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPlaylist.setCurrentPlayList(customSongs);
                CurrentPlaylist.setIndex(0);
                MediaPLayerService.playSong();
            }
        });


        return view;
    }
}
