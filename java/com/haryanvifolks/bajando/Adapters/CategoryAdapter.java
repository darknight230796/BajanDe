package com.haryanvifolks.bajando.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends ArrayAdapter {

    private Activity context;
    private String[] categories={"Personal Favourites","Browse Artists","Weekly Top 15","Top Artists","New Releases","Top Genres"};

    public CategoryAdapter(Activity context, String[] categories) {
        super(context, R.layout.main_list_item,categories);
        this.context = context;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.main_list_item,null,true);
        TextView txt = rowView.findViewById(R.id.mainItemName);
        RecyclerView recyclerView = rowView.findViewById(R.id.mainRecyclerView);
        txt.setText(categories[position]);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.getMainContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter(position));
        return rowView;
    }

    private RecyclerView.Adapter adapter(int position){
        switch (position){
            case 0:
                return MainActivity.personalTopAdapter;
            case 1:
                return MainActivity.allArtists;
            case 2:
                return MainActivity.trendingAdapter;
            case 3:
                return MainActivity.popularArtist;
            case 4:
                return MainActivity.allSongsAdapter;
            case 5:
                return new GenreAdapter();
        }
        return MainActivity.personalTopAdapter;
    }
}