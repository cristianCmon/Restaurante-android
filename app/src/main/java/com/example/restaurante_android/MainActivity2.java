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

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
                //TODO REFACTORIZAR ALERTS Y IMPLEMENTAR PROGRESSBAR
                System.out.println("PEDIR");
                buscarPedidosSeleccionados();
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
                mostrarFacturaFinal();
            }
        });

        realizarPeticionBD();
    }

    public void mostrarFacturaFinal() {
        String pedidoTotal = "";
        double precioPedido = 0;
        String precioFormateado = "";

        for (Elemento e : ElementoAdapter.facturaFinal) {
            pedidoTotal += e.getCantidad() + "x  "+e.getDescripcion() + "\n";
            precioPedido += e.getPrecio() * e.getCantidad();
            System.out.println(e);
        }

        precioFormateado = Double.toString(precioPedido);

        if (precioFormateado.endsWith(".0")) {
            precioFormateado = precioFormateado.substring(0, precioFormateado.length() - 2);
        }

        pedidoTotal += "\n\nPrecio Final: " + precioFormateado + "€\n";

        AlertDialog.Builder alertFacturaFinal = new AlertDialog.Builder(this);
        alertFacturaFinal.setTitle("Factura Mesa " + (Integer.parseInt(mesaSeleccionada.getNumero()) + 1));
        alertFacturaFinal.setMessage("\n" + pedidoTotal);
        alertFacturaFinal.setCancelable(false);

        alertFacturaFinal.setPositiveButton("Pagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ElementoAdapter.alertFacturaFinal.addAll(ElementoAdapter.seleccionados);
//                ElementoAdapter.seleccionados.clear();
                dialog.dismiss();
//                reiniciarActivity();
            }
        });

        alertFacturaFinal.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("CANCELAR");
                dialog.cancel();
            }
        });

        alertFacturaFinal.show();
    }

    public void buscarPedidosSeleccionados() {
        String pedido = "";
        double precioPedido = 0;
        String precioFormateado = "";

        for (Elemento e : ElementoAdapter.seleccionados) {
            pedido += e.getCantidad() + "x  "+e.getDescripcion() + "\n";
            precioPedido += e.getPrecio() * e.getCantidad();
        }

        precioFormateado = Double.toString(precioPedido);

        if (precioFormateado.endsWith(".0")) {
            precioFormateado = precioFormateado.substring(0, precioFormateado.length() - 2);
        }

        if (ElementoAdapter.facturaFinal.isEmpty()) {
            pedido += "\n\nPrecio total: " + precioFormateado + "€\n";

        } else {
            double precioFacturasAnteriores = 0;
            for (Elemento e : ElementoAdapter.facturaFinal) {
                precioFacturasAnteriores += e.getPrecio() * e.getCantidad();
            }
            pedido += "\n\nPrecio total pedido: " + precioFormateado + "€ (+" + precioFacturasAnteriores + "€)\n";
        }

        AlertDialog.Builder alertRealizarPedido = new AlertDialog.Builder(this);
        alertRealizarPedido.setTitle("Pedido Mesa " + (Integer.parseInt(mesaSeleccionada.getNumero()) + 1));
        alertRealizarPedido.setMessage("\n" + pedido);
        alertRealizarPedido.setCancelable(false);

        alertRealizarPedido.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ElementoAdapter.facturaFinal.addAll(ElementoAdapter.seleccionados);
                ElementoAdapter.seleccionados.clear();

                mostrarBarraProgreso();
//                dialog.dismiss();
//                reiniciarActivity();
            }
        });

        alertRealizarPedido.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("CANCELAR");
                dialog.cancel();
            }
        });

        alertRealizarPedido.show();
    }

    public void mostrarBarraProgreso() {
        Dialog customPB = new Dialog(this);
        customPB.setContentView(R.layout.barra_progreso_view);
        customPB.setCancelable(false);

        mesaSeleccionada.setBloqueada(true);
        customPB.show();

        escucharBD();
        //bloquearMesa();

//        do {
//            escucharBD();
//        } while (mesaSeleccionada.isBloqueada());
//        if (mesaSeleccionada.isBloqueada()) {
//            customPB.show();
//        } else {
//            customPB.dismiss();
//        }

//        customPB.dismiss();
    }

    public void escucharBD() {
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                System.out.println("myHandler: here!"); // Do your work here
                handler.postDelayed(this, delay);

                if (!mesaSeleccionada.isBloqueada()) {
                    handler.removeCallbacksAndMessages(null);
                }
            }
        }, delay);
    }

    public void cambiarActivity() {

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
