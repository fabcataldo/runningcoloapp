package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iua.fabio.runningcoloapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button boton_start=findViewById(R.id.button1);
        boton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistryActivity(v);
            }
        });
    }

    private void goToRegistryActivity(View view){
        Intent i = new Intent(this, RegistryActivity.class);
        startActivity(i);
    }
}

