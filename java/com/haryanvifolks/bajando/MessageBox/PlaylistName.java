package com.haryanvifolks.bajando.MessageBox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.haryanvifolks.bajando.DatabaseClasses.PlayListDatabase;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import androidx.annotation.NonNull;

public class PlaylistName extends Dialog{
    public Context context;
    public Button doneButton,cancel;
    public EditText playlistNameTxt;
    public PlaylistName(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.playlist_msg_name);

        doneButton = findViewById(R.id.done_button);
        cancel = findViewById(R.id.cancel_button);
        playlistNameTxt = findViewById(R.id.new_playlist_name);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.songViewModel.insertPlaylist(new PlayListDatabase(playlistNameTxt.getText().toString(),String.valueOf(MainActivity.addToPlaylist)));
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
