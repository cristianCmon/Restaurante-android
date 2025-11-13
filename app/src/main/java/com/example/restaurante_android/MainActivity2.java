package com.example.restaurante_android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.w3c.dom.Node;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//TODO TERMINAR BARRA HORIZONTAL, CORREGIR PRECIOS DOBLES DIGITOS PEDIDO ANTERIOR, LIMPIEZA GENERAL
public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtPedidos;
    Button btnPedir, btnPagar;
    Dialog barraProgresoCircular, barraProgresoHorizontal;
    Mesa mesaSeleccionada;
    Comanda comandaMesa;
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
                //TODO REFACTORIZAR ALERTS Y IMPLEMENTAR PROGRESSBAR
                System.out.println("PEDIR");
//                buscarPedidosSeleccionados();
                mostrarVistaPedidoFactura("Pedir");
            }
        });

        // BOTÓN PAGAR
        btnPagar = findViewById(R.id.btnPagarPedido);
        if (ElementoAdapter.facturaFinal.isEmpty()) {
            btnPagar.setEnabled(false);
        } else {
            btnPagar.setEnabled(true);
        }

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO REFACTORIZAR ALERTS Y IMPLEMENTAR PROGRESSBAR
//                mostrarFacturaFinal();
                mostrarVistaPedidoFactura("Pagar");
            }
        });

        // BARRAS PROGRESO BLOQUEANTES USUARIO
        barraProgresoCircular = new Dialog(this);
        barraProgresoCircular.setContentView(R.layout.barra_progreso_view);
        barraProgresoCircular.setCancelable(false);

        barraProgresoHorizontal = new Dialog(this);
        barraProgresoHorizontal.setContentView(R.layout.barra_progreso2_view);
        barraProgresoHorizontal.setCancelable(false);

        // OBTENER MENUS DE BD Y CARGAR ELEMENTOS
        realizarPeticionBD();
    }

    public void mostrarVistaPedidoFactura(String tipoAccion) {
        String pedidoTotal = "";
        double precioPedido = 0;
        String precioFormateado = "";
        String tituloPedidoFactura = "";
        String accionPedidoFactura = "";


        switch (tipoAccion) {
            case "Pedir":
                for (Elemento e : ElementoAdapter.seleccionados) {
                    pedidoTotal += e.getCantidad() + "x  "+e.getDescripcion() + "\n";
                    precioPedido += e.getPrecio() * e.getCantidad();
                }

                tituloPedidoFactura = "Pedido Mesa " + (Integer.parseInt(mesaSeleccionada.getNumero()) + 1);
                accionPedidoFactura = "Confirmar";
                break;

            case "Pagar":
                for (Elemento e : ElementoAdapter.facturaFinal) {
                    pedidoTotal += e.getCantidad() + "x  "+e.getDescripcion() + "\n";
                    precioPedido += e.getPrecio() * e.getCantidad();
                }

                tituloPedidoFactura = "Factura Mesa " + (Integer.parseInt(mesaSeleccionada.getNumero()) + 1);
                accionPedidoFactura = "Pagar";
                break;
        }

        precioFormateado = String.format("%.2f", precioPedido);

        if (precioFormateado.endsWith(".00")) {
            precioFormateado = precioFormateado.substring(0, precioFormateado.length() - 3);
        }

        switch (tipoAccion) {
            case "Pedir":
                if (ElementoAdapter.facturaFinal.isEmpty()) {
                    pedidoTotal += "\n\nPrecio Total:  " + precioFormateado + "€\n";

                } else {
                    double precioFacturasAnteriores = 0;

                    for (Elemento e : ElementoAdapter.facturaFinal) {
                        precioFacturasAnteriores += e.getPrecio() * e.getCantidad();
                    }

                    pedidoTotal += "\n\nPrecio Total:  " + precioFormateado + "€ (+" + precioFacturasAnteriores + "€)\n";
                }
                break;

            case "Pagar":
                pedidoTotal += "\n\nPrecio Final:  " + precioFormateado + "€\n";
                break;
        }

        AlertDialog.Builder alertPedidoFactura = new AlertDialog.Builder(this);
        alertPedidoFactura.setTitle(tituloPedidoFactura);
        alertPedidoFactura.setMessage("\n" + pedidoTotal);
        alertPedidoFactura.setCancelable(false);

        alertPedidoFactura.setPositiveButton(accionPedidoFactura, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO TENER EN CUENTA SI ES PEDIDO O FACTURA
                mostrarPantallaBloqueo(tipoAccion);
                dialog.dismiss();
            }
        });

        alertPedidoFactura.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertPedidoFactura.show();
    }

    public void mostrarPantallaBloqueo(String tipoAccion) {
        // Mientras se muestra la vista de bloqueo al usuario, se envía la comanda a la BD y también se actualiza la mesa en la BD
        switch (tipoAccion) {
            case "Pedir":
                mesaSeleccionada.setBloqueada(true);
                realizarPeticionBD2("Actualizar");
                barraProgresoCircular.show();
                bloquearDispositivo(tipoAccion);
                break;

            case "Pagar":
                mesaSeleccionada.setOcupada(false);
                realizarPeticionBD2("Actualizar");
                barraProgresoHorizontal.show();
                bloquearDispositivo(tipoAccion);
                break;
        }
    }

    public void bloquearDispositivo(String tipoAccion) {

        switch (tipoAccion) {

            case "Pedir":
                Handler manejadorEnvioComanda = new Handler();
                int retardoComanda = 1000;

                manejadorEnvioComanda.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        crearComanda();
                    }
                }, retardoComanda);

                Handler manejadorActualizarMesa = new Handler();
                final int retardoMesa = 2500;

                manejadorActualizarMesa.postDelayed(new Runnable() {
                    public void run() {
                        manejadorActualizarMesa.postDelayed(this, retardoMesa);

                        realizarPeticionBD2("Leer");

                        if (!mesaSeleccionada.isBloqueada()) {
                            // https://stackoverflow.com/questions/7407242/how-to-cancel-handler-postdelayed
                            manejadorActualizarMesa.removeCallbacksAndMessages(null);
                            barraProgresoCircular.dismiss();
                            reiniciarActivity();
                            //Toast.makeText(getApplicationContext(), "PEDIDO PROCESADO", Toast.LENGTH_LONG).show();
                        }
                    }
                }, retardoMesa);

                break;

            case "Pagar":
                Handler manejadorDespacharCliente = new Handler();
                int retardoVolverSeleccionMesa = 10000;

                manejadorDespacharCliente.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ElementoAdapter.facturaFinal.clear();
                        ElementoAdapter.seleccionados.clear();
                        barraProgresoHorizontal.dismiss();
                        volverVistaPrincipal();
                    }
                }, retardoVolverSeleccionMesa);

                break;
        }

    }

    public void volverVistaPrincipal() {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }

    public void reiniciarActivity() {
        this.recreate();
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
                    elementos.add(new Elemento(p));
                }

                for (Pedido p : pedidos) {
                    System.out.println(p);
                }
                // CARGAR DE ELEMENTOS EL RECYCLERVIEW
                recyclerView.setAdapter(new ElementoAdapter(getApplicationContext(), elementos, MainActivity2.this));
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    public void realizarPeticionBD2(String tipo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory
                        .create()).build();

        ApiMongo api = retrofit.create(ApiMongo.class);

        switch (tipo) {
            case "Leer":
                Call<Mesa> llamada = api.leerMesaLocal(mesaSeleccionada.getId());

                llamada.enqueue(new Callback<Mesa>() {
                    @Override
                    public void onResponse(Call<Mesa> call, Response<Mesa> response) {
                        // en el body de la respuesta están los documentos de la colección
                        Mesa data = response.body();
                        // cargamos documentos obtenidos de la bd como elementos de la lista
                        mesaSeleccionada.setBloqueada(data.isBloqueada());
                    }

                    @Override
                    public void onFailure(Call<Mesa> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR", t.toString());
                    }
                });
                break;

            case "Actualizar":
                String idMesa = mesaSeleccionada.getId();

                // Cambiamos estado en base de datos
                Call<Mesa> actualizar = api.actualizarMesa(
                        idMesa, mesaSeleccionada.isOcupada(), mesaSeleccionada.isBloqueada()
                );

                actualizar.enqueue(new Callback<Mesa>() {
                    @Override
                    public void onResponse(Call<Mesa> call, Response<Mesa> response) {
//                        Toast.makeText(getApplicationContext(), "MESA ACTUALIZADA", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Mesa> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

    public void crearComanda() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory
                        .create()).build();

        ApiMongo api = retrofit.create(ApiMongo.class);

        // Creación Comanda
        List<String> idMenus = new ArrayList<>();
        List<Integer> cantidadMenus = new ArrayList<>();

        for (Elemento e : ElementoAdapter.seleccionados) {
            idMenus.add(e.getId());
            cantidadMenus.add(e.getCantidad());
        }
        // TODO REVISAR INSERCIÓN DE MENÚS Y CANTIDADES
        comandaMesa = new Comanda(
                mesaSeleccionada.getId(), new Date().toString(), idMenus, cantidadMenus
        );

        // Envío Comanda a base de datos
        Call<Comanda> llamada = api.crearComanda(
                comandaMesa.getIdMesa(), comandaMesa.getFecha(), comandaMesa.getIdMenus(), comandaMesa.getCantidadMenus()
        );

        llamada.enqueue(new Callback<Comanda>() {
            @Override
            public void onResponse(Call<Comanda> call, Response<Comanda> response) {
                // En esta respuesta obtendremos el id de la comanda generado en Mongo
                comandaMesa.setId(response.body().getId());

                // Actualizamos listas en adaptador
                ElementoAdapter.facturaFinal.addAll(ElementoAdapter.seleccionados);
                ElementoAdapter.seleccionados.clear();
            }

            @Override
            public void onFailure(Call<Comanda> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
//                Log.d("ERROR", t.toString());
            }
        });
    }

}
