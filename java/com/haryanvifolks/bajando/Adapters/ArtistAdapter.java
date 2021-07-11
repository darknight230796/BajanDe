package com.haryanvifolks.bajando.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haryanvifolks.bajando.DatabaseClasses.Artist;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {
    private List<Artist> artists= new ArrayList<>();

    @NonNull
    @Override
    public ArtistAdapter.ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card,parent,false);
        return new ArtistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ArtistHolder holder, int position) {
        final Artist artist = artists.get(position);
        holder.artistName.setText(artist.getArtistName());
        try{
            Picasso.get().load(artist.getArtistImgURL()).fit().transform(new CropCircleTransformation()).into(holder.artistImg);
            //Picasso.get().load(artist.getArtistImgURL()).fit().into(holder.artistImg);
        }catch (Exception e){
            holder.artistImg.setBackgroundResource(R.drawable.ic_profile);
        }
        holder.artistImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setArtistGenre(new Artist(artist.getArtistName(),artist.getArtistImgURL(),0));
                artist.updatePopularity();
                MainActivity.songViewModel.updateArtist(artist);
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.currenList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setArtists(List<Artist> artists){
        this.artists = artists;
        notifyDataSetChanged();
    }

    public class ArtistHolder extends RecyclerView.ViewHolder {
        private ImageButton artistImg;
        private TextView artistName;
        public ArtistHolder(@NonNull View itemView) {
            super(itemView);
            artistImg= itemView.findViewById(R.id.albumPic);
            artistName = itemView.findViewById(R.id.albumName);
        }
    }
}
