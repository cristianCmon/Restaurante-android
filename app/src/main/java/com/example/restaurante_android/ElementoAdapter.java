package com.example.restaurante_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ElementoAdapter extends RecyclerView.Adapter<ElementoViewHolder> {

    Context applicationContext;
    Context activityContext; // necesario para acceder a métodos de MainActivity
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
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

}
