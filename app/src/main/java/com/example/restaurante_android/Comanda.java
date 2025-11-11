package com.example.restaurante_android;

import java.util.List;

public class Comanda {

    private String _id;
    private String fecha;
    private List<Elemento> elementosSeleccionados;


    public Comanda(String _id) {
        this._id = _id;
    }

    public Comanda(String fecha, List<Elemento> elementosSeleccionados) {
        this.fecha = fecha;
        this.elementosSeleccionados = elementosSeleccionados;
    }

    public Comanda(String _id, String fecha, List<Elemento> elementosSeleccionados) {
        this._id = _id;
        this.fecha = fecha;
        this.elementosSeleccionados = elementosSeleccionados;
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

    public List<Elemento> getElementosSeleccionados() {
        return elementosSeleccionados;
    }

    public void setElementosSeleccionados(List<Elemento> elementosSeleccionados) {
        this.elementosSeleccionados = elementosSeleccionados;
    }
}
