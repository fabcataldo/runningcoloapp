package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades.RegistryActivity;

public class FileNotFoundFromListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_not_found_from_list);
        setTitle(R.string.titulo_file_not_found_from_list_activity);

        Button boton = findViewById(R.id.boton_volver_a_registry_activity);
        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goToRegistryActivity();
           }
        });
    }

    private void goToRegistryActivity(){
        Intent i=new Intent(this, RegistryActivity.class);
        startActivity(i);
    }
}
