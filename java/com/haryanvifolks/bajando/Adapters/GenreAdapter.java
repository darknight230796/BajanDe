package com.haryanvifolks.bajando.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.haryanvifolks.bajando.DatabaseClasses.Artist;
import com.haryanvifolks.bajando.DatabaseClasses.GenreFormat;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {
    private List<GenreFormat> genres = new ArrayList<>();

    public GenreAdapter() {
        String json = null;
        try {
            InputStream is = MainActivity.getMainContext().getAssets().open("genre.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.e("ErrorInRead", e.toString());
        }

        try {
            JSONArray JA = new JSONArray(json);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                genres.add(new GenreFormat(JO.getString("genreName"),JO.getString("genreImgURL")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GenreAdapter(List<Artist> artists){
        for(int i =0; i<artists.size();i++){
            this.genres.add(new GenreFormat(artists.get(i).getArtistName(),artists.get(i).getArtistImgURL()));
        }
    }

    public void setArtists(List<Artist> artists){

        for(int i =0; i<artists.size();i++){
            try{
                this.genres.add(new GenreFormat(artists.get(i).getArtistName(),artists.get(i).getArtistImgURL()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreAdapter.GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_card,parent,false);
        return new GenreHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.GenreHolder holder, int position) {
        final GenreFormat g = genres.get(position);
        holder.genreName.setText(g.getGenreName());
        try{
            final int radius = 25;
            final int margin = 10;

            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.get().load(g.getGenreUrl()).fit().transform(transformation).into(holder.genrePic);
            //Picasso.get().load(g.getGenreUrl()).fit().into(holder.genrePic);
        }catch (Exception e){
            holder.genrePic.setBackgroundResource(R.drawable.ic_musical_note);
            }
            holder.genrePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.setArtistGenre(new Artist(g.getGenreName(),g.getGenreUrl(),0));
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.currenList);
                }
            });
    }

    @Override
    public int getItemCount() {
if (genres==null)
    return 0;
        return genres.size();
    }

    public class GenreHolder extends RecyclerView.ViewHolder {
        private ImageButton genrePic;
        private TextView genreName;
        public GenreHolder(@NonNull View itemView) {
            super(itemView);
            genrePic = itemView.findViewById(R.id.genrePic);
            genreName = itemView.findViewById(R.id.genreName);
        }
    }
}
