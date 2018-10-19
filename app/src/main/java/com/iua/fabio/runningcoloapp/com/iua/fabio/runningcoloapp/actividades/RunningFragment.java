package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.CoordData;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.JSONSingleton;
import org.json.JSONException;
import java.io.IOException;
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
        //if(getArguments().getString("cronometrofrag") == "cronometrofrag"){
            //cronometro=(Chronometer) getArguments().getSerializable("cronometrofrag");
        //}
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

        CoordData coords=new CoordData(-31.4293, -64.1752);
        CoordData coords2=new CoordData(-31.433216, -64.18754);
        double[][] coordsarray=new double[2][2];
        coordsarray[0]=coords.getCoordsArray();
        coordsarray[1]=coords2.getCoordsArray();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        actualV=inflater.inflate(R.layout.fragment_running, container, false);

        cronometro=actualV.findViewById(R.id.cronometro);
        cronometro.setFormat("%s");
        cronometro.setBase(SystemClock.elapsedRealtime());

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
                detener(v);
            }
        });

        Button botonpause=actualV.findViewById(R.id.bpausecron);
        botonpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausar(v);
            }
        });

        Button botonres=actualV.findViewById(R.id.bresetcron);
        botonres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetear(v);
            }
        });

        return actualV;
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
