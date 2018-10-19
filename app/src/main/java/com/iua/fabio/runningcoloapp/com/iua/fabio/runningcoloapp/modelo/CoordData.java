package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo;

import java.io.Serializable;

public class CoordData implements Serializable {
    private double latitud;
    private double longitud;

    public CoordData(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double[] getCoordsArray(){
        double[] latlongdata=new double[2];
        latlongdata[0]=getLatitud();
        latlongdata[1]=getLongitud();
        return latlongdata;
    }
    @Override
    public String toString() {
        return "{" +
                "Latitud:" + getLatitud() +
                ", Longitud:" + getLongitud() +
                '}';
    }
}
