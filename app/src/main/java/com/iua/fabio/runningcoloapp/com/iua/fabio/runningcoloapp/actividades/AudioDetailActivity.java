package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.SongData;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.MPSingleton;

public class AudioDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);
        setTitle(R.string.titulo_audio_detail_activity);

        SongData actualData = (SongData)getIntent().getSerializableExtra("songData");
        SongData nextActualData=(SongData)getIntent().getSerializableExtra("nextActualData");

        System.out.println("ACTUAL DATA: "+actualData);
        System.out.println("nextActualda: "+nextActualData);

        final Uri resSongID = Uri.parse(actualData.getSong_location());
        final Uri nextResSongID = Uri.parse(nextActualData.getSong_location());

        MPSingleton.getInstance().prepareMedia(this,resSongID);

        TextView txtv1=findViewById(R.id.audioname);
        txtv1.setText("Nombre de la canción: "+actualData.getSong_title()+
        "\n"+"Album: "+actualData.getAlbum()+"\n"+"Artista: "+actualData.getArtist());

        Button boton1 = findViewById(R.id.audioplaybutton);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MPSingleton.getInstance().play();
            }
        });

        Button boton2 = findViewById(R.id.audiopausebutton);
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MPSingleton.getInstance().pauseSong();
            }
        });

        Button boton3 = findViewById(R.id.audiostopbutton);
        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MPSingleton.getInstance().stopSong();
            }
        });

        MPSingleton.getInstance().get_mp().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MPSingleton.getInstance().stopSong();
                MPSingleton.getInstance().prepareMedia(getApplicationContext(), nextResSongID);
                MPSingleton.getInstance().play();
            }
        });
    }
}
