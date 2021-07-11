package com.haryanvifolks.bajando.MessageBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haryanvifolks.bajando.DatabaseClasses.PlayListDatabase;
import com.haryanvifolks.bajando.MainActivity;
import com.haryanvifolks.bajando.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PresetSelect extends Dialog{
    public Activity c;
    public ListView presetListView;

    public PresetSelect(@NonNull Activity activity) {
        super(activity);
        this.c = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.equaliser);
        presetListView = findViewById(R.id.presets_listview);

        presetListView.setAdapter(new PlayListAdapter(MainActivity.getMainContext(),R.layout.presets_list_item,MainActivity.musicStyles));

    }

    public class PlayListAdapter extends ArrayAdapter<String>{

        TextView presetName;
        Context context;
        int resource;
        List<String> presets;

        public PlayListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.presets = objects;
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v==null){
                LayoutInflater vi = LayoutInflater.from(context);
                v=vi.inflate(resource,null);
            }
            final String preset = presets.get(position);
            if (preset!=null){
                presetName =v.findViewById(R.id.preset_name);
                presetName.setText(preset);
                presetName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.equalizer.setEnabled(true);
                       MainActivity.equalizer.usePreset((short) position);
                        Toast.makeText(MainActivity.getMainContext(),"Set To:"+
                                MainActivity.equalizer.getPresetName(MainActivity.equalizer.getCurrentPreset())
                                , Toast.LENGTH_SHORT).show();
                       dismiss();
                    }
                });
            }
            return v;
        }
    }
}
