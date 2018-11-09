package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades.AudioDetailActivity;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.SongData;
import java.util.List;


public class AudioCustomListAdapter extends BaseAdapter{

    private Context cont;
    private List<SongData> lcd;

    public AudioCustomListAdapter(Context context) {
        this.cont = context;
    }

    public void setLcd(List<SongData> lcdp){
        this.lcd=lcdp;
    }

    @Override
    public int getCount() {
        return lcd.size();
    }

    @Override
    public Object getItem(int position) {
        return lcd.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=LayoutInflater.from(cont).inflate(R.layout.activity_audio_list_cell, parent, false);
        }

        final SongData actualData = lcd.get(position);
        TextView nombre_song = convertView.findViewById(R.id.audiotextview1);
        nombre_song.setText(actualData.getSong_title()+"\n"+actualData.getArtist());

        Button boton=convertView.findViewById(R.id.audiodetailbutton);
        boton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                GoToAudioDetailActivity(actualData);
            }
        });
        return convertView;
    }

    public void GoToAudioDetailActivity(SongData songData){
        Intent intento=new Intent(cont, AudioDetailActivity.class);
        intento.putExtra("songData", songData);
        cont.startActivity(intento);
    }
}
