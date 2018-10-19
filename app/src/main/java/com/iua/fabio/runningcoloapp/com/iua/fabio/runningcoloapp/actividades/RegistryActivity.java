package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.iua.fabio.runningcoloapp.R;

//public class RegistryActivity extends FragmentActivity {
public class RegistryActivity extends AppCompatActivity implements RunningFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        setTitle(R.string.titulo_registry_activity);

        RunningFragment rf=new RunningFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedor, rf);

        Button boton_start=findViewById(R.id.button2);
        boton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRunningFragment(v);
            }
        });

        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListActivity(v);
            }
        });
    }

    private void goToRunningFragment(View view){
        RunningFragment rf1=new RunningFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, rf1).commit();
    }

    private void goToListActivity(View v){
        Intent i=new Intent(this, ListActivity.class);
        startActivity(i);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}

