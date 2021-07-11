package com.haryanvifolks.bajando.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haryanvifolks.bajando.DatabaseClasses.Artist;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ArtistListAdapter extends ArrayAdapter<Artist> {
    private Context context;
    private int resource;
    private List<Artist> artists = new ArrayList<>();

    public ArtistListAdapter(@NonNull Context context, int resource, @NonNull List<Artist> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.artists = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v==null){
            LayoutInflater vi = LayoutInflater.from(context);
            v=vi.inflate(resource,null);
        }

        final Artist artist = artists.get(position);
        if (artist!=null){
            ImageView imageView = v.findViewById(R.id.list_item_artist_image);
            TextView artistName = v.findViewById(R.id.list_item_artist_name);
            LinearLayout layout = v.findViewById(R.id.list_item_artist_layout);

            try{
                Picasso.get().load(artist.getArtistImgURL()).fit().into(imageView);
            }catch (Exception e){
                imageView.setBackgroundResource(R.drawable.ic_profile);
            }

            artistName.setText(artist.getArtistName());


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.setArtistGenre(new Artist(artist.getArtistName(),artist.getArtistImgURL(),0));
                    artist.updatePopularity();
                    MainActivity.songViewModel.updateArtist(artist);
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.currenList);
                }
            });

        }
        return v;
    }
}
