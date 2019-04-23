package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.button.MaterialButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.RaceData;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.CustomListAdapter;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.JSONSingleton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    //private boolean isRaceListEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            makeAndShowNotif();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();

        //puede pasar la excepción de abajo, cuando haya eliminado previamente todos los registros
        //del archivo de carreras
        } catch (ArrayIndexOutOfBoundsException aioobe){
            goToRegistryActivity();
        }

        MaterialButton boton_start=findViewById(R.id.button1);
        //Button boton_start=findViewById(R.id.button1);
        boton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistryActivity();
            }
        });
    }

    private void goToRegistryActivity(){
        Intent i = new Intent(this, RegistryActivity.class);
        startActivity(i);
    }

    private void makeAndShowNotif() throws IOException, ParseException, ArrayIndexOutOfBoundsException {
        int diasquehanpasado=knowAboutLastRace();

        //EL IF DE ABAJO SE PUEDE CAMBIAR
        if(diasquehanpasado > 1){
            // Las tres líneas de abajo es para cuando el usuario presione sobre la
            //notificación, que va ya a la actividad correspondiente
            //que pongo como parámetro en el objeto Intent
            Intent intent = new Intent(this, RegistryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            //El pendingIntent, nos dice que, va a estar en pendiente la ejecución de la
            // actividad "RegistryActivity", y no va a correr hasta que no reaccionemos
            // con "un toque" la notificación,
            // tengo que guardar en algún lugar la acción a tomar, una vez que reaccione a la
            //notificación, y a donde la guardo? en el objeto pendingIntent
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            //creo la notificación, de forma básica
            //le pongo cualquier channelId ya que, para versiones de Android
            //menores a 8.0, este parámetro se ignora
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.drawable.common_full_open_on_phone)
                    .setContentTitle("Ojo!!! andá a correr YA")
                    .setContentText("Han pasado "+diasquehanpasado+" días desde tu última carrera." +
                            " Te aconsejo que retomes el entrenamiento :)!.")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Han pasado "+diasquehanpasado+" días desde tu última carrera." +
                                    " Te recomiendo que retomes el entrenamiento :)!."))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                    // le digo que se ejecute la actividad en el objeto PendingIntent
                    // cuando el usuario presione en la notificación
                    .setContentIntent(pendingIntent)

                    //permito que el usuario deslize hacia afuera la notificación, y que
                    //ésta no quede anclada
                    .setAutoCancel(true);

            //creo un objeto NotificationManager Compat para mostrar la notificación
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            //el id de la notificación sirve para cuando, en un futuro,
            //yo pueda actualizar o remover la notificación
            notificationManager.notify(101, mBuilder.build());

        }

    }
    private int knowAboutLastRace() throws IOException, ParseException {
        FileInputStream fis=new FileInputStream(this.getApplicationContext().getFilesDir()+"/race_data.json");
        JSONSingleton.getInstancia().setIn(fis);
        JSONSingleton.getInstancia().setRaceDataList(JSONSingleton.getInstancia().getJsonStream());
        List<RaceData> listRaces = JSONSingleton.getInstancia().getRaceDataList();
        
        String lastDateRace = listRaces.get(listRaces.size() - 1).getFecha();

        //uso el objeto "formato" para usar el parser, que te pasa la fecha en String
        //a un objeto de tipo Date, que necesitamos para restar la fecha actual
        //con la fecha de la última carrera (almacenado en lastDateRace)
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = formato.parse(lastDateRace);

        return knowDifferenceBetwLastRaceAndNow(fechaDate,  new Date());
    }

    private int knowDifferenceBetwLastRaceAndNow(Date fechaInicial, Date fechaFinal){
        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();

        //devuelve la diferencia en milisegundos
        long diferencia = fechaFinalMs - fechaInicialMs;

        //paso el resultado anterior de milisegundos a segundos, luego a minutos,
        //luego a hora, y luego a días
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));

        return ( (int) dias);
    }
}

