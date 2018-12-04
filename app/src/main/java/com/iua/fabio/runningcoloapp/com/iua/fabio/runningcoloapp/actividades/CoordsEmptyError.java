package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades.RegistryActivity;

public class CoordsEmptyError extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coords_empty_error);

        Button boton = findViewById(R.id.boton_volver_activity_coords_empty_error);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityRegistro();
            }
        });
    }

    private void goToActivityRegistro(){
        Intent intento = new Intent(this, RegistryActivity.class);
        startActivity(intento);
    }
}
