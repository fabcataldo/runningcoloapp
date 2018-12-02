package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.utilitarios;

import android.app.Activity;
import android.content.Context;
import android.util.JsonReader;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.CoordData;
import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.RaceData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONSingleton {
    private static JSONSingleton instancia=null;
    private static InputStream is=null;
    private static List<RaceData> raceDataList=null;

    public JSONSingleton(){

    }

    public static JSONSingleton getInstancia(){
        if(instancia==null){
            instancia=new JSONSingleton();
        }
        return instancia;
    }

    public void setIn(InputStream in){
        getInstancia().is=in;
    }

    public InputStream getIn(){
        return getInstancia().is;
    }

    public void setRaceDataList(List<RaceData> lrd){
        getInstancia().raceDataList=lrd;
    }

    public List<RaceData> getRaceDataList(){
        return getInstancia().raceDataList;
    }

    //-----------------------------------------------------------------------------------

    public static List<RaceData> getJsonStream() throws IOException {
        InputStreamReader isr = new InputStreamReader(getInstancia().getIn());
        JsonReader reader = new JsonReader(isr);
        getInstancia().setRaceDataList(getDataArray(reader));
        return getInstancia().getRaceDataList();
    }

    public static List<RaceData> getDataArray(JsonReader reader) throws IOException{
        ArrayList<RaceData> al=new ArrayList<RaceData>();
        reader.beginArray();
        while(reader.hasNext()){
            al.add(getData(reader));
        }
        reader.endArray();
        return al;
    }

    private static RaceData getData(JsonReader reader) throws IOException{
        ArrayList<CoordData> coords=new ArrayList<CoordData>();
        double distancia=0;
        double duracion=0;
        String fecha="xx";
        double ritmo=0;

        reader.beginObject();
        while(reader.hasNext()) {
            if (reader.nextName().equals("fecha") ) {
                fecha = reader.nextString();
            }
            if (reader.nextName().equals("coords") ) {
                coords = parseCoordenadas(reader);
            }
            if (reader.nextName().equals("distancia") ) {
                distancia = reader.nextDouble();
            }
            if (reader.nextName().equals("duracion") ){
                duracion = reader.nextDouble();
            }
            if (reader.nextName().equals("ritmo") ){
                ritmo = reader.nextDouble();
            }
            else{
                reader.skipValue();
            }
        }
        reader.endObject();

        RaceData rd=new RaceData(coords, distancia, fecha, duracion, ritmo);
        return rd;
    }

    private static ArrayList<CoordData> parseCoordenadas(JsonReader reader) throws IOException {
        ArrayList<CoordData> temp=new ArrayList<CoordData>();

        reader.beginArray();

        while(reader.hasNext()){
            temp.add(getLatLongData(reader));
        }
        reader.endArray();

        return temp;
    }

    private static CoordData getLatLongData(JsonReader reader) throws IOException{
        double lat=0;
        double longitud=0;
        String op="xx";
        CoordData elementTempList=null;

        reader.beginObject();
        while(reader.hasNext()) {
            op=reader.nextName();
            switch (op) {
                case "Latitud":
                    lat = reader.nextDouble();
                    break;
                case "Longitud":
                    longitud = reader.nextDouble();
                    break;

                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        elementTempList = new CoordData(lat,longitud);
        return elementTempList;
    }

    //------------------------------------------------------------------------------------

    public void deleteAObjFromJSON ( String pathJSON, int posObj, Context ctxt) throws JSONException, IOException {
        File myfile= new File(ctxt.getFilesDir()+pathJSON);
        JSONArray raceDataJSON=new JSONArray();

        FileInputStream fis=new FileInputStream(ctxt.getFilesDir()+pathJSON);
        getInstancia().setIn(fis);
        getInstancia().setRaceDataList(getJsonStream());

        //UNA VEZ QUE OBTENGO TODOS LOS OBJETOS DEL JSON, VOY LLENANDO EL NUEVO
        //JSONARRAY que va a tener los objetos del json
        //después le agrego el objeto nuevo
        JSONArray coordsArrObjsJSON=new JSONArray();
        getInstancia().getRaceDataList().remove(posObj);

        for(int i=0;i<getInstancia().getRaceDataList().size();i++){
            JSONObject raceDataElement=new JSONObject();
            raceDataElement.put("fecha", getInstancia().getRaceDataList().get(i).getFecha());

            for(int j=0;j<getInstancia().getRaceDataList().get(i).getCoords().size();j++){
                JSONObject coordsJSONObj=new JSONObject();
                coordsJSONObj.put("Latitud", getInstancia().getRaceDataList().get(i).getCoords().get(j).getLatitud());
                coordsJSONObj.put("Longitud", getInstancia().getRaceDataList().get(i).getCoords().get(j).getLongitud());
                coordsArrObjsJSON.put(j, coordsJSONObj);
            }
            raceDataElement.put("coords", coordsArrObjsJSON);
            raceDataElement.put("distancia", getInstancia().getRaceDataList().get(i).getDistancia());
            raceDataElement.put("duracion", getInstancia().getRaceDataList().get(i).getDuracion());
            raceDataElement.put("ritmo", getInstancia().getRaceDataList().get(i).getRitmo());
            raceDataJSON.put(raceDataElement);
        }

        FileOutputStream fos =  new FileOutputStream(myfile, false);

        fos.write(raceDataJSON.toString().getBytes());
        fos.flush();
        fos.close();

    }
    public void writeToJSON(String pathJSON, String fecha, double[][] coords, double duracion, double ritmo, double distancia, Context ctxt, Activity act) throws IOException, JSONException {
        File myfile= new File(ctxt.getFilesDir()+pathJSON);
        JSONArray raceDataJSON=new JSONArray();


        if(myfile.exists() == false){
            myfile.createNewFile();
        }
        else{
            FileInputStream fis=new FileInputStream(ctxt.getFilesDir()+pathJSON);
            getInstancia().setIn(fis);
            getInstancia().setRaceDataList(getJsonStream());

            //UNA VEZ QUE OBTENGO TODOS LOS OBJETOS DEL JSON, VOY LLENANDO EL NUEVO
            //JSONARRAY que va a tener los objetos del json
            //después le agrego el objeto nuevo
            JSONArray coordsArrObjsJSON=new JSONArray();

            for(int i=0;i<getInstancia().getRaceDataList().size();i++){
                JSONObject raceDataElement=new JSONObject();
                raceDataElement.put("fecha", getInstancia().getRaceDataList().get(i).getFecha());

                for(int j=0;j<getInstancia().getRaceDataList().get(i).getCoords().size();j++){
                    JSONObject coordsJSONObj=new JSONObject();
                    coordsJSONObj.put("Latitud", getInstancia().getRaceDataList().get(i).getCoords().get(j).getLatitud());
                    coordsJSONObj.put("Longitud", getInstancia().getRaceDataList().get(i).getCoords().get(j).getLongitud());
                    coordsArrObjsJSON.put(j, coordsJSONObj);
                }
                raceDataElement.put("coords", coordsArrObjsJSON);

                raceDataElement.put("distancia", getInstancia().getRaceDataList().get(i).getDistancia());
                raceDataElement.put("duracion", getInstancia().getRaceDataList().get(i).getDuracion());
                raceDataElement.put("ritmo", getInstancia().getRaceDataList().get(i).getRitmo());

                raceDataJSON.put(raceDataElement);
            }
        }

        FileOutputStream fos =  new FileOutputStream(myfile, false);

        //EL NUEVO OBJETO, lo agrego ahora
        JSONObject raceDetails= new JSONObject();
        JSONArray coordsArrayDetails=new JSONArray();

        for(int i=0;i<coords.length;i++){
            JSONObject coordsJSONObj=new JSONObject();
            coordsJSONObj.put("Latitud", coords[i][0]);
            coordsJSONObj.put("Longitud", coords[i][1]);
            coordsArrayDetails.put(i, coordsJSONObj);
        }

        raceDetails.put("fecha", fecha);
        raceDetails.put("coords", coordsArrayDetails);
        raceDetails.put("distancia", distancia);
        raceDetails.put("duracion", duracion);
        raceDetails.put("ritmo", ritmo);

        raceDataJSON.put(raceDetails);

        fos.write(raceDataJSON.toString().getBytes());
        fos.flush();
        fos.close();
    }
}
