package com.example.restaurante_android;

public class Mesa {

    private String _id;
    private String numero;
    private boolean ocupada;
    private boolean bloqueada;


    public Mesa(String id, String numero, boolean ocupada, boolean bloqueada) {
        this._id = id;
        this.numero = numero;
        this.ocupada = ocupada;
        this.bloqueada = bloqueada;
    }

    public Mesa(String[] propiedades) {
        this._id = propiedades[0];
        this.numero = propiedades[1];
        this.ocupada = Boolean.parseBoolean(propiedades[2]);
        this.bloqueada = Boolean.parseBoolean(propiedades[3]);
    }


    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }


    @Override
    public String toString() {
        return this.getId() + " - " + this.getNumero() + " - " + this.isOcupada() + " - " + this.isBloqueada();
    }

    public String[] getObjetoSerializado() {
        return new String[]{getId(), getNumero(), Boolean.toString(isOcupada()), Boolean.toString(isBloqueada())};
    }

}
