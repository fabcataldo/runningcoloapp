package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import com.iua.fabio.runningcoloapp.R;

//public class RegistryActivity extends FragmentActivity {
public class RegistryActivity extends AppCompatActivity implements RunningFragment.OnFragmentInteractionListener, AudioListFragment.OnFragmentInteractionListener {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        setTitle(R.string.titulo_registry_activity);

        toolbar=findViewById(R.id.toolbar);

        //Este método establece la barra de herramientas como la barra de app de la actividad.
        setSupportActionBar(toolbar);

        //llamo al método getSupportBar() para utilizar sus métodos
        //y poder customizar mi barra
        ActionBar ab=getSupportActionBar();

        //lo de abajo es para habilitar (o no) la flechita de volver atrás, en el toolbar
        ab.setDisplayHomeAsUpEnabled(false);

        //el color del titulo de la toolbar
        setTitleColor(Color.WHITE);

        RunningFragment rf = new RunningFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedor, rf);

        AudioListFragment alf = new AudioListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.audiocontenedor, alf);

        //PARA DESAPARECER el botoncito de GO, o empezar
        //y mostrar los fragments
        final Button boton_start = findViewById(R.id.button2);
        boton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boton_start.setVisibility(v.GONE);
                goToRunningFragment(v);
                goToAudioListFragment(v);
            }
        });
    }

    //inflo el layout del menu, para la toolbar
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //dependiendo del botoncito que presione, del menú
    //es la acción que se ve va a ejecutar
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.lista_de_carreras:
                goToListActivity();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void goToRunningFragment(View view){
        RunningFragment rf1=new RunningFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, rf1).commit();
    }

    private void goToAudioListFragment(View view){
        AudioListFragment alf = new AudioListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.audiocontenedor, alf).commit();
    }

    private void goToListActivity(){
        Intent i=new Intent(this, ListActivity.class);
        startActivity(i);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}

