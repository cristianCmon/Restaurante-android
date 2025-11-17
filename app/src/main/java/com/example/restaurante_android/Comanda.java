package com.example.restaurante_android;

import java.util.List;

public class Comanda {

    private String _id;
    private String fecha;
    private List<String> idMenus;
    private List<Integer> cantidadMenus;


    public Comanda() {};

    public Comanda(String _id) {
        this._id = _id;
    }

    public Comanda(String fecha, List<String> idMenus, List<Integer> cantidadMenus) {
        this.fecha = fecha;
        this.idMenus = idMenus;
        this.cantidadMenus = cantidadMenus;

    }


    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<String> getIdMenus() {
        return idMenus;
    }

    public void setIdMenus(List<String> idMenus) {
        this.idMenus = idMenus;
    }

    public List<Integer> getCantidadMenus() {
        return cantidadMenus;
    }

    public void setCantidadMenus(List<Integer> cantidadMenus) {
        this.cantidadMenus = cantidadMenus;
    }

    @Override
    public String toString() {
        return "id: " + this._id + " | fecha: " + this.fecha;
    }
}
