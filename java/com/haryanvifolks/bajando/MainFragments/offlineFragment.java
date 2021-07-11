package com.haryanvifolks.bajando.MainFragments;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haryanvifolks.bajando.Activities.PlayListActivity;
import com.haryanvifolks.bajando.Adapters.OfflineSongAdapter;
import com.haryanvifolks.bajando.Adapters.SongListRecyclerAdapter;
import com.haryanvifolks.bajando.DatabaseClasses.PlayListDatabase;
import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class offlineFragment extends Fragment {

    RecyclerView recyclerView;
    List<Song> songs;


    private static final int MY_PERMISSION_REQUEST = 123;

    public offlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        songs = new ArrayList<>();
        View view = inflater.inflate(R.layout.activity_playlist, container, false);


        recyclerView = view.findViewById(R.id.current_ListView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.getMainContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (ContextCompat.checkSelfPermission(MainActivity.getMainContext()
                , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) MainActivity.getMainContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions((Activity) MainActivity.getMainContext(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
            else
            {
                ActivityCompat.requestPermissions((Activity) MainActivity.getMainContext(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }else{
            recyclerView.setAdapter(new OfflineSongAdapter(getOfflineSong(),MainActivity.getMainContext()));
        }



        return view;
    }




    public List<Song> getOfflineSong(){
        List<Song> songs = new ArrayList<>();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor songCursor = null;
        ContentResolver contentResolver = MainActivity.getMainContext().getContentResolver();

        songCursor = contentResolver.query(songUri,null,selection,null,null);


        if (songCursor!=null && songCursor.moveToFirst()){
            do{
                String name = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String artist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String url = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                songs.add(new Song(name,artist,"","",url,"",0));

            }while (songCursor.moveToNext());

            songCursor.close();
        }

        return songs;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.getMainContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                            ==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(MainActivity.getMainContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(new OfflineSongAdapter(getOfflineSong(),MainActivity.getMainContext()));}
                }else {
                    Toast.makeText(MainActivity.getMainContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
