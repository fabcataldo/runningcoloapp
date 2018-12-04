package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.CustomListAdapter;

import java.io.FileNotFoundException;

public class ListActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(R.string.titulo_list_activity);

        ListView lista=findViewById(R.id.listraces);
        CustomListAdapter cla=null;
        try {
             cla = new CustomListAdapter(this);
             if(cla.getLcd().size()==0){
                 GoToFileNotFoundFromListActivity();
             }
        } catch (FileNotFoundException fnfe){
            GoToFileNotFoundFromListActivity();
        }
        lista.setAdapter(cla);
    }

    private void GoToFileNotFoundFromListActivity(){
        Intent intento=new Intent(this, FileNotFoundFromListActivity.class);
        startActivity(intento);
    }
}
