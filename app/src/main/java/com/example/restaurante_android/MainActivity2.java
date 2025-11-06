package com.example.restaurante_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtPedidos;
    Button btnPedir, btnPagar;
    Mesa mesaSeleccionada;
    List<Pedido> pedidos = new ArrayList<>();
    List<Elemento> elementos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent datoRecibido = getIntent();
        if (datoRecibido != null) {
            mesaSeleccionada = new Mesa(datoRecibido.getStringArrayExtra("mesa"));
        }

        System.out.println(mesaSeleccionada);
        activarComponentesActivity();
    }

    public void activarComponentesActivity() {
        txtPedidos = findViewById(R.id.txtVistaPedidos);
        String titulo = "PEDIDOS MESA " + (Integer.parseInt(mesaSeleccionada.getNumero()) + 1);
        txtPedidos.setText(titulo);

        // LISTA DE ELEMENTOS
        recyclerView = findViewById(R.id.rvPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // BOTÓN HACER PEDIDO
        btnPedir = findViewById(R.id.btnRealizarPedido);
        btnPedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO LANZAR VISTA ENVIAR PEDIDO Y VOLVER A VISTA PEDIDO
                System.out.println("PEDIR");
            }
        });

        // BOTÓN PAGAR
        btnPagar = findViewById(R.id.btnPagarPedido);
        btnPagar.setEnabled(false);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO LANZAR VISTA PAGO Y VOLVER A VISTA PRINCIPAL
                System.out.println("PAGAR");
            }
        });

        realizarPeticionBD();
    }

    public void cambiarActivity() {

    }

    public void realizarPeticionBD() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory
                        .create()).build();

        ApiMongo api = retrofit.create(ApiMongo.class);

        Call<List<Pedido>> llamada = api.leerMenusLocal();

        llamada.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                // en el body de la respuesta están los documentos de la colección
                List<Pedido> data = response.body();
                // cargamos documentos obtenidos de la bd como elementos de la lista
                for (Pedido p : data) {
                    //pedidos.add(new Pedido(p.getId(), p.getTipo(), p.getDescripcion(), p.getPrecio(), p.getRutaImagen()));
                    elementos.add(new Elemento(p));
                }

                for (Pedido p : pedidos) {
                    System.out.println(p);
                }

                recyclerView.setAdapter(new ElementoAdapter(getApplicationContext(), elementos, MainActivity2.this));
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }
}