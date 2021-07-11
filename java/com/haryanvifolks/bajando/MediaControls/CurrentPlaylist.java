package com.haryanvifolks.bajando.MediaControls;


import com.haryanvifolks.bajando.DatabaseClasses.Song;

import java.util.ArrayList;
import java.util.List;

public class CurrentPlaylist {
    public static List<Song> currentPlayList = new ArrayList<>();
    public static int index;

    public static List<Song> getCurrentPlayList() {
        return currentPlayList;
    }

    public static void setCurrentPlayList(List<Song> currentPlayList) {
        CurrentPlaylist.currentPlayList = currentPlayList;
    }

    public static int getIndex() {

        return index;
    }

    public CurrentPlaylist(List<Song> currentPlayList, int index) {
        CurrentPlaylist.currentPlayList = currentPlayList;
        CurrentPlaylist.index = index;
    }

    public static void setIndex(int index) {

        CurrentPlaylist.index = index;
    }

    void nextIndex() {
        if (index < currentPlayList.size() - 1)
            index += 1;
        else
            index = 0;
    }

    void previousIndex() {
        if (index == 0)
            index = currentPlayList.size() - 1;
        else
            index -= 1;
    }

    public static void addSongInPlaylist(Song song) {
        currentPlayList.add(song);
    }

}
