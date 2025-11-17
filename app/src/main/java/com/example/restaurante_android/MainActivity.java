package com.example.restaurante_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    List<Mesa> mesas = new ArrayList<>();
    int indiceMesa = 0;
    Button btnMesa1, btnMesa2, btnMesa3, btnMesa4, btnMesa5;
    List<Button> btnMesas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activarComponentesActivity();
    }

    public void activarComponentesActivity() {
        btnMesa1 = findViewById(R.id.btnMesa1);
        btnMesa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MESA", "1");
                indiceMesa = 0;
                cambiarActivity();
            }
        });

        btnMesa2 = findViewById(R.id.btnMesa2);
        btnMesa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MESA", "2");
                indiceMesa = 1;
                cambiarActivity();
            }
        });

        btnMesa3 = findViewById(R.id.btnMesa3);
        btnMesa3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MESA", "3");
                indiceMesa = 2;
                cambiarActivity();
            }
        });

        btnMesa4 = findViewById(R.id.btnMesa4);
        btnMesa4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MESA", "4");
                indiceMesa = 3;
                cambiarActivity();
            }
        });

        btnMesa5 = findViewById(R.id.btnMesa5);
        btnMesa5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MESA", "5");
                indiceMesa = 4;
                cambiarActivity();
            }
        });

        // Añadimos botones seteados a la lista
        btnMesas.add(btnMesa1);
        btnMesas.add(btnMesa2);
        btnMesas.add(btnMesa3);
        btnMesas.add(btnMesa4);
        btnMesas.add(btnMesa5);

        realizarPeticionBD("Leer");
    }

    public void cambiarActivity() {
        realizarPeticionBD("Actualizar");

        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("mesa", mesas.get(indiceMesa).getObjetoSerializado());
        startActivity(intent);
    }

    public void realizarPeticionBD(String tipo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory
                        .create()).build();

        ApiMongo api = retrofit.create(ApiMongo.class);

        switch (tipo) {
            case "Leer":
                Call<List<Mesa>> llamada = api.leerMesasLocal();

                llamada.enqueue(new Callback<List<Mesa>>() {
                    @Override
                    public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                        // en el body de la respuesta están los documentos de la colección
                        System.out.println(response);
                        System.out.println(response.body());
                        List<Mesa> data = response.body();
                        // cargamos documentos obtenidos de la bd como elementos de la lista
                        for (Mesa m : data) {
                            mesas.add(new Mesa(m.getId(), m.getNumero(), m.isOcupada(), m.isBloqueada(), m.getIdComanda()));
                        }
                        // Seteamos los botones de las mesas según estén ocupadas o no
                        for (int i = 0; i < mesas.size(); i++) {
                            btnMesas.get(i).setEnabled(!mesas.get(i).isOcupada());
                        }

        //                for (Mesa m : mesas) {
        //                    System.out.println(m);
        //                }
                    }

                    @Override
                    public void onFailure(Call<List<Mesa>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR", t.toString());
                    }
                });
                break;

            case "Actualizar":
                String idMesa = mesas.get(indiceMesa).getId();

                // Cambiamos estado en base de datos
                Call<Mesa> actualizar = api.actualizarMesa(
                    idMesa, true, false, ""
                );
                // Y también en el objeto
                mesas.get(indiceMesa).setOcupada(true);

                actualizar.enqueue(new Callback<Mesa>() {
                    @Override
                    public void onResponse(Call<Mesa> call, Response<Mesa> response) {
                        //Toast.makeText(getApplicationContext(), "MESA ACTUALIZADA", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Mesa> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }

    }

}
