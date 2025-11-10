package com.example.restaurante_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiMongo {

    @GET("mesas")
    Call<List<Mesa>> leerMesasLocal();

    @GET("mesas/{id}")
    Call<Mesa> leerMesaLocal(
        @Path("id") String id
    );

    @GET("menus")
    Call<List<Pedido>> leerMenusLocal();

    @FormUrlEncoded
    @PUT("mesas/{id}")
    Call<Mesa> actualizarMesa(
        @Path("id") String id,
        @Field("ocupada") boolean ocupada,
        @Field("bloqueada") boolean bloqueada
    );

    @FormUrlEncoded
    @POST("comandas")
    Call<Pedido> crearComanda(
        @Field("id") String id,
        @Field("tipo") String tipo,
        @Field("descripcion") String descripcion,
        @Field("precio") String precio,
        @Field("rutaImagen") String rutaImagen
    );

//    @GET("jugadores")
//    Call<List<Jugador>> leerColeccion();
//
//    @GET("jugadores/{id}")
//    Call<Jugador> leerDocumento(@Path("id") String id);
//
//    @FormUrlEncoded
//    @POST("jugadores/crear")
//    Call<Jugador> crearDocumento(
//        @Field("nombre") String nombre,
//        @Field("apellidos") String apellidos,
//        @Field("edad") String edad,
//        @Field("posicion") String posicion,
//        @Field("club") String club,
//        @Field("dorsal") String dorsal,
//        @Field("urlFoto") String urlFoto
//    );
//
//    @FormUrlEncoded
//    @PUT("jugadores/actualizar/{id}")
//    Call<Jugador> actualizarDocumento(
//        @Path("id") String id,
//        @Field("nombre") String nombre,
//        @Field("apellidos") String apellidos,
//        @Field("edad") String edad,
//        @Field("posicion") String posicion,
//        @Field("club") String club,
//        @Field("dorsal") String dorsal,
//        @Field("urlFoto") String urlFoto
//    );
//
//    @DELETE("jugadores/borrar/{id}")
//    Call<Jugador> borrarDocumento(@Path("id") String id);

}