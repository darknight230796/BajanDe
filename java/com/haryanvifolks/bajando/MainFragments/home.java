package com.haryanvifolks.bajando.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haryanvifolks.bajando.Adapters.CategoryAdapterRecycler;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

public class home extends Fragment {

    RecyclerView mainList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);

        mainList = view.findViewById(R.id.mainList);

        mainList.setLayoutManager(new LinearLayoutManager(MainActivity.getMainContext(),LinearLayoutManager.VERTICAL,false));
        mainList.setHasFixedSize(true);

        mainList.setAdapter(MainActivity.categoryAdapterRecycler);

        return view;
    }
}
