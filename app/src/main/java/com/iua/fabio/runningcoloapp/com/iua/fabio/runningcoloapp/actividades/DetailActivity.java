package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.RaceData;

public class DetailActivity extends AppCompatActivity implements RunningFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(R.string.titulo_detail_activity);

        final RaceData actualData = (RaceData)getIntent().getSerializableExtra("raceData");

        TextView txtv=findViewById(R.id.tvfecha);
        txtv.setText("Fecha y hora de inicio de la carrera: "+actualData.getFecha());

        TextView txtv2=findViewById(R.id.tvduracion);
        txtv2.setText("Tiempo realizado: "+actualData.getDuracion()+" minutos");

        TextView txtv3=findViewById(R.id.tvdistancia);
        txtv3.setText("Distancia recorrida: "+actualData.getDistancia()+" metros");

        TextView txtv4=findViewById(R.id.tvritmo);
        txtv4.setText("Ritmo promedio: "+actualData.getRitmo()+" minutos cada 1 km");

        Button boton1 = findViewById(R.id.buttonmaprun);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailMapRunActivity(v, actualData);
            }
        });

    }


    private void goToDetailMapRunActivity(View v, RaceData rc){
/*
        Intent i=new Intent(this, DetailMapRunActivity.class);
        i.putExtra("raceDataCoords", rc.getCoords());
        startActivity(i);
*/
        DetailMapRunFragment dmra=new DetailMapRunFragment();
        Bundle args=new Bundle();
        args.putSerializable("raceDataCoords", rc.getCoords());
        dmra.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedormapa, dmra).commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
