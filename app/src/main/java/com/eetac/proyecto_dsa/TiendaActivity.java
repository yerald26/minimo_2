package com.eetac.proyecto_dsa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.eetac.proyecto_dsa.model.Item;
import com.eetac.proyecto_dsa.model.PeticionCompra;
import com.eetac.proyecto_dsa.model.TiendaJuego;
import com.eetac.proyecto_dsa.network.RetrofitClient;
import com.eetac.proyecto_dsa.utils.LocalUserManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiendaActivity extends AppCompatActivity {

    private LocalUserManager userManager;
    private TextView tvMonedas;
    private LinearLayout containerObjetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        userManager = new LocalUserManager(this);
        tvMonedas = findViewById(R.id.tvMonedasTienda);
        containerObjetos = findViewById(R.id.containerObjetos);

        // Mostrar las monedas actuales
        tvMonedas.setText(String.valueOf(userManager.getCoins()));

        // Cargar objetos desde el servidor
        cargarObjetos();

        Button btnVolver = findViewById(R.id.btnVolverTienda);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }
    }

    private void cargarObjetos() {
        RetrofitClient.getService().getTienda().enqueue(new Callback<TiendaJuego>() {
            @Override
            public void onResponse(Call<TiendaJuego> call, Response<TiendaJuego> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarObjetos(response.body().getItems());
                } else {
                    Toast.makeText(TiendaActivity.this, "Error al cargar tienda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TiendaJuego> call, Throwable t) {
                Toast.makeText(TiendaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarObjetos(List<Item> items) {
        containerObjetos.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Item item : items) {
            View itemView = inflater.inflate(R.layout.item_tienda_card, containerObjetos, false);
            
            TextView tvNombre = itemView.findViewById(R.id.tvNombreItem);
            TextView tvPrecio = itemView.findViewById(R.id.tvPrecioItem);
            Button btnComprar = itemView.findViewById(R.id.btnComprarItem);

            tvNombre.setText(item.getNombre());
            tvPrecio.setText("Precio: " + (int)item.getPrecio() + " monedas");

            btnComprar.setOnClickListener(v -> realizarCompra(item));

            containerObjetos.addView(itemView);
        }
    }

    public void realizarCompra(Item item) {
        String username = userManager.getLoggedUsername();
        int precio = (int) item.getPrecio();
        
        // Comprobar saldo local antes de enviar (opcional, el server debería validar)
        if (userManager.getCoins() < precio) {
            Toast.makeText(this, "❌ Monedas insuficientes", Toast.LENGTH_SHORT).show();
            return;
        }

        PeticionCompra peticion = new PeticionCompra(username, item.getNombre(), precio);

        RetrofitClient.getService().comprar(peticion).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    int nuevasMonedas = userManager.getCoins() - precio;
                    userManager.updateCoins(nuevasMonedas);
                    userManager.añadirAlInventario(item.getNombre());

                    tvMonedas.setText(String.valueOf(nuevasMonedas));
                    Toast.makeText(TiendaActivity.this, "¡Comprado: " + item.getNombre() + "!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TiendaActivity.this, "❌ Error en la compra", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TiendaActivity.this, "⚠ Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
