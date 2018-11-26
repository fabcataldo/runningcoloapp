package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.actividades;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.iua.fabio.runningcoloapp.R;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.SongData;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.AudioCustomListAdapter;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios.MPSingleton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioListFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View actualView;
    private static final int MY_PERMISSION_REQUEST = 1;
    private ArrayList<String> arrayList;
    private ListView listView;
    private ArrayList<SongData> songlist;
    private Context ctx;
    private OnFragmentInteractionListener mListener;
    private AudioCustomListAdapter acla;
    private SongData actualData;
    private SongData nextActualData;

    public AudioListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioListFragment newInstance(String param1, String param2) {
        AudioListFragment fragment = new AudioListFragment();
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
        actualView = inflater.inflate(R.layout.fragment_audio_list, container, false);

        listView = (ListView) actualView.findViewById(R.id.audiolistview);
        ctx = this.getContext();

        //acá pedimos permiso de escritura/lectura de la memoria externa, si es que está presente
        if (ContextCompat.checkSelfPermission(this.getContext()
                , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            doStuff();
        }

        listView.setItemsCanFocus(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actualData = songlist.get(position);
                final Uri resSongID = Uri.parse(actualData.getSong_location());

                int positionplus=position+1;
                nextActualData=songlist.get(positionplus);
                final Uri nextResSongID = Uri.parse(nextActualData.getSong_location());

                MPSingleton.getInstance().prepareMedia(getContext(),resSongID);
                if(!MPSingleton.getInstance().get_mp().isPlaying()){
                    MPSingleton.getInstance().play();
                }
                else{
                    MPSingleton.getInstance().pauseSong();
                }

                MPSingleton.getInstance().get_mp().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        MPSingleton.getInstance().stopSong();
                        MPSingleton.getInstance().prepareMedia(ctx.getApplicationContext(), nextResSongID);
                        MPSingleton.getInstance().play();
                    }
                });
            }
        });

        return actualView;
    }

    public void doStuff() {
        arrayList = new ArrayList<>();
        songlist = new ArrayList<SongData>();
        getMusic();
        acla=new AudioCustomListAdapter(ctx);
        acla.setLcd(songlist);
        listView.setAdapter(acla);
    }

    public void getMusic(){
        ContentResolver contentResolver = getActivity().getContentResolver();

        //Uri, objeto que sirbe para guardar una referencia a una dirección determinada
        //utilizo la Media Store, para, desde la memoria externa, obtener una lista de
        // todo el contenido de audio de la misma
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        //el objeto songcursosr, sirve como indexador de una lista, la que sea,
        //obteniendo como dirección de inicio lo que diga SongUri
        //sirve también, por ej, para indexar un set de respuesta de lo que consultemos
        //a una bd
        //una idea muy similar tiene JSONReader
        Cursor songcursor = contentResolver.query(songUri, null, null, null, null);

        if (songcursor != null && songcursor.moveToFirst()) {
            int songTitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songid = songcursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songAlbum = songcursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do {
                String currentTitle = songcursor.getString(songTitle);
                String currentArtist = songcursor.getString(songArtist);
                String currentLocation = songcursor.getString(songLocation);
                String currentAlbum = songcursor.getString(songAlbum);
                String currentid=songcursor.getString(songid);
                arrayList.add(currentTitle + "\n" + currentArtist);

                songlist.add(new SongData(currentTitle, currentAlbum, currentArtist,
                        "", currentid, currentLocation));
            } while (songcursor.moveToNext());
        }
    }


    //metodo que nos sirve para chequear, lo que pase cuando obtenemos el permiso de escritura
    //o lectura de la memoria externa
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case MY_PERMISSION_REQUEST:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this.getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager. PERMISSION_GRANTED){
                        doStuff();
                    }else{

                    }
                    return;
                }
            }
        }
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
