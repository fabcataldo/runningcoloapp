package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades.RegistryActivity;

public class FbShareRaceActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_share_race);
        Button bsi=findViewById(R.id.boton_yes_fb_share_race_act);
        Button bno=findViewById(R.id.boton_no_fb_share_race_act);

        bsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartirEnFacebook((double) getIntent().getDoubleExtra("distancia", 0),
                        (double) getIntent().getDoubleExtra("ritmopromedio", 0),
                        (String) getIntent().getStringExtra("fecha"));
            }
        });

        bno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverARegistryActivity();
            }
        });
    }


    private void volverARegistryActivity(){
        Intent intento=new Intent(this, RegistryActivity.class);
        startActivity(intento);
    }

    private void compartirEnFacebook(double distancia, double ritmoPromedio, String fecha) {
        String race = "Hoy hice nueva carrera!!" +
                "\nFecha y hora de inicio de la carrera: " + fecha +
                "\nRitmo promedio: " + ritmoPromedio + " minutos cada 1 km" +
                "\nDistancia recorrida: " + distancia + " metros" +
                "\n\nMúsica motivacional por default, provista por la aplicación, " +
                "debajo se encuentra un link de Youtube";
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.youtube.com/watch?v=IcrbM1l_BoI"))
                    .setQuote(race)
                    .build();
            shareDialog.show(linkContent);
        }
    }
}
