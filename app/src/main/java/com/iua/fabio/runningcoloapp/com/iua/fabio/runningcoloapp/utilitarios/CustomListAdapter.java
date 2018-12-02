package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades.DetailActivity;
import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades.ListActivity;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.RaceData;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class CustomListAdapter extends BaseAdapter{
    private Context cont;
    private List<RaceData> lcd;

    public CustomListAdapter(Context context) throws FileNotFoundException {
            this.cont = context;

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(this.cont.getFilesDir() + "/race_data.json");
                JSONSingleton.getInstancia().setIn(fis);
                this.lcd = JSONSingleton.getInstancia().getJsonStream();
            }catch (FileNotFoundException fnfe){
                throw new FileNotFoundException("THIS IS A FILENOTFOUND EXCEPTION");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }

    public List<RaceData> getLcd(){
        return lcd;
    }

    @Override
    public int getCount() {
        if(lcd!=null)
            return lcd.size();
        else
            return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        //uso el inflater para obtener una referencia a la view de las celdas
        if (convertView==null){
            convertView= LayoutInflater.from(cont).inflate(R.layout.activity_list_cell, parent, false);
        }

        final RaceData actualData = lcd.get(position);

            TextView racedata = convertView.findViewById(R.id.tvrace);
            racedata.setText("" + actualData.getFecha());

            Button boton = convertView.findViewById(R.id.buttondetail);
            boton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    GoToDetailActivity(actualData);
                }
            });

            Button boton2 = convertView.findViewById(R.id.buttoneliminarregistro);
            boton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONSingleton.getInstancia().deleteAObjFromJSON("/race_data.json", position, cont);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    recargarListActivity();
                }
            });
        return convertView;
    }

    public void recargarListActivity(){
        Intent intento1 = new Intent(cont, ListActivity.class);
        cont.startActivity(intento1);
    }

    public void GoToDetailActivity(RaceData raceData){
        Intent intento=new Intent(cont, DetailActivity.class);
        intento.putExtra("raceData", raceData);
        cont.startActivity(intento);
    }

}
