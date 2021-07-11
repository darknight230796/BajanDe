package com.haryanvifolks.bajando.MainFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.haryanvifolks.bajando.Activities.PlayListActivity;
import com.haryanvifolks.bajando.DatabaseClasses.PlayListDatabase;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class stationFragment extends Fragment {



    public stationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist,container,false);
        ListView listView = view.findViewById(R.id.playlist_name_listView);
        listView.setAdapter(new PlayListAdapter1(MainActivity.getMainContext(),R.layout.playlist_list_item,MainActivity.playLists));
        return view;
    }

    public class PlayListAdapter1 extends ArrayAdapter<PlayListDatabase> {

        TextView playListName;
        Context context;
        int resource;
        List<PlayListDatabase> playlists;

        public PlayListAdapter1(@NonNull Context context, int resource, @NonNull List<PlayListDatabase> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.playlists = objects;
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v==null){
                LayoutInflater vi = LayoutInflater.from(context);
                v=vi.inflate(resource,null);
            }
            final PlayListDatabase playlist = playlists.get(position);
            if (playlist!=null){
                playListName=v.findViewById(R.id.playlist_name);
                playListName.setText(playlist.getPlaylistname());
                playListName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.getMainContext(),PlayListActivity.class);
                        intent.putExtra(MainActivity.PLAYLIST_ID,position+1);
                        startActivity(intent);
                    }
                });
            }
            return v;
        }
    }
}
