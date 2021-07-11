package com.haryanvifolks.bajando.MainFragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.haryanvifolks.bajando.Adapters.ArtistListAdapter;
import com.haryanvifolks.bajando.Adapters.SongListAdaper;
import com.haryanvifolks.bajando.DatabaseClasses.Artist;
import com.haryanvifolks.bajando.DatabaseClasses.Song;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class search extends Fragment {
    EditText searchTV;
    ListView searchList;
    ListView artistList;
    Button searchCancel;
    private static Thread t;
    private List<Song> searchedSongs = new ArrayList<>();
    private List<Artist> searchedArtists = new ArrayList<>();

    public static Activity searchAct;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.search_fragment,container,false);
        searchTV = view.findViewById(R.id.search_text);
        searchList = view.findViewById(R.id.search_list);
        artistList = view.findViewById(R.id.search_list_artist);
        searchAct = (Activity) MainActivity.getMainContext();


        searchTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        searchedSongs = MainActivity.songViewModel.searchSong("%" + s + "%");
                        searchedArtists = MainActivity.songViewModel.searchArtist("%" + s + "%");
                        search.getSearch().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                searchList.setAdapter(new SongListAdaper(MainActivity.getMainContext(), R.layout.list_item
                                        , searchedSongs));
                                artistList.setAdapter(new ArtistListAdapter(MainActivity.getMainContext(),R.layout.list_item_artist,
                                        searchedArtists));

                                ListUtils.setDynamicHeight(searchList);
                                ListUtils.setDynamicHeight(artistList);
                            }
                        });
                    }
                };
                t = new Thread(r);
                t.start();
            }
        });
        return view;
    }
    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public static Activity getSearch() {
        return searchAct;
    }
}
