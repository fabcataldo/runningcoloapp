package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.CoordData;
import java.util.ArrayList;

public class DetailMapRunFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng[] coords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //HAGO ACÁ LA CONVERSIÓN A LATLNG PORQUE EL LATLNG NO ES SERIALIZABLE
        //NO SE LO PUEDO PASAR POR PARÁMETRO A LA ACTIVIDAD, POR LO TANTO
        //LE PASO LAS COORDENADAS COMO UN ARRAYLIST DE DOUBLES
        //Y ACÁ LO CONVIERTO A UN ARREGLO COMUN DE OBJECTS LATLNG
        Bundle args =getArguments();
        ArrayList<CoordData> c= (ArrayList<CoordData>) args.getSerializable("raceDataCoords");
        coords=new LatLng[c.size()];
        for(int i=0;i<c.size();i++){
            coords[i]=new LatLng(c.get(i).getLatitud(), c.get(i).getLongitud());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View actualV = inflater.inflate(R.layout.activity_detail_map_run, container, false);
        return actualV;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);

        Polyline polylines = mMap.addPolyline(new PolylineOptions().clickable(false).add(coords));

        //si no tenés los permisos, pedilos
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(getActivity(), new
                    String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},22);
            return;
        }
        //ya los tenés, activá lo que sigue
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.addMarker(new MarkerOptions().position(coords[0]).title("Punto de inicio"));
        mMap.addMarker(new MarkerOptions().position(coords[coords.length-1]).title("Punto final"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coords[0]));

        //hago zoom, el segundo argumento es qué tanto zoom quiero hacer, del objeto
        //LatLng, del primer argumento
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords[0], 15.0f));
    }

    //el metodo que sigue es para cuando obtuviste los permisos
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
    }
}
