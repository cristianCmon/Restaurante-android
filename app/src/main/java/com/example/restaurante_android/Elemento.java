package com.example.restaurante_android;

public class Elemento {

    Pedido pedido;
    private String _id;
    private String tipo;
    private String descripcion;
    private double precio;
    private String rutaImagen;


    public Elemento(String tipo, String descripcion, double precio, String rutaImagen) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
    }

    public Elemento(String _id, String tipo, String descripcion, double precio, String rutaImagen) {
        this._id = _id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
    }

    public Elemento(Pedido pedido) {
        this.pedido = pedido;
        rellenarPropiedades();
    }


    private void rellenarPropiedades() {
        this._id = pedido.getId();
        this.tipo = pedido.getTipo();
        this.descripcion = pedido.getDescripcion();
        this.precio = pedido.getPrecio();
        this.rutaImagen = pedido.getRutaImagen();
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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

}
