package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo;

import com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo.CoordData;

import java.io.Serializable;
import java.util.ArrayList;

public class RaceData implements Serializable {
    private ArrayList<CoordData> coords;
    private double distancia;
    private String fecha;
    private double duracion;
    private double ritmo;

    public RaceData(ArrayList<CoordData> coords, double distancia, String fecha, double duracion, double ritmo) {
        this.coords = coords;
        this.distancia = distancia;
        this.fecha = fecha;
        this.duracion = duracion;
        this.ritmo = ritmo;
    }

    public double getRitmo() {
        return ritmo;
    }

    public void setRitmo(double ritmo) {
        this.ritmo = ritmo;
    }

    public ArrayList<CoordData> getCoords() {
        return coords;
    }

    public void setCoords(ArrayList<CoordData> coords) {
        this.coords = coords;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "RaceData{" +
                "coords=" + coords +
                ", distancia=" + distancia +
                ", fecha='" + fecha + '\'' +
                ", duracion=" + duracion +
                ", ritmo=" + ritmo +
                '}';
    }
}
