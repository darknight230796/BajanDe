package com.haryanvifolks.bajando.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapterRecycler extends RecyclerView.Adapter<CategoryAdapterRecycler.CategoryHolderRecycler> {


    private String[] categories={"Personal Favourites","Browse Artists","Weekly Top 15","Top Artists","New Releases","Top Genres"};


    @NonNull
    @Override
    public CategoryAdapterRecycler.CategoryHolderRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_list_item, parent, false);
        return new CategoryHolderRecycler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapterRecycler.CategoryHolderRecycler holder, int position) {
            holder.categoryName.setText(categories[position]);
            holder.list.setLayoutManager(new LinearLayoutManager(MainActivity.getMainContext(),LinearLayoutManager.HORIZONTAL,false));
            holder.list.setHasFixedSize(true);
            holder.list.setAdapter(adapter(position));

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
                return new GenreAdapter(MainActivity.artists);
            case 4:
                return MainActivity.allSongsAdapter;
            case 5:
                return new GenreAdapter();
        }
        return MainActivity.personalTopAdapter;
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class CategoryHolderRecycler extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private RecyclerView list;


        public CategoryHolderRecycler(@NonNull View itemView) {
            super(itemView);


                categoryName = itemView.findViewById(R.id.mainItemName);
                list = itemView.findViewById(R.id.mainRecyclerView);




        }
    }


}
