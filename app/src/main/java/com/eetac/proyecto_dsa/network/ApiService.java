package com.eetac.proyecto_dsa.network;

import com.eetac.proyecto_dsa.model.InventarioJugador;
import com.eetac.proyecto_dsa.model.Item;
import com.eetac.proyecto_dsa.model.PeticionCompra;
import com.eetac.proyecto_dsa.model.TiendaJuego;
import com.eetac.proyecto_dsa.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // POST /api/juego/login
    @POST("juego/login")
    Call<User> login(@Body User credenciales);

    // POST /api/juego/registro
    @POST("juego/registro")
    Call<User> registro(@Body User nuevoUsuario);

    // POST /api/juego/comprar
    @POST("juego/comprar")
    Call<Void> comprar(@Body PeticionCompra peticion);

    // GET /api/juego/inventario/{nombre}
    @GET("juego/inventario/{nombre}")
    Call<InventarioJugador> getInventario(@Path("nombre") String nombre);

    // GET /api/juego/tienda
    @GET("juego/tienda")
    Call<TiendaJuego> getTienda();
}