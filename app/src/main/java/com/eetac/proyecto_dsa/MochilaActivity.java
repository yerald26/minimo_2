package com.eetac.proyecto_dsa;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.eetac.proyecto_dsa.model.InventarioJugador;
import com.eetac.proyecto_dsa.model.InventarioJugador;
import com.eetac.proyecto_dsa.network.RetrofitClient;
import com.eetac.proyecto_dsa.utils.LocalUserManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MochilaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mochila);

        LocalUserManager userManager = new LocalUserManager(this);
        TextView tvLista = findViewById(R.id.tvListaObjetos);
        Button btnVolver = findViewById(R.id.btnVolverMochila);

        tvLista.setText("Cargando inventario...");

        // Llamada al servidor para obtener el inventario real
        String username = userManager.getLoggedUsername();
        RetrofitClient.getService().getInventario(username).enqueue(new Callback<InventarioJugador>() {
            @Override
            public void onResponse(Call<InventarioJugador> call, Response<InventarioJugador> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> inventario = response.body().getObjetos();
                    if (inventario == null || inventario.isEmpty()) {
                        tvLista.setText("Tu mochila está vacía.\n¡Ve a la tienda!");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Objetos en el servidor:\n\n");
                        for (String objeto : inventario) {
                            sb.append("⚔️ ").append(objeto).append("\n");
                        }
                        tvLista.setText(sb.toString());
                    }
                } else {
                    tvLista.setText("Error al cargar inventario: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<InventarioJugador> call, Throwable t) {
                tvLista.setText("⚠ Sin conexión con el servidor");
                Toast.makeText(MochilaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });

        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }
    }
}
