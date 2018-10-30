package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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
    private boolean señalLocatReq;

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


        Button botons=actualV.findViewById(R.id.bstartcron);
        botons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comenzar(v);
            }
        });

        Button botonstop=actualV.findViewById(R.id.bstopcron);
        botonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                detener(v);
            }
        });

        Button botonpause=actualV.findViewById(R.id.bpausecron);
        botonpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                pausar(v);
            }
        });

        Button botonres=actualV.findViewById(R.id.bresetcron);
        botonres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                resetear(v);
                startLocationUpdates();
            }
        });

        //final TextView v = findViewById(R.id.tvprueba);
        mLocationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult location) {
                if (location != null) {
                    if(firstLocation==null) {
                        setFirstLocation(location.getLastLocation());
                    }
                    if(firstLocation.getLatitude() != location.getLastLocation().getLatitude()
                            && firstLocation.getLongitude() != location.getLastLocation().getLongitude()) {
                        setLastLocation(location.getLastLocation());
                    }
                    System.out.println("FIRST LOCATION: "+firstLocation.getLongitude()+", "+firstLocation.getLatitude());
                    if(lastLocation!=null)
                        System.out.println("LAST LOCATION: "+lastLocation.getLongitude()+", "+lastLocation.getLatitude());

                    /*
                    for (int i = 0; i < location.getLocations().size(); i++) {
                        locationsGetted.add(location.getLocations().get(i));
                    }
                    */
                    //lastLocation=location.getLocations().get(locationsGetted.size()-1);

                    //System.out.println("DEGREEES OF LAST AND FIRST LOCATION: "+bearing(firstLocation.getLatitude(),firstLocation.getLongitude(),lastLocation.getLatitude(),lastLocation.getLongitude()));
                }
                else{
                    return;
                }

                /*
                for(int i=0;i<locationsGetted.size();i++){
                    System.out.println("LOCATIONS GETTED: "+locationsGetted.get(i).getLongitude()+", "+locationsGetted.get(i).getLatitude());
                    System.out.println("LOCATIONS GETTED DEGREES: "+bearing(locationsGetted.get(i).getLatitude(),locationsGetted.get(i).getLongitude(),firstLocation.getLatitude(),firstLocation.getLongitude()));
                }
                */
                /*
                for (int i = 0; i < locationsGetted.size()-1; i++) {
                    //if(firstLocation.getBearing() - locationsGetted.get(i).getBearing() > 45){
                    //if(isBetterLocation(firstLocation, locationsGetted.get(i))){
                    //if(isBetterLocation(location.getLastLocation(), firstLocation)){
                    if(isBetterLocation(location.getLastLocation(), firstLocation) && bearing(locationsGetted.get(i).getLatitude(),locationsGetted.get(i).getLongitude(),firstLocation.getLatitude(),firstLocation.getLongitude())>45){
                        bestLocation=location.getLastLocation();
                        System.out.println("Location actual(best): "+bestLocation.getLatitude() +", "+ bestLocation.getLongitude());
                        v.setText("Location actual(best): "+bestLocation.getLatitude() +", "+ bestLocation.getLongitude());
                    }
                    //System.out.println("Locacion de : "+locationsGetted.get(i).getBearing());
                }
                */
                if(lastLocation!=null) {
                    if (isBetterLocation(lastLocation, firstLocation) && calcGrados(firstLocation.getLatitude(), firstLocation.getLongitude(), lastLocation.getLatitude(), lastLocation.getLongitude()) > 45) {
                        addElementRaceBestLocations(lastLocation);
                    } else {
                        addElementRaceBestLocations(firstLocation);
                    }
                    //System.out.println("Location actual(best): " + bestLocation.getLatitude() + ", " + bestLocation.getLongitude());
                    //v.setText("Location actual(best): " + bestLocation.getLatitude() + ", " + bestLocation.getLongitude());
                    setFirstLocation(null);
                    setLastLocation(null);
                }
            }
        };
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
        grabarDatos();
    }

    private void grabarDatos(){
        long elapsedMillis = SystemClock.elapsedRealtime() - cronometro.getBase();
        double elapsedSeconds = (elapsedMillis*0.001) /1;
        double elapsedMinutes = (elapsedSeconds*1) /60;

        double[][] coordsarray=new double[raceBestLocations.size()][2];
        /*
        CoordData coords=new CoordData(-31.4293, -64.1752);
        CoordData coords2=new CoordData(-31.433216, -64.18754);
        double[][] coordsarray=new double[2][2];
        coordsarray[0]=coords.getCoordsArray();
        coordsarray[1]=coords2.getCoordsArray();
        */

        CoordData coords;
        for(int i =0;i<raceBestLocations.size();i++){
            coords=new CoordData(raceBestLocations.get(i).getLatitude(), raceBestLocations.get(i).getLongitude());
            coordsarray[i]=coords.getCoordsArray();
        }

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
        String fecha=dia+"/"+mes+"/"+anio+" "+hours+":"+pseudominutes;

        double ritmo=30;
        double distancia=30;

        try {
            String pathJson="/race_data.json";
            JSONSingleton.getInstancia().writeToJSON(pathJson,fecha, coordsarray, elapsedMinutes,  ritmo, distancia, this.getContext(), this.getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pausar(View v){
        if(isCorriendo){
            cronometro.stop();
            pauseOffset=SystemClock.elapsedRealtime()-cronometro.getBase();
            isCorriendo=false;
        }
    }

    public void resetear(View v){
        cronometro.setBase(SystemClock.elapsedRealtime());
        pauseOffset=0;
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


    protected double calcGrados(double startLat, double startLng, double endLat, double endLng){
        double longitude1 = startLng;
        double longitude2 = endLng;
        double latitude1 = Math.toRadians(startLat);
        double latitude2 = Math.toRadians(endLat);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    //private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        //System.out.println("ENTRO ACAAA");
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
            //System.out.println("EL LAST LOCATION FUNCAAAA");
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
