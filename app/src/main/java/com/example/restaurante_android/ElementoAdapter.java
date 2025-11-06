package com.example.restaurante_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ElementoAdapter extends RecyclerView.Adapter<ElementoViewHolder> {

    Context applicationContext;
    Context activityContext;
    List<Elemento> elementos;


    public ElementoAdapter(Context applicationContext, List<Elemento> elementos, Context activityContext) {
        this.applicationContext = applicationContext;
        this.elementos = elementos;
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public ElementoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ElementoViewHolder(LayoutInflater.from(applicationContext).inflate(R.layout.elemento_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ElementoViewHolder holder, int position) {
        //TODO IMPLEMENTAR ADAPTADOR
        Elemento elemento = elementos.get(position);
        holder.tipo.setText(elementos.get(position).getTipo());
        holder.descripcion.setText(elementos.get(position).getDescripcion());
        holder.precio.setText(elementos.get(position).getPrecioFormateado());
        //holder.imagen.setImageResource(elementos.get(position).getRutaImagen());

        // Create an ArrayAdapter using the string array and a default spinner layout.

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.array_cantidades,
            android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        holder.cantidad.setAdapter(adapter);
        //holder.spinner.setSelection(currentItem.getSelectedOptionIndex(), false);

        holder.cantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("SPINNER PULSADO - " + parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("SPINNER SALIDO");
            }
        });

        holder.confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CHECKBOX PULSADO");
            }
        });


//        holder.btnActualizar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("ACTUALIZAR " + elemento.getNombreCompleto());
//                Intent intent = new Intent(applicationContext, MainActivity3.class);
//                // necesario setear esta bandera o lanzará excepción
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                // pasamos contenido del elemento pulsado al nuevo activity
//                intent.putExtra("OBJETO_JUGADOR", elemento.jugador.serializarStringArray());
//                applicationContext.startActivity(intent);
//            }
//        });
//
//        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity activity = (MainActivity) activityContext;
//                activity.realizarPeticionBD(elemento.jugador);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

}
