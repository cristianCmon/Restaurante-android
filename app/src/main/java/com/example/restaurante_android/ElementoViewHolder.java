package com.example.restaurante_android;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ElementoViewHolder extends RecyclerView.ViewHolder {

    ImageView imagen;
    TextView tipo, descripcion, precio;
    Spinner cantidad;
    CheckBox confirmar;
    ConstraintLayout parentLayout;

    public ElementoViewHolder(@NonNull View itemView) {
        super(itemView);
        imagen = itemView.findViewById(R.id.imgPedido);

        tipo = itemView.findViewById(R.id.txtTipo);
        descripcion = itemView.findViewById(R.id.txtDescripcion);
        precio = itemView.findViewById(R.id.txtPrecio);

        cantidad = itemView.findViewById(R.id.spCantidad);
        confirmar = itemView.findViewById(R.id.checkEscoger);

        parentLayout = itemView.findViewById(R.id.pedidosLayout);
    }
}
