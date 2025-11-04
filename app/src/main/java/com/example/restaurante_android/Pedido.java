package com.example.restaurante_android;

public class Pedido {

    private String _id;
    private String tipo;
    private String descripcion;
    private double precio;
    private String rutaImagen;


    public Pedido(String _id, String tipo, String descripcion, double precio) {
        this._id = _id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Pedido(String _id, String tipo, String descripcion, double precio, String rutaImagen) {
        this._id = _id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
    }


    public String getId() {
        return _id;
    }

    public void setId(String idPedido) {
        this._id = idPedido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @Override
    public String toString() {
        return "Id: " + this._id + " - Tipo " + this.tipo + " - Descripción: " + this.descripcion + " - Precio: " + this.precio;
    }

}
