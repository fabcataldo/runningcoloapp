package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.CoordData;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.JSONSingleton;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RunningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RunningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunningFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Chronometer cronometro;
    private boolean isCorriendo;
    private long pauseOffset;
    private View actualV;
    private OnFragmentInteractionListener mListener;

    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedProviderLocationClient;
    private ArrayList<Location> raceBestLocations;
    private boolean mRequestingLocationUpdates;
    private Location firstLocation;
    private Location lastLocation;

    private double ritmo;
    private long ritmelapsedMillis;
    private double ritmelapsedSeconds;
    private double ritmelapsedMinutes;
    private double distancia;
    private double ritmoPromedio;
    private long ritmodistancia;
    private String fecha;
    private double ritmoVelocProm;
    private double[][] coordsarray;

    public RunningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RunningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RunningFragment newInstance(String param1, String param2) {
        RunningFragment fragment = new RunningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        actualV=inflater.inflate(R.layout.fragment_running, container, false);

        cronometro=actualV.findViewById(R.id.cronometro);
        cronometro.setFormat("%s");
        cronometro.setBase(SystemClock.elapsedRealtime());

        raceBestLocations = new ArrayList<Location>();
        mFusedProviderLocationClient=LocationServices.getFusedLocationProviderClient(this.getActivity());
        createLocationRequest();

        final TextView txtvritmo = actualV.findViewById(R.id.textvritmo);

        final MaterialButton botons=actualV.findViewById(R.id.bstartcron);
        botons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCorriendo){
                    botons.setText("Pausar");
                    startLocationUpdates();
                    comenzar(v);
                }
                else{
                    botons.setText("Continuar");
                    stopLocationUpdates();
                    pausar(v);
                }
            }
        });

        MaterialButton botonstop=actualV.findViewById(R.id.bstopcron);
        botonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                detener(v);
            }
        });

        mLocationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult location) {
                if (location != null) {
                    if(firstLocation==null) {
                        setFirstLocation(location.getLastLocation());
                        addElementRaceBestLocations(firstLocation);
                    }
                    if(firstLocation.getLatitude() != location.getLastLocation().getLatitude()
                            && firstLocation.getLongitude() != location.getLastLocation().getLongitude()) {
                        setLastLocation(location.getLastLocation());
                    }
                }
                else{
                    return;
                }

                if(lastLocation!=null) {
                    if (isBetterLocation(lastLocation, firstLocation) && Math.abs(firstLocation.getBearing() - lastLocation.getBearing()) >= 45) {
                        addElementRaceBestLocations(lastLocation);
                    }

                    ritmelapsedMillis = SystemClock.elapsedRealtime() - cronometro.getBase();
                    ritmelapsedSeconds = (ritmelapsedMillis*0.001) /1;
                    ritmelapsedMinutes =Math.round((ritmelapsedSeconds*1) /60 * 100d) / 100d;

                    //ya al tener dos puntos, calculo la veloc prom
                    //entre esos dos puntos
                    ritmodistancia+=measure(firstLocation,lastLocation);
                    ritmoVelocProm=ritmodistancia/ritmelapsedMinutes;

                    //y luego, el ritmo en ese momento va a ser
                    //la distancia fija que yo pongo (1000 m o sea 1km)
                    // dividida la veloc prom (variable)
                    ritmo=Math.round((1000/ritmoVelocProm) * 100d) / 100d;
                    String sritmo=String.format("%.2f", ritmo);
                    txtvritmo.setText("Ritmo: "+sritmo+" minutos cada 1 km");

                    setFirstLocation(null);
                    setLastLocation(null);
                }
            }
        };
        
        //hago esto (de llamar al startLoc... ACÁ, ya que, he notado, en las pruebas que hice
        //que los Locations no los agarraba, luego, cuando iba a ver los detalles de
        //la carrera que grabé, se clavaba la app ya que no se habia grabado ningun punto
        startLocationUpdates();

        return actualV;
    }

    public void comenzar(View v){
        if(!isCorriendo){
            cronometro.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            cronometro.start();
            isCorriendo=true;
        }
    }

    public void detener(View v){
        if(isCorriendo){
            cronometro.stop();
            pauseOffset=0;
            isCorriendo=false;
        }
        saveCoords();
        if(coordsarray.length<2){
            Intent intento = new Intent(this.getContext(), CoordsEmptyError.class);
            startActivity(intento);
            return;
        }
        else{
            grabarDatos();
            irAFbShareRaceActivity(distancia, ritmoPromedio, fecha);
        }
    }

    //Devuelve la distancia recorrida entre dos puntos
    //utilizando la fórmula de Havershine
    private double measure(Location l1, Location l2) {
        //La fórmula supone que la tierra es perfectamente redonda
        //como no estamos en un plano, no se puede utilizar el método de Pitágoras
        //Radio equivolumen de la Tierra, está en km, al final, después de calcular todo
        //paso todo a metros
        double R = 6378.137;

        //Calculo la diferencia de latitud, y la de longitud
        // entre los dos puntos, pasando cada latitud y longitud a radianes, previamente
        //ya que la latitud y la longitud están en grados,minutos,segundos
        double dLat = l1.getLatitude() * Math.PI / 180 - l1.getLatitude() * Math.PI / 180;
        double dLon = l2.getLongitude() * Math.PI / 180 - l1.getLongitude() * Math.PI / 180;

        //la función de havershine: ( havershine(d/R) ) = h
        //siendo d, la distancia entre los dos puntos
        //R es el radio que estamos utilizando, el de la Tierra
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(l1.getLatitude() * Math.PI / 180) * Math.cos(l2.getLatitude() * Math.PI / 180) *
                        Math.pow(Math.sin(dLon / 2), 2);

        //luego, en la variable c para sacar la distancia final
        //fórmula de havershine
        //double c = 2 * R *Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        //corrijo el cálculo de la distancia, debajo ya que, segun Wikipedia:
        //"a veces, la fórmula DE ABAJO se escribe en términos de la función arcotangente
        //(como está comentado arriba) pero esta adolece de problemas numéricos similares
        //con valores cerca de h = 1"
        double c = 2 * R * Math.asin(Math.sqrt(a));

        //paso la distancia obtenida a metros
        return c * 1000;
    }

    private void saveCoords(){
        coordsarray=new double[raceBestLocations.size()][2];

        CoordData coords;
        distancia=0;
        for(int i =0;i<raceBestLocations.size();i++){
            coords=new CoordData(raceBestLocations.get(i).getLatitude(), raceBestLocations.get(i).getLongitude());
            coordsarray[i]=coords.getCoordsArray();
            if((i+1) != raceBestLocations.size()){
                distancia+=Math.round(measure(raceBestLocations.get(i),raceBestLocations.get(i+1)) * 100d / 100d);
            }
        }
    }

    private void grabarDatos(){
        long elapsedMillis = SystemClock.elapsedRealtime() - cronometro.getBase();
        double elapsedSeconds = (elapsedMillis*0.001) /1;
        double elapsedMinutes =Math.round((elapsedSeconds*1) /60 * 100d) / 100d;

        //el ritmo promedio va a ser los metros que recorri / los segundos que hice en toda la race
        //redondeo el cálculo final con dos decimales
        //por eso la multiplicación, la división, y el 100d
        //PASO LA DISTANCIA A KM, para luego hacer el ritmo prom: km/min
        ritmoPromedio=Math.round((distancia/1000)/elapsedMinutes * 100d) / 100d;

        Calendar c2 = new GregorianCalendar();
        String dia = Integer.toString(c2.get(Calendar.DATE));
        String mes = Integer.toString(c2.get(Calendar.MONTH)+1);
        String anio = Integer.toString(c2.get(Calendar.YEAR));
        Date date = c2.getTime();
        String hours = ""+ date.getHours();
        String minutes = ""+date.getMinutes();
        String pseudominutes="";
        if(date.getMinutes()<10){
            pseudominutes="0"+minutes;
        }
        else{
            pseudominutes=minutes;
        }
        fecha=dia+"/"+mes+"/"+anio+" "+hours+":"+pseudominutes;


        try {
            String pathJson="/race_data.json";
            JSONSingleton.getInstancia().writeToJSON(pathJson,fecha, coordsarray, elapsedMinutes, ritmoPromedio, distancia, this.getContext(), this.getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void irAFbShareRaceActivity(double distancia, double ritmoPromedio, String fecha){
        Intent intento=new Intent(this.getContext(), FbShareRaceActivity.class);
        intento.putExtra("distancia", distancia);
        intento.putExtra("ritmopromedio", ritmoPromedio);
        intento.putExtra("fecha", fecha);
        startActivity(intento);
    }

    public void pausar(View v){
        if(isCorriendo){
            cronometro.stop();
            pauseOffset=SystemClock.elapsedRealtime()-cronometro.getBase();
            isCorriendo=false;
        }
    }


    public void setFirstLocation(Location fl){
        firstLocation=fl;
    }

    public void setLastLocation(Location ll){
        lastLocation=ll;
    }

    public void addElementRaceBestLocations(Location element){
        raceBestLocations.add(element);
    }

    //private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    //Primero pregunta si sólo se está enviando el argumento "location"
    //entonces, el nuevo punto del gps es el mejor
    //si no, comprueba qué tan nuevo es la nueva localización con respecto a la
    //localización que queremos comparar
    //si la nueva localización es nueva, devuelve true, si no, false
    //utiliza también la precisión de las dos localizaciones, para ver cuál de las dos es mejor
    //tiene en cuenta la precisión, y si vienen del mismo provider (wifi o datos, o gps)
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > MINUTES;
        boolean isSignificantlyOlder = timeDelta < -MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;

            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mRequestingLocationUpdates=true;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this.getActivity(), new
                    String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},22);
            return;
        }
        mFusedProviderLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void stopLocationUpdates(){
        mFusedProviderLocationClient.removeLocationUpdates(mLocationCallback);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
}
